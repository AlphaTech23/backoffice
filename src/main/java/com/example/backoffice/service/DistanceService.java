package com.example.backoffice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Distance;
import com.example.backoffice.model.Hotel;
import com.example.backoffice.repository.DistanceRepository;

public class DistanceService {
    private final DistanceRepository distanceRepository;

    public DistanceService(DAO dao) {
        this.distanceRepository = new DistanceRepository(dao);
    }

    public Map<String, Double> getDistanceMap() throws Exception {
        List<Distance> distances = distanceRepository.getAll();
        Map<String, Double> distanceMap = new HashMap<>();
        for (Distance d : distances) {
           distanceMap.put(d.getFromHotel().getCode() + "-" + d.getToHotel().getCode(), d.getKilometre());
           distanceMap.put(d.getToHotel().getCode() + "-" + d.getFromHotel().getCode(), d.getKilometre());
        }
        return distanceMap;
    }

    public Double getBetween(Hotel h1, Hotel h2, Map<String, Double> distances) {
        Double distance = distances.get(h1.getCode()+"-"+h2.getCode());
        return distance != null ? distance : 0.;
    }
}
