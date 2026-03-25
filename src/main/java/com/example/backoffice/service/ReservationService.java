package com.example.backoffice.service;

import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.util.Utils;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.Vehicule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationService {

    private static final Comparator<Reservation> RESERVATION_COMPARATOR = Comparator
            .comparingInt(Reservation::getNombrePassager)
            .reversed()
            .thenComparing(Reservation::getDateArrivee);

    private final ReservationRepository reservationRepository;
    private final ParametreRepository parametreRepository;
    private final VehiculeService vehiculeService;
    private final TrajetService trajetService;
    private final TrajetReservationService trajetReservationService;

    public ReservationService(DAO dao) {
        this.reservationRepository = new ReservationRepository(dao);
        this.parametreRepository = new ParametreRepository(dao);
        this.vehiculeService = new VehiculeService(dao);
        this.trajetService = new TrajetService(dao);
        this.trajetReservationService = new TrajetReservationService(dao);
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

    List<List<Reservation>> getGroupesReservations(LocalDate date) throws Exception {
        List<Reservation> reservations = reservationRepository.getByDateArrivee(date.atStartOfDay());
        LocalTime tempsAttente = parametreRepository.getTempsAttente();
        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune reservation a traiter");
        }
        LocalDateTime dateHeureDebut = reservations.get(0).getDateArrivee();
        LocalDateTime dateHeureFin = dateHeureDebut.plusHours(tempsAttente.getHour())
                .plusMinutes(tempsAttente.getMinute())
                .plusSeconds(tempsAttente.getSecond());

        List<List<Reservation>> groupes = new ArrayList<>();
        List<Reservation> groupeReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (Utils.isBetween(reservation.getDateArrivee(),
                    dateHeureDebut, dateHeureFin)) {
                groupeReservations.add(reservation);
            } else {
                dateHeureDebut = reservation.getDateArrivee();
                dateHeureFin = dateHeureDebut.plusHours(tempsAttente.getHour())
                        .plusMinutes(tempsAttente.getMinute())
                        .plusSeconds(tempsAttente.getSecond());
                groupes.add(groupeReservations);
                groupeReservations = new ArrayList<>();
                groupeReservations.add(reservation);
            }
        }
        if (!groupeReservations.isEmpty()) {
            groupes.add(groupeReservations);
        }

        return groupes;
    }

    public Trajet traiterReservation(Reservation reservation,
            List<Reservation> assignees,
            List<Reservation> groupeReservations,
            List<Reservation> groupeReservationsSuivantes,
            LocalDateTime heureFin) throws Exception {
        if (!assignees.contains(reservation)) {
            Vehicule vehiculeDisponible = vehiculeService.getVehiculeDisponible(reservation, heureFin);
            if (vehiculeDisponible != null) {
                Trajet trajet = trajetService.creerTrajet(reservation, vehiculeDisponible);
                assigner(reservation, vehiculeDisponible, assignees, trajet);

                trajetReservationService.remplirVehicule(groupeReservations, assignees, trajet);

                return trajet;
            }
        }
        return null;
    }

    public void assigner(Reservation reservation, Vehicule vehicule,
            List<Reservation> assignees, Trajet trajet) throws Exception {

        trajetReservationService.creerTrajetReservation(reservation, trajet);
        assignees.add(reservation);

        trajet.getVehicule().diminuerCapaciteRestante(reservation.getNombrePassager());

    }

    public void traiterGroupeReservation(LocalDate date, List<Reservation> groupeReservations,
            List<Reservation> groupeReservationsSuivantes) throws Exception {

        LocalTime tempsAttente = parametreRepository.getTempsAttente();
        LocalDateTime heureDebut = groupeReservations.get(0).getDateArrivee();
        LocalDateTime heureFin = heureDebut.plusHours(tempsAttente.getHour())
                .plusMinutes(tempsAttente.getMinute())
                .plusSeconds(tempsAttente.getSecond());

        groupeReservations.sort(RESERVATION_COMPARATOR);

        List<Reservation> assignees = new ArrayList<>();
        List<Trajet> trajets = new ArrayList<>();
        for (int i = 0; i < groupeReservations.size(); i++) {
            int size = groupeReservations.size();
            Reservation reservation = groupeReservations.get(i);
            Trajet trajet = traiterReservation(reservation, assignees, groupeReservations,
                    groupeReservationsSuivantes, heureFin);
            if (size != groupeReservations.size()) {
                i = -1;
            }
            if (trajet != null)
                trajets.add(trajet);
        }
        trajetService.preparerTrajet(assignees, trajets);
    }

    public void assignation(LocalDate date) throws Exception {
        trajetService.deleteByDate(date);
        List<List<Reservation>> groupes = getGroupesReservations(date);
        for (int i = 0; i < groupes.size(); i++) {
            if (i + 1 < groupes.size())
                traiterGroupeReservation(date, groupes.get(i), groupes.get(i + 1));
            else
                traiterGroupeReservation(date, groupes.get(i), null);
        }
    }
}
