package com.example.backoffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.TrajetRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrajetService {

    private final TrajetRepository trajetRepository;
    private final VehiculeService vehiculeService;
    private final TrajetReservationService trajetReservationService;
    private final DistanceService distanceService;

    public TrajetService(DAO dao) {
        this.trajetRepository = new TrajetRepository(dao);
        this.vehiculeService = new VehiculeService(dao);
        this.trajetReservationService = new TrajetReservationService(dao);
        this.distanceService = new DistanceService(dao);
    }

    public List<Trajet> getByDate(LocalDate date) throws Exception {
        return trajetRepository.getByDate(date);
    }

    public double getDistance(Trajet trajet, Map<String, Double> distances, Hotel aeroport) throws Exception {

        List<TrajetReservation> trajetReservation = trajetReservationService.getByTrajet(trajet.getId(), false);

        if (trajetReservation == null || trajetReservation.isEmpty() || distances == null || distances.isEmpty()) {
            return 0.0;
        }

        double distance = 0.0;

        for (int i = 0; i < trajetReservation.size(); i++) {
            Hotel currentHotel = trajetReservation.get(i).getReservation().getHotel();

            if (i == 0) {
                distance += distanceService.getBetween(aeroport, currentHotel, distances);
            } else {
                Hotel previousHotel = trajetReservation.get(i - 1).getReservation().getHotel();
                distance += distanceService.getBetween(previousHotel, currentHotel, distances);
            }
        }

        Hotel lastHotel = trajetReservation.get(trajetReservation.size() - 1).getReservation().getHotel();
        distance += distanceService.getBetween(lastHotel, aeroport, distances);

        return distance;
    }

    public LocalTime getDuree(double distance, Double vitesse) throws Exception {

        if (vitesse <= 0) {
            throw new IllegalArgumentException("Vitesse moyenne invalide");
        }

        double dureeHeures = distance / vitesse;

        int heures = (int) dureeHeures;
        int minutes = (int) Math.round((dureeHeures - heures) * 60);

        return LocalTime.of(heures % 24, minutes % 60);
    }

    public Trajet creerTrajet(Reservation reservation) throws Exception {
        Vehicule vehicule = vehiculeService.getDisponible(reservation);

        if (vehicule == null) {
            return null;
        }

        Trajet trajet = new Trajet();
        trajet.setVehicule(vehicule);
        LocalDateTime dateArrivee = reservation.getDateArrivee();
        trajet.setDateTrajet(dateArrivee.toLocalDate());
        trajet.setHeureDepart(dateArrivee.toLocalTime());

        trajetRepository.save(trajet);

        return trajet;
    }

    public void ordonnerTrajet(Trajet trajet, Map<String, Double> distances, Hotel aeroport) throws Exception {

        List<TrajetReservation> reservations = trajetReservationService.getByTrajet(trajet.getId(), true);

        if (reservations == null || reservations.isEmpty()) {
            return;
        }

        List<Integer> nonTriees = new ArrayList<>();
        for (int i = 0; i < reservations.size(); i++) {
            nonTriees.add(i);
        }

        Hotel lieuActuel = aeroport;

        for (int tri = 1; tri <= reservations.size(); tri++) {

            double min = Double.MAX_VALUE;
            int indice = -1;

            for (Integer i : nonTriees) {

                double distance = distanceService.getBetween(
                        lieuActuel,
                        reservations.get(i).getReservation().getHotel(), distances);

                if (distance < min) {
                    min = distance;
                    indice = i;
                }
            }

            if (indice != -1) {

                nonTriees.remove(Integer.valueOf(indice));

                TrajetReservation tr = reservations.get(indice);
                tr.setOrdre(tri);
                trajetReservationService.save(tr);
                lieuActuel = tr.getReservation().getHotel();
            }
        }
    }

    public void save(Trajet trajet) throws Exception {
        trajetRepository.save(trajet);
    }

    public void preparerTrajet(Trajet trajet, Map<String, Double> distances, Hotel aeroport, Double vitesse)
            throws Exception {
        ordonnerTrajet(trajet, distances, aeroport);

        double distance = getDistance(trajet, distances, aeroport);
        LocalTime duree = getDuree(distance, vitesse);
        trajet.setDistance(distance);

        LocalTime heureArrivee = trajet.getHeureDepart().plusHours(duree.getHour())
                .plusMinutes(duree.getMinute());
        trajet.setHeureRetour(heureArrivee);
        save(trajet);
    }

    public Trajet assignerTrajet(Reservation reservation, LocalDateTime min, LocalDateTime max) throws Exception {
        try {

            Trajet dispo = trajetRepository.getByCapacite(
                    reservation.getNombrePassager(),
                    min, max);

            if (dispo == null) {
                Trajet create = creerTrajet(reservation);
                if (create != null)
                    trajetReservationService.assigner(create, reservation);
                return create;
            }

            LocalTime heureReservation = reservation
                    .getDateArrivee()
                    .toLocalTime();

            if (dispo.getHeureDepart().isBefore(heureReservation)) {
                dispo.setHeureDepart(heureReservation);
            }
            trajetReservationService.assigner(dispo, reservation);

            return dispo;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
