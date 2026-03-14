package com.example.backoffice.service;

import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.util.Utils;
import com.example.backoffice.dao.DAO; 
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetService trajetService;
    private final ParametreRepository parametreRepository;
    private final DistanceService distanceService;
    private final HotelRepository hotelRepository;

    public ReservationService(DAO dao) {
        this.reservationRepository = new ReservationRepository(dao);
        this.trajetService = new TrajetService(dao);
        this.parametreRepository = new ParametreRepository(dao);
        this.distanceService = new DistanceService(dao);
        this.hotelRepository = new HotelRepository(dao);
    }

    public Reservation reserver(String idClient,
            Integer nombrePassager,
            LocalDateTime dateArrivee,
            Integer idHotel) throws Exception {

        Hotel hotel = new Hotel();
        hotel.setId(idHotel);

        Reservation reservation = new Reservation();
        reservation.setIdClient(idClient);
        reservation.setNombrePassager(nombrePassager);
        reservation.setDateArrivee(dateArrivee);
        reservation.setHotel(hotel);

        reservationRepository.save(reservation);

        return reservation;
    }

    public List<Reservation> getByDateArrivee(String dateStr) throws Exception {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return reservationRepository.getAll();
        }

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime startOfDay = date.atStartOfDay();

        return reservationRepository.getByDateArrivee(startOfDay);
    }

    public List<Reservation> getNonAssigne(LocalDate date) throws Exception {
        return reservationRepository.getNonAssigne(date);
    }

    public LocalDateTime getFirst(List<Reservation> reservations, LocalDateTime min) {
        LocalDateTime first = null;
        for (Reservation reservation : reservations) {
            if (first == null || first.isAfter(reservation.getDateArrivee()))
                if (min == null || min.isBefore(reservation.getDateArrivee()))
                    first = reservation.getDateArrivee();
        }
        return first;
    }

    public void assignation(LocalDate date) throws Exception {
        List<Reservation> reservations = reservationRepository.getByDateArrivee(date.atStartOfDay());

        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune réservation à traiter");
        }

        Double vitesse = parametreRepository.getVitesseMoyenne();
        LocalTime TA = parametreRepository.getTempsAttente();
        Map<String, Double> distances = distanceService.getDistanceMap();
        Hotel aeroport = hotelRepository.getAeroport();
        LocalDateTime min = getFirst(reservations, null);

        while (min != null) {

            LocalDateTime max = min.plusHours(TA.getHour())
                    .plusMinutes(TA.getMinute())
                    .plusSeconds(TA.getSecond());

            Iterator<Reservation> iterator = reservations.iterator();

            while (iterator.hasNext()) {
                Reservation reservation = iterator.next();

                if (!Utils.isBetween(reservation.getDateArrivee(), min, max))
                    continue;

                Trajet trajet = trajetService.assignerTrajet(reservation, min, max);

                if (trajet != null) {
                    iterator.remove();
                    trajetService.preparerTrajet(trajet, distances, aeroport, vitesse);
                }
            }

            min = getFirst(reservations, max);
        }
    }
}
