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

import java.util.ArrayList;
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

    public void assignation() throws Exception {
        List<Reservation> reservations = reservationRepository.getAll();

        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune réservation à traiter");
        }
        
        Double vitesse = parametreRepository.getVitesseMoyenne();
        List<Distance> distances = distanceRepository.getAll();
        Hotel aeroport = hotelRepository.getAeroport();
        
        List<Trajet> trajets = new ArrayList<>();
        for (Reservation reservation : reservations) {
            Trajet trajet = trajetService.trouverTrajet(reservation);
            if(trajet != null) trajets.add(trajet);
        }

        for (Trajet t : trajets) {
            trajetService.ordonnerTrajet(t, distances, aeroport);
            
            double distance = trajetService.getDistance(t, distances, aeroport);
            LocalTime duree = trajetService.getDuree(distance, vitesse);
            t.setDistance(distance);

            LocalTime heureArrivee = t.getHeureDepart().plusHours(duree.getHour())
                    .plusMinutes(duree.getMinute());
            t.setHeureRetour(heureArrivee);
            trajetService.save(t);
        }
    }
}
