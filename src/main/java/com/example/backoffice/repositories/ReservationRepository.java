package com.example.backoffice.repositories;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.models.Reservation;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepository {

    public List<Reservation> getAll() throws Exception {
        return DAO.getList("SELECT * FROM reservation", Reservation.class);
    }

    public List<Reservation> getByDateArrive(LocalDateTime dateArrive) throws Exception {
        // Casting timestamp to date for day matching as per requirement usually for 'date' param
        // PostreSQL syntax: date(date_arrive)
        return DAO.getList("SELECT * FROM reservation WHERE date(date_arrive) = date(?)", Reservation.class, dateArrive);
    }
}
