package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.sql.Timestamp;

public class ReservationRepository {

    public int save(Reservation reservation) throws Exception {

        if (reservation.getId() != null) {
            // UPDATE
            String sql = """
                        UPDATE reservation
                        SET id_client = ?,
                            nombre_passager = ?,
                            date_arrivee = ?,
                            id_hotel = ?,
                            ordre = ?,
                            id_trajet = ?
                        WHERE id = ?
                    """;

            return DAO.executeUpdate(
                    sql,
                    reservation.getIdClient(),
                    reservation.getNombrePassager(),
                    Timestamp.valueOf(reservation.getDateArrive()),
                    reservation.getHotel().getId(),
                    reservation.getOrdre(),
                    reservation.getTrajet() != null ? reservation.getTrajet().getId() : null,
                    reservation.getId());

        } else {
            // INSERT
            String sql = """
                        INSERT INTO reservation(id_client, nombre_passager, date_arrivee, id_hotel, ordre, id_trajet)
                        VALUES (?, ?, ?, ?, ?, ?)
                    """;

            return DAO.executeUpdate(
                    sql,
                    reservation.getIdClient(),
                    reservation.getNombrePassager(),
                    Timestamp.valueOf(reservation.getDateArrive()),
                    reservation.getHotel().getId(),
                    reservation.getOrdre(),
                    reservation.getTrajet() != null ? reservation.getTrajet().getId() : null);
        }
    }

    public List<Reservation> getAll() throws Exception {

        String sql = """
                    SELECT *
                    FROM reservation
                    ORDER BY date_arrivee ASC
                """;

        return DAO.getList(sql, Reservation.class);
    }

    public List<Reservation> getByDateArrive(LocalDateTime dateArrive) throws Exception {
        return DAO.getList("SELECT * FROM reservation WHERE date(date_arrive) = date(?)", Reservation.class,
                Timestamp.valueOf(dateArrive));
    }

    public List<Reservation> getNonAssigne(LocalDate date) throws Exception {

        String sql = """
                    SELECT *
                    FROM reservation
                    WHERE id_trajet IS NULL
                """;

        if (date != null) {
            sql += " AND DATE(date_arrivee) = ?";
            return DAO.getList(sql, Reservation.class, java.sql.Date.valueOf(date));
        } else {
            return DAO.getList(sql, Reservation.class);
        }
    }

    public List<Reservation> getByTrajet(Integer trajetId, boolean alphabetique) throws Exception {

        String sql = """
                    SELECT r.*
                    FROM reservation r
                    JOIN hotel h ON r.id_hotel = h.id
                    WHERE r.id_trajet = ?
                """;

        if (alphabetique) {
            sql += " ORDER BY h.libelle ASC";
        } else {
            sql += " ORDER BY r.ordre ASC";
        }

        return DAO.getList(sql, Reservation.class, trajetId);
    }
}
