package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import java.util.List;
import java.time.LocalDateTime;


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

      public List<Reservation> getAll() throws Exception {
        return DAO.getList("SELECT * FROM reservation", Reservation.class);
    }

    public List<Reservation> getByDateArrive(LocalDateTime dateArrive) throws Exception {
        return DAO.getList("SELECT * FROM reservation WHERE date(date_arrive) = date(?)", Reservation.class, dateArrive);
    }
}
