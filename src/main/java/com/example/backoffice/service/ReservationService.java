package com.example.backoffice.service;

import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    public List<List<Reservation>> getGroup(LocalDate date) throws Exception {
        List<Reservation> reservations = reservationRepository.getByDateArrivee(date.atStartOfDay());

        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune réservation à traiter");
        }

        LocalTime TA = parametreRepository.getTempsAttente();
        List<List<Reservation>> groups = new ArrayList<>();

        LocalDateTime windowStart = reservations.get(0).getDateArrivee();
        LocalDateTime windowEnd = windowStart.plusHours(TA.getHour())
                .plusMinutes(TA.getMinute())
                .plusSeconds(TA.getSecond());

        List<Reservation> currentGroup = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (!reservation.getDateArrivee().isAfter(windowEnd)) {
                currentGroup.add(reservation);
            } else {
                if (!currentGroup.isEmpty()) {
                    currentGroup.sort(Comparator.comparingInt(Reservation::getNombrePassager).reversed());
                    groups.add(currentGroup);
                }
                currentGroup = new ArrayList<>();
                currentGroup.add(reservation);
                windowStart = reservation.getDateArrivee();
                windowEnd = windowStart.plusHours(TA.getHour())
                        .plusMinutes(TA.getMinute())
                        .plusSeconds(TA.getSecond());
            }
        }

        if (!currentGroup.isEmpty()) {
            currentGroup.sort(Comparator.comparingInt(Reservation::getNombrePassager).reversed());
            groups.add(currentGroup);
        }

        return groups;
    }

    public void assignation(LocalDate date) throws Exception {
        trajetService.deleteByDate(date);

        List<List<Reservation>> groups = getGroup(date);
        double vitesse = parametreRepository.getVitesseMoyenne();
        Map<String, Double> distances = distanceService.getDistanceMap();
        Hotel aeroport = hotelRepository.getAeroport();

        for (List<Reservation> group : groups) {
            List<Trajet> trajets = new ArrayList<>();
            Map<Trajet, List<TrajetReservation>> trajetReservations = new HashMap<>();

            for (Reservation reservation : group) {
                TrajetReservation trAssigned = null;
                for (Trajet trajet : trajets) {
                    if (trajet.getPlacesRestantes() >= reservation.getNombrePassager()) {
                        trAssigned = trajetService.assigner(trajet, reservation);
                        trajetReservations.computeIfAbsent(trajet, k -> new ArrayList<>()).add(trAssigned);
                        break;
                    }
                }

                if (trAssigned == null) {
                    Trajet newTrajet = trajetService.creerTrajet(reservation);
                    if (newTrajet != null) {
                        trAssigned = trajetService.assigner(newTrajet, reservation);
                        trajetReservations.put(newTrajet, new ArrayList<>(List.of(trAssigned)));
                        trajets.add(newTrajet);
                    }
                }
            }

            for (Trajet trajet : trajets) {
                trajetService.preparerTrajet(trajet, trajetReservations.get(trajet), distances, aeroport, vitesse);
            }
        }
    }
}
