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

    public void deleteByDate(LocalDate date) throws Exception {
        trajetRepository.deleteByDate(date);
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

    public Trajet creerTrajet(Reservation reservation, LocalDateTime windowEnd, Map<String, Integer> tripCount) throws Exception {
        Vehicule vehicule = vehiculeService.getDisponible(reservation, windowEnd, tripCount);

        if (vehicule == null) {
            return null;
        }

        Trajet trajet = new Trajet();
        trajet.setVehicule(vehicule);
        LocalDateTime dateArrivee = reservation.getDateArrivee();
        trajet.setDateTrajet(dateArrivee.toLocalDate());
        trajet.setHeureDepart(dateArrivee.toLocalTime());
        trajet.setPlacesRestantes(vehicule.getCapacite());

        trajetRepository.save(trajet);

        return trajet;
    }

    public Double ordonnerTrajet(List<TrajetReservation> trajetReservations, Map<String, Double> distances,
            Hotel aeroport)
            throws Exception {

        if (trajetReservations == null || trajetReservations.isEmpty()) {
            return 0.;
        }

        List<Integer> nonTriees = new ArrayList<>();
        for (int i = 0; i < trajetReservations.size(); i++) {
            nonTriees.add(i);
        }

        double totalDistance = 0.;
        Hotel lieuActuel = aeroport;

        for (int ordre = 1; ordre <= trajetReservations.size(); ordre++) {
            double minDistance = Double.MAX_VALUE;
            int indiceMin = -1;

            for (int idx : nonTriees) {
                double distance = distanceService.getBetween(lieuActuel,
                        trajetReservations.get(idx).getReservation().getHotel(),
                        distances);
                if (distance < minDistance) {
                    minDistance = distance;
                    indiceMin = idx;
                } else if (distance == minDistance) {
                    Hotel hotelCourant = trajetReservations.get(idx).getReservation().getHotel();
                    Hotel hotelMin = trajetReservations.get(indiceMin).getReservation().getHotel();

                    if (hotelCourant.getNom().compareToIgnoreCase(hotelMin.getNom()) < 0) {
                        indiceMin = idx;
                    }
                }
            }

            if (indiceMin != -1) {
                TrajetReservation tr = trajetReservations.get(indiceMin);
                tr.setOrdre(ordre);
                trajetReservationService.save(tr);

                totalDistance += minDistance;

                lieuActuel = tr.getReservation().getHotel();
                nonTriees.remove(Integer.valueOf(indiceMin));
            }
        }

        return totalDistance + distanceService.getBetween(aeroport, lieuActuel, distances);
    }

    public void preparerTrajet(Trajet trajet, List<TrajetReservation> trajetReservations, Map<String, Double> distances,
            Hotel aeroport, Double vitesse)
            throws Exception {
        double distance = ordonnerTrajet(trajetReservations, distances, aeroport);
        trajet.setDistance(distance);

        LocalTime duree = getDuree(distance, vitesse);
        LocalTime heureArrivee = trajet.getHeureDepart().plusHours(duree.getHour())
                .plusMinutes(duree.getMinute());
        trajet.setHeureRetour(heureArrivee);

        trajetRepository.save(trajet);
    }

    public TrajetReservation assigner(Trajet trajet, Reservation reservation) throws Exception {
        Integer placesRestantes = trajet.getPlacesRestantes(),
                nombrePassager = reservation.getNombrePassager();

        if (placesRestantes >= nombrePassager) {
            LocalTime heureReservation = reservation.getDateArrivee().toLocalTime();
            if (heureReservation.isAfter(trajet.getHeureDepart()))
                trajet.setHeureDepart(heureReservation);
            trajet.setPlacesRestantes(placesRestantes - nombrePassager);
            return new TrajetReservation(null, trajet, reservation, 1);
        }
        return null;
    }
}
