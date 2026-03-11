package com.example.backoffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Distance;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.TrajetRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetService {

    private TrajetRepository trajetRepository;
    private VehiculeService vehiculeService;
    private TrajetReservationService trajetReservationService;

    public TrajetService(DAO dao) {
        this.trajetRepository = new TrajetRepository(dao);
        this.vehiculeService = new VehiculeService(dao);
        this.trajetReservationService = new TrajetReservationService(dao);
    }

    public List<Trajet> getByDate(LocalDate date) throws Exception {
        return trajetRepository.getByDate(date);
    }

    public Double getBetween(Hotel h1, Hotel h2, List<Distance> distances) {
        if (h1.getCode().equals(h2.getCode()))
            return 0.;

        for (Distance d : distances) {
            String from = d.getFromHotel().getCode(),
                    to = d.getToHotel().getCode();
            if ((from.equalsIgnoreCase(h1.getCode())
                    && to.equalsIgnoreCase(h2.getCode())) ||
                    (from.equalsIgnoreCase(h2.getCode())
                            && to.equalsIgnoreCase(h1.getCode()))) {
                return d.getKilometre();
            }
        }
        return 0.;
    }

    public double getDistance(Trajet trajet, List<Distance> distances, Hotel aeroport) throws Exception {

        List<TrajetReservation> trajetReservation = trajetReservationService.getByTrajet(trajet.getId(), false);

        if (trajetReservation == null || trajetReservation.isEmpty() || distances == null || distances.isEmpty()) {
            return 0.0;
        }

        double distance = 0.0;

        for (int i = 0; i < trajetReservation.size(); i++) {
            Hotel currentHotel = trajetReservation.get(i).getReservation().getHotel();

            if (i == 0) {
                distance += getBetween(aeroport, currentHotel, distances);
            } else {
                Hotel previousHotel = trajetReservation.get(i - 1).getReservation().getHotel();
                distance += getBetween(previousHotel, currentHotel, distances);
            }
        }

        Hotel lastHotel = trajetReservation.get(trajetReservation.size() - 1).getReservation().getHotel();
        distance += getBetween(lastHotel, aeroport, distances);

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

    public void ordonnerTrajet(Trajet trajet, List<Distance> distances, Hotel aeroport) throws Exception {

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

                double distance = getBetween(
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

    public Trajet trouverTrajet(Reservation reservation) throws Exception {
        try {
            Trajet dispo = trajetRepository.getByCapacite(
                reservation.getNombrePassager(),
                reservation.getDateArrivee());
            if (dispo == null) {
                Trajet create = creerTrajet(reservation);
                if(create != null)  trajetReservationService.assigner(create, reservation);
                return create;
            } 
            if (dispo != null) {
                trajetReservationService.assigner(dispo, reservation);
            }
            return dispo;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
