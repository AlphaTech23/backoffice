package com.example.backoffice.service;

import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import java.time.LocalDateTime;

public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService() {
        this.reservationRepository = new ReservationRepository();
    }

    public Reservation reserver(String idClient,
                                Integer nombrePassager,
                                LocalDateTime dateArrive,
                                Integer idHotel) throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(idHotel);

        Reservation reservation = new Reservation();
        reservation.setIdClient(idClient);
        reservation.setNombrePassager(nombrePassager);
        reservation.setDateArrive(dateArrive);
        reservation.setHotel(hotel);

        reservationRepository.save(reservation);

        return reservation;
    }

    public List<Reservation> getByDateArrive(String dateStr) throws Exception {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return reservationRepository.getAll();
        }

        // Expected format yyyy-MM-dd
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime startOfDay = date.atStartOfDay();
        
        return reservationRepository.getByDateArrive(startOfDay);
    }
}
