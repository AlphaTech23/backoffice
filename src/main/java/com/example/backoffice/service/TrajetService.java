package com.example.backoffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Distance;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.TrajetRepository;
import com.example.backoffice.repository.ReservationRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetService {

    private ReservationRepository reservationRepository;
    private TrajetRepository trajetRepository;
    private VehiculeService vehiculeService;

    public TrajetService(DAO dao) {
        this.reservationRepository = new ReservationRepository(dao);
        this.trajetRepository = new TrajetRepository(dao);
        this.vehiculeService = new VehiculeService(dao);
    }

    public List<Trajet> getByDate(LocalDate date) throws Exception {
        return trajetRepository.getByDate(date);
    }

    public Double getBetween(Hotel h1, Hotel h2, List<Distance> distances) {
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

        List<Reservation> reservations = reservationRepository.getByTrajet(trajet.getId(), false);

        if (reservations == null || reservations.isEmpty() || distances == null || distances.isEmpty()) {
            return 0.0;
        }

        double distance = 0.0;

        for (int i = 0; i < reservations.size(); i++) {
            Hotel currentHotel = reservations.get(i).getHotel();

            if (i == 0) {
                distance += getBetween(aeroport, currentHotel, distances);
            } else {
                Hotel previousHotel = reservations.get(i - 1).getHotel();
                distance += getBetween(previousHotel, currentHotel, distances);
            }
        }

        Hotel lastHotel = reservations.get(reservations.size() - 1).getHotel();
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

        reservation.setOrdre(1);

        return trajet;
    }

    public Trajet getDisponible(Reservation reservation) throws Exception {

        List<Trajet> trajets = trajetRepository.getByCapacite(
                reservation.getNombrePassager(),
                reservation.getDateArrivee());

        Trajet disponible = null;
        int min = Integer.MAX_VALUE;

        for (Trajet trajet : trajets) {
            if (min < trajet.getPlacesRestantes())
                break;
            if (min > trajet.getPlacesRestantes()) {
                disponible = trajet;
                min = trajet.getPlacesRestantes();
            } else if (min == trajet.getPlacesRestantes()
                    && trajet.getVehicule().getTypeCarburant().getCode().equals("D")) {
                disponible = trajet;
            }
        }

        if (trajets != null && !trajets.isEmpty() && disponible == null) {
            int random = (int) (Math.random() * trajets.size());
            disponible = trajets.get(random);
        }

        return disponible;
    }

    public void ordonnerTrajet(Trajet trajet, List<Distance> distances, Hotel aeroport) throws Exception {

        List<Reservation> reservations = reservationRepository.getByTrajet(trajet.getId(), true);

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
                        reservations.get(i).getHotel(), distances);

                if (distance < min) {
                    min = distance;
                    indice = i;
                }
            }

            if (indice != -1) {

                nonTriees.remove(Integer.valueOf(indice));

                Reservation r = reservations.get(indice);
                r.setOrdre(tri);

                lieuActuel = r.getHotel();
            }
        }

        reservationRepository.updateOrdre(reservations);
    }

    public void save(Trajet trajet) throws Exception {
        trajetRepository.save(trajet);
    }

    public Trajet trouverTrajet(Reservation reservation, List<Distance> distances, Hotel aeroport) throws Exception {
        try {
            Trajet dispo = getDisponible(reservation);
            Trajet create = creerTrajet(reservation);
            if (dispo == null || dispo.getPlacesRestantes() > create.getVehicule().getCapacite()) {
                reservation.setTrajet(dispo);
                reservationRepository.save(reservation);
                return create;
            }
            reservation.setTrajet(dispo);
            reservationRepository.save(reservation);
            ordonnerTrajet(dispo, distances, aeroport);

            return dispo;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
