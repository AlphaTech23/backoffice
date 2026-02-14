package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.model.Reservation;

import java.sql.Timestamp;

public class ReservationRepository {

    public int save(Reservation reservation) throws Exception {

        String sql = """
                INSERT INTO reservation(id_client, nombre_passager, date_arrive, id_hotel)
                VALUES (?, ?, ?, ?)
                """;

        return DAO.executeUpdate(
                sql,
                reservation.getIdClient(),
                reservation.getNombrePassager(),
                Timestamp.valueOf(reservation.getDateArrive()),
                reservation.getHotel().getId()
        );
    }
}
