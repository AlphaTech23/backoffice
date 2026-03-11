package com.example.backoffice.service;

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

    public TrajetReservation assigner(Trajet trajet, Reservation reservation) throws Exception {
        TrajetReservation trajetReservation = new TrajetReservation(null, trajet, reservation, 1);
        trajetReservationRepository.save(trajetReservation);
        return trajetReservation;
    }
}
