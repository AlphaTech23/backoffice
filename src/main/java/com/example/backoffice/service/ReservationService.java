package com.example.backoffice.service;

import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.repository.TrajetRepository;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationService {

    private ReservationRepository reservationRepository;
    private TrajetRepository trajetRepository;
    private TrajetService trajetService;

    public ReservationService() {
        this.reservationRepository = new ReservationRepository();
        this.trajetRepository = new TrajetRepository();
        this.trajetService = new TrajetService();
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

    public List<Reservation> getNonAssigne(LocalDate date) throws Exception {
        return reservationRepository.getNonAssigne(date);
    }

    public void assigner(Reservation reservation) throws Exception {

        // créer ou récupérer un trajet pour cette réservation
        Trajet trajet = trajetService.creerTrajet(reservation);

        // calculer l'heure d'arrivée en ajoutant la durée estimée
        LocalTime heureArrivee = trajet.getHeureDepart().plusHours(trajetService.getDuree(trajet).getHour())
                .plusMinutes(trajetService.getDuree(trajet).getMinute());
        trajet.setHeureRetour(heureArrivee);

        // sauvegarder le trajet
        trajetRepository.save(trajet);
    }

    
    public List<Reservation> getByTrajet(int idTrajet) throws Exception {
        return reservationRepository.getByTrajet(idTrajet, false);
    }

    public void assignation() throws Exception {

        // récupérer toutes les réservations non encore assignées
        List<Reservation> reservations = reservationRepository.getAll();

        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Aucune réservation à traiter");
        }

        // parcourir chaque réservation et l'assigner
        for (Reservation reservation : reservations) {
            if(reservation.getTrajet() != null) continue;
            assigner(reservation);
                
        }
    }
}
