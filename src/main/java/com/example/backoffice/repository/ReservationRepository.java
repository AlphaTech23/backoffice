package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.sql.Timestamp;

public class ReservationRepository {
    private final DAO dao;

    public ReservationRepository(DAO dao) {
        this.dao = dao;
    }

    public int save(Reservation reservation) throws Exception {

        if (reservation.getId() != null) {
            String sql = """
                        UPDATE reservation
                        SET id_client = ?,
                            nombre_passager = ?,
                            date_arrivee = ?,
                            id_hotel = ?,
                        WHERE id = ?
                    """;

            return dao.executeUpdate(
                    sql,
                    reservation.getIdClient(),
                    reservation.getNombrePassager(),
                    Timestamp.valueOf(reservation.getDateArrivee()),
                    reservation.getHotel().getId(),
                    reservation.getId());

        } else {
            String sql = """
                        INSERT INTO reservation(id_client, nombre_passager, date_arrivee, id_hotel)
                        VALUES (?, ?, ?, ?)
                    """;

            Integer id = dao.executeUpdate(
                    sql,
                    reservation.getIdClient(),
                    reservation.getNombrePassager(),
                    Timestamp.valueOf(reservation.getDateArrivee()),
                    reservation.getHotel().getId());
            if(id > 0) reservation.setId(id);
            return id;
        }
    }

    public List<Reservation> getAll() throws Exception {

        String sql = """
                    SELECT *
                    FROM reservation
                    ORDER BY date_arrivee ASC
                """;

        return dao.getList(sql, Reservation.class);
    }

    public List<Reservation> getByDateArrivee(LocalDateTime dateArrivee) throws Exception {
        String sql = """
            SELECT * FROM reservation 
            WHERE date(date_arrivee) = date(?) 
            ORDER BY date_arrivee ASC
        """;
        return dao.getList(sql, Reservation.class,
                Timestamp.valueOf(dateArrivee));
    }

    public List<Reservation> getNonAssigne(LocalDate date) throws Exception {
        String sql = """
                    SELECT r.*
                    FROM reservation r
                    LEFT JOIN trajet_reservation tr
                    ON tr.id_reservation = r.id
                    WHERE tr.id IS NULL
                """;

        if (date != null) {
            sql += " AND DATE(date_arrivee) = ?";
            return dao.getList(sql, Reservation.class, java.sql.Date.valueOf(date));
        } else {
            return dao.getList(sql, Reservation.class);
        }
    }

    public List<Reservation> getAssignees(Integer idTrajet) throws Exception {
        String sql = """
                    SELECT r.*
                    FROM reservation r
                    LEFT JOIN trajet_reservation tr
                    ON tr.id_reservation = r.id
                    WHERE tr.id_trajet = ?
                """;
        return dao.getList(sql, Reservation.class, idTrajet);
    }
}
