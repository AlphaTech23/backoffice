package com.example.backoffice.service;

import com.example.backoffice.repository.DistanceRepository;
import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Distance;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetService trajetService;
    private final ParametreRepository parametreRepository;
    private final DistanceRepository distanceRepository;
    private final HotelRepository hotelRepository;

    public ReservationService(DAO dao) {
        this.reservationRepository = new ReservationRepository(dao);
        this.trajetService = new TrajetService(dao);
        this.parametreRepository = new ParametreRepository(dao);
        this.distanceRepository = new DistanceRepository(dao);
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

    public List<Reservation> getByTrajet(Integer id) throws Exception {
        return reservationRepository.getByTrajet(id, false);
    }

    public void assigner(Reservation reservation, Double vitesse, List<Distance> distances, Hotel aeroport) throws Exception {
        Trajet trajet = trajetService.trouverTrajet(reservation, distances, aeroport);
        
        if (trajet != null) {
            try {
            double distance = trajetService.getDistance(trajet, distances, aeroport);
            LocalTime duree = trajetService.getDuree(distance, vitesse);
            trajet.setDistance(distance);

            LocalTime heureArrivee = trajet.getHeureDepart().plusHours(duree.getHour())
                    .plusMinutes(duree.getMinute());
            trajet.setHeureRetour(heureArrivee);

            trajetService.save(trajet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void assignation() throws Exception {
        List<Reservation> reservations = reservationRepository.getAll();
        double vitesse = parametreRepository.getVitesseMoyenne();
        List<Distance> distances = distanceRepository.getAll();
        Hotel aeroport = hotelRepository.getAeroport();
        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune réservation à traiter");
        }

        for (Reservation reservation : reservations) {
            if(reservation.getTrajet() != null) continue;
            assigner(reservation, vitesse, distances, aeroport);
        }
    }
}
