package com.example.backoffice.service;

import com.example.backoffice.repository.ReservationRepository;
import com.example.model.Hotel;
import com.example.model.Reservation;

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
}
