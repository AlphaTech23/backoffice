package com.example.backoffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.TrajetRepository;
import com.example.backoffice.repository.DistanceRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.repository.ParametreRepository;

import java.time.LocalTime;
import java.util.List;

public class TrajetService {

    private ReservationRepository reservationRepository;
    private DistanceRepository distanceRepository;
    private HotelRepository hotelRepository;
    private ParametreRepository parametreRepository;
    private TrajetRepository trajetRepository;
    private VehiculeService vehiculeService;

    public TrajetService() {
        this.reservationRepository = new ReservationRepository();
        this.distanceRepository = new DistanceRepository();
        this.hotelRepository = new HotelRepository();
        this.parametreRepository = new ParametreRepository();
        this.trajetRepository = new TrajetRepository();
        this.vehiculeService = new VehiculeService();
    }

    public List<Trajet> getByDate(LocalDate date) throws Exception {
        return trajetRepository.getByDate(date);
    }

    public double getDistance(Trajet trajet) throws Exception {

        List<Reservation> reservations = reservationRepository.getByTrajet(trajet.getId(), false);

        if (reservations == null || reservations.isEmpty()) {
            return 0.0; // pas de réservation → distance nulle
        }

        double distance = 0.0;
        Hotel aeroport = hotelRepository.getAeroport();

        for (int i = 0; i < reservations.size(); i++) {
            Hotel currentHotel = reservations.get(i).getHotel();

            if (i == 0) {
                // première réservation → distance aéroport → hôtel
                distance += distanceRepository.getBetween(aeroport, currentHotel);
            } else {
                // distance entre hôtel précédent et hôtel courant
                Hotel previousHotel = reservations.get(i - 1).getHotel();
                distance += distanceRepository.getBetween(previousHotel, currentHotel);
            }
        }

        // Optionnel : retour à l'aéroport après la dernière réservation
        Hotel lastHotel = reservations.get(reservations.size() - 1).getHotel();
        distance += distanceRepository.getBetween(lastHotel, aeroport);

        return distance;
    }

    public LocalTime getDuree(double distance) throws Exception {

        // vitesse moyenne en km/h
        double vitesse = parametreRepository.getVitesseMoyenne();

        if (vitesse <= 0) {
            throw new IllegalArgumentException("Vitesse moyenne invalide");
        }

        // durée en heures
        double dureeHeures = distance / vitesse;

        // convertir en heures et minutes
        int heures = (int) dureeHeures;
        int minutes = (int) Math.round((dureeHeures - heures) * 60);

        return LocalTime.of(heures % 24, minutes % 60);
    }

    public Trajet creerTrajet(Reservation reservation) throws Exception {
        // récupérer un véhicule disponible
        Vehicule vehicule = vehiculeService.getDisponible(reservation);

        if (vehicule == null) {
            // aucun véhicule disponible pour cette réservation
            return null;
        }

        // créer le trajet et assigner le véhicule
        Trajet trajet = new Trajet();
        trajet.setVehicule(vehicule);
        LocalDateTime dateArriveee = reservation.getDateArrivee();
        trajet.setDateTrajet(dateArriveee.toLocalDate());
        trajet.setHeureDepart(dateArriveee.toLocalTime());

        trajetRepository.save(trajet);

        // assigner ordre = 1 à la réservation
        reservation.setOrdre(1);
        reservation.setTrajet(trajet);

        // sauvegarder la réservation avec le trajet associé
        reservationRepository.save(reservation);

        return trajet;
    }
}
