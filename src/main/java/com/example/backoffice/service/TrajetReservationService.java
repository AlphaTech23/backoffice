package com.example.backoffice.service;

import java.util.ArrayList;
import java.util.List;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;
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
                } 
            }
        }

        return trajetReservations;
    }

    public TrajetReservation creerTrajetReservation(Reservation reservation, Trajet trajet) throws Exception {
        TrajetReservation trajetReservation = new TrajetReservation();
        trajetReservation.setReservation(reservation);
        trajetReservation.setTrajet(trajet);
        trajetReservationRepository.save(trajetReservation);
        return trajetReservation;
    }
}
