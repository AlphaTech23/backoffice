package com.example.backoffice.service;

import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.ReservationGroup;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    public List<ReservationGroup> getGroup(LocalDate date, LocalTime TA) throws Exception {
        List<Reservation> reservations = reservationRepository.getByDateArrivee(date.atStartOfDay());

        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune réservation à traiter");
        }

        List<ReservationGroup> groups = new ArrayList<>();

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
                    groups.add(new ReservationGroup(windowStart, windowEnd, currentGroup));
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
            groups.add(new ReservationGroup(windowStart, windowEnd, currentGroup));
        }

        return groups;
    }

    public TrajetReservation assign(Trajet trajet, Reservation reservation,
            Map<Trajet, List<TrajetReservation>> trajetReservations) throws Exception {

        TrajetReservation trAssigned = trajetService.assigner(trajet, reservation);
        trajetReservations.computeIfAbsent(trajet, k -> new ArrayList<>()).add(trAssigned);

        return trAssigned;
    }

    public void assignation(LocalDate date) throws Exception {
        trajetService.deleteByDate(date);

        Hotel aeroport = hotelRepository.getAeroport();
        LocalTime TA = parametreRepository.getTempsAttente();
        double vitesse = parametreRepository.getVitesseMoyenne();
        Map<String, Double> distances = distanceService.getDistanceMap();
        List<ReservationGroup> groups = getGroup(date, TA);

        List<Reservation> unassigned = new ArrayList<>();
        Map<String, Integer> tripCount = new HashMap<>();
        Map<String, LocalTime> tripEnd = new HashMap<>();

        for (ReservationGroup reservationGroup : groups) {
            LocalTime heureDepart = null;
            List<Trajet> trajets = new ArrayList<>();
            Map<Trajet, List<TrajetReservation>> trajetReservations = new HashMap<>();
            List<Reservation> group = new ArrayList<>(reservationGroup.getReservations());

            if (!unassigned.isEmpty()) {
                group.addAll(unassigned);
                unassigned.clear();
            }

            while (!group.isEmpty()) {
                Reservation pivot = group.remove(0);

                LocalTime heureArrivee = pivot.getDateArrivee().toLocalTime();
                if (heureDepart == null || heureDepart.isBefore(heureArrivee))
                    heureDepart = heureArrivee;

                Trajet trajet = trajetService.creerTrajet(
                        pivot,
                        reservationGroup.getWindowEnd(),
                        tripCount);

                if (trajet == null) {
                    unassigned.add(pivot);
                    continue;
                }

                LocalTime retourVoiture = tripEnd.get(trajet.getVehicule().getReference());
                if (retourVoiture != null && heureDepart.isBefore(retourVoiture))
                    heureDepart = retourVoiture;

                tripCount.merge(trajet.getVehicule().getReference(), 1, Integer::sum);

                assign(trajet, pivot, trajetReservations);

                Iterator<Reservation> iterator = group.iterator();

                while (iterator.hasNext()) {
                    Reservation r = iterator.next();

                    if (trajet.getPlacesRestantes() >= r.getNombrePassager()) {
                        heureArrivee = r.getDateArrivee().toLocalTime();
                        if (heureDepart.isBefore(heureArrivee))
                            heureDepart = heureArrivee;

                        assign(trajet, r, trajetReservations);
                        iterator.remove();
                    }
                }
                
                trajets.add(trajet);
            }
            for (Trajet trajet : trajets) {
                trajet.setHeureDepart(heureDepart);
                
                trajetService.preparerTrajet(
                        trajet,
                        trajetReservations.get(trajet),
                        distances,
                        aeroport,
                        vitesse);

                String ref = trajet.getVehicule().getReference();
                if (tripEnd.get(ref) == null || trajet.getHeureRetour().isAfter(tripEnd.get(ref))) {
                    tripEnd.put(ref, trajet.getHeureRetour());
                }
            }
        }
    }
}
