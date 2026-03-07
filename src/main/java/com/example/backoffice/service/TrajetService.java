package com.example.backoffice.service;

import com.example.backoffice.TrajetRepository;

import java.time.LocalDate;
import java.util.List;

public class TrajetService {

    private TrajetRepository trajeRepository;

    public TrajetService() {
        this.trajetRepository = new TrajetRepository();
    }

    public List<Trajet> getByDate(LocalDate date) throws Exception {
        return trajetRepository.getByDate(date);
    }
}
