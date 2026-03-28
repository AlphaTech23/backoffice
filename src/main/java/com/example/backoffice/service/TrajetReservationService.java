package com.example.backoffice.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.TrajetReservationRepository;

public class TrajetReservationService {
    private final TrajetReservationRepository trajetReservationRepository;

    public TrajetReservationService(DAO dao) {
        trajetReservationRepository = new TrajetReservationRepository(dao);
    }

    public List<TrajetReservation> getByTrajet(Integer id, boolean alphabetique) throws Exception {
        return trajetReservationRepository.getByTrajet(id, alphabetique);
    }

    public void save(TrajetReservation trajet) throws Exception {
        trajetReservationRepository.save(trajet);
    }

    public List<TrajetReservation> remplirVehicule(List<Reservation> groupeReservations,
            List<Reservation> assignees,
            Trajet trajet) throws Exception {
        List<TrajetReservation> trajetReservations = new ArrayList<>();

        for (int i = 0; i < groupeReservations.size(); i++) {

            if (trajet.getVehicule().getCapaciteRestante() <= 0)
                break;

            Reservation reservation = groupeReservations.get(i);
            if (!assignees.contains(reservation)) {
                if (trajet.getVehicule().getCapaciteRestante() >= reservation.getNombrePassager()) {
                    TrajetReservation trajetReservation = creerTrajetReservation(reservation, trajet);
                    trajetReservations.add(trajetReservation);
                    trajet.getVehicule().diminuerCapaciteRestante(reservation.getNombrePassager());
                    assignees.add(reservation);
                } else {
                    // sprint-7
                    Reservation reservationProche = getPlusProche(trajet.getVehicule(), groupeReservations, assignees);
                    if (reservationProche != null) {
                        if (reservationProche.getNombrePassager() > trajet.getVehicule().getCapaciteRestante()) {
                            Reservation reservationRestante = diviserReservation(reservationProche,
                                    trajet.getVehicule().getCapaciteRestante());

                            if (reservationRestante.getNombrePassager() > 0) {
                                groupeReservations.add(0, reservationRestante);
                                groupeReservations.sort(
                                        Comparator.comparingInt(Reservation::getNombrePassager)
                                                .reversed()
                                                .thenComparing(Reservation::getDateArrivee));
                                i = 0;
                            }
                        }
                        
                        TrajetReservation trajetReservation = creerTrajetReservation(reservationProche, trajet);
                        trajetReservations.add(trajetReservation);

                        trajet.getVehicule().diminuerCapaciteRestante(reservationProche.getNombrePassager());
                        assignees.add(reservationProche);
                    }
                }
            }
        }

        return trajetReservations;
    }

    public Reservation diviserReservation(Reservation reservationProche, Integer capacite) {

        Integer reste = reservationProche.getNombrePassager() - capacite;
        Reservation reservationRestante = new Reservation(
                reservationProche.getId(),
                reservationProche.getIdClient(),
                reste,
                reservationProche.getDateArrivee(),
                reservationProche.getHotel());

        reservationProche.setNombrePassager(capacite);

        return reservationRestante;
    }

    private Reservation getPlusProche(Vehicule vehicule, List<Reservation> reservations, List<Reservation> assignees) {
        int index = -1;
        for (int i = 0; i < reservations.size(); i++) {
            if (!assignees.contains(reservations.get(i))) {
                if (index == -1)
                    index = i;
                int min = Math.abs(vehicule.getCapaciteRestante() - reservations.get(index).getNombrePassager());
                if (Math.abs(vehicule.getCapaciteRestante() - reservations.get(i).getNombrePassager()) < min) {
                    index = i;
                }
                if (Math.abs(vehicule.getCapaciteRestante() - reservations.get(i).getNombrePassager()) == min
                        && reservations.get(i).getNombrePassager() > reservations.get(index).getNombrePassager()) {
                    index = i;
                }
            }
        }
        if (index == -1)
            return null;
        return reservations.get(index);
    }

    public TrajetReservation creerTrajetReservation(Reservation reservation, Trajet trajet) throws Exception {
        TrajetReservation trajetReservation = new TrajetReservation();
        trajetReservation.setReservation(reservation);
        trajetReservation.setTrajet(trajet);
        trajetReservation.setNombrePassager(reservation.getNombrePassager());
        trajetReservationRepository.save(trajetReservation);
        return trajetReservation;
    }

    public void assigner(Reservation reservation, Vehicule vehicule,
            List<Reservation> assignees, Trajet trajet) throws Exception {

        creerTrajetReservation(reservation, trajet);
        assignees.add(reservation);

        trajet.getVehicule().diminuerCapaciteRestante(reservation.getNombrePassager());

    }

}
