package com.example.backoffice.repository;

import java.util.List;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.TrajetReservation;

public class TrajetReservationRepository {
    private final DAO dao;

    public TrajetReservationRepository(DAO dao) {
        this.dao = dao;
    }

    public List<TrajetReservation> getByTrajet(Integer trajetId, boolean alphabetique) throws Exception {

        String sql = """
                    SELECT tr.*
                    FROM trajet_reservation tr
                    JOIN reservation r
                    ON r.id = tr.id_reservation
                    JOIN hotel h 
                    ON h.id = r.id_hotel
                    WHERE tr.id_trajet = ?
                """;

        if (alphabetique) {
            sql += " ORDER BY h.nom ASC";
        } else {
            sql += " ORDER BY tr.ordre ASC";
        }

        return dao.getList(sql, TrajetReservation.class, trajetId);
    }

    public void save(TrajetReservation trajetReservation) throws Exception {

        if (trajetReservation.getId() == null) {

            String sql = """
                        INSERT INTO trajet_reservation (id_trajet, id_reservation, ordre)
                        VALUES (?, ?, ?)
                    """;

            dao.executeUpdate(
                    sql,
                    trajetReservation.getTrajet().getId(),
                    trajetReservation.getReservation().getId(),
                    trajetReservation.getOrdre());
        } else {

            String sql = """
                        UPDATE trajet_reservation
                        SET id_trajet = ?, id_reservation = ?, ordre = ?
                        WHERE id = ?
                    """;

            dao.executeUpdate(
                    sql,
                    trajetReservation.getTrajet().getId(),
                    trajetReservation.getReservation().getId(),
                    trajetReservation.getOrdre(),
                    trajetReservation.getId());
        }
    }
}
