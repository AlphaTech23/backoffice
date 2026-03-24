package com.example.backoffice.service;

import java.time.LocalDate;

import com.example.backoffice.model.Hotel;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.repository.ParametreRepository;
import com.example.backoffice.repository.ReservationRepository;
import com.example.backoffice.repository.TrajetRepository;
import com.example.backoffice.repository.VehiculeRepository;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrajetService {

    private final TrajetRepository trajetRepository;
    private final HotelRepository hotelRepository;
    private final ParametreRepository parametreRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ReservationRepository reservationRepository;
    private final DistanceService distanceService;

    public TrajetService(DAO dao) {
        this.trajetRepository = new TrajetRepository(dao);
        this.hotelRepository = new HotelRepository(dao);
        this.parametreRepository = new ParametreRepository(dao);
        this.vehiculeRepository = new VehiculeRepository(dao);
        this.reservationRepository = new ReservationRepository(dao);
        this.distanceService = new DistanceService(dao);
    }

    public void save(Trajet trajet) throws Exception {
        trajetRepository.save(trajet);
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

    public LocalTime getHeureDepart(List<Reservation> assignees, List<Trajet> trajets) throws Exception {
        if (assignees == null || assignees.isEmpty()) {
            return null;
        }

        assignees.sort(Comparator.comparing(Reservation::getDateArrivee).reversed());

        LocalTime heureDepart = assignees.get(0).getDateArrivee().toLocalTime();
        for (Trajet trajet : trajets) {
            LocalTime heureRetour = vehiculeRepository.getHeureRetour(trajet.getVehicule().getId());

            if (heureRetour != null && !heureRetour.isBefore(heureDepart)) {
                heureDepart = heureRetour;
            }

            LocalTime heureDisponible = trajet.getVehicule().getHeureDisponible();

            if (heureDisponible != null && heureDepart.isBefore(heureDisponible)) {
                heureDepart = heureDisponible;
            }
        }
        return heureDepart;
    }

    public Double calculerDistance(Trajet trajet) throws Exception {
        List<Reservation> assignees = reservationRepository.getAssignees(trajet.getId());
        Double distanceTotale = 0.0;
        Hotel aeroport = hotelRepository.getAeroport();
        Hotel lieuActuel = aeroport;
        Map<String, Double> distanceMap = distanceService.getDistanceMap();
        List<Hotel> nonParcourues = assignees.stream()
                .map(Reservation::getHotel)
                .distinct()
                .collect(Collectors.toList());
        while (!nonParcourues.isEmpty()) {
            Hotel plusProche = null;
            double distanceMin = Double.MAX_VALUE;
            for (Hotel hotel : nonParcourues) {
                double distance = distanceService.getBetween(lieuActuel, hotel, distanceMap);
                if (distance < distanceMin
                        || (distance == distanceMin && hotel.getNom().compareTo(plusProche.getNom()) < 0)) {
                    distanceMin = distance;
                    plusProche = hotel;
                }
            }
            distanceTotale += distanceMin;
            lieuActuel = plusProche;
            nonParcourues.remove(plusProche);
        }
        double distance = distanceService.getBetween(aeroport, lieuActuel, distanceMap);
        return distanceTotale + distance;
    }

    public void calculerItineraire(Trajet trajet) throws Exception {
        Double distance = calculerDistance(trajet);
        Double vitesseMoyenne = parametreRepository.getVitesseMoyenne();
        LocalTime duree = getDuree(distance.doubleValue(), vitesseMoyenne);
        LocalTime heureRetour = trajet.getHeureDepart().plusHours(duree.getHour())
                .plusMinutes(duree.getMinute())
                .plusSeconds(duree.getSecond());
        trajet.setDistance(distance);
        trajet.setHeureRetour(heureRetour);
    }

    public void preparerTrajet(List<Reservation> assignees, List<Trajet> trajets) throws Exception {
        LocalTime heureDepart = getHeureDepart(assignees, trajets);
        for (Trajet trajet : trajets) {
            trajet.setHeureDepart(heureDepart);
            calculerItineraire(trajet);
            save(trajet);
        }
    }

    public Trajet creerTrajet(Reservation reservation, Vehicule vehicule) throws Exception {
        Trajet trajet = new Trajet();
        trajet.setDateTrajet(reservation.getDateArrivee().toLocalDate());
        trajet.setVehicule(vehicule);
        save(trajet);
        return trajet;
    }

}
