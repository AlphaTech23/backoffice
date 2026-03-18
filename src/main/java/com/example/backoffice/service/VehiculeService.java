package com.example.backoffice.service;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.TypeCarburant;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.VehiculeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;

    public VehiculeService(DAO dao) {
        this.vehiculeRepository = new VehiculeRepository(dao);
    }

    public void create(String reference, Integer capacite, Integer idTypeCarburant) throws Exception {
        TypeCarburant tc = new TypeCarburant();
        tc.setId(idTypeCarburant);
        Vehicule v = new Vehicule(null, reference, capacite, tc);
        vehiculeRepository.create(v);
    }

    public void update(Integer id, String reference, Integer capacite, Integer idTypeCarburant) throws Exception {
        TypeCarburant tc = new TypeCarburant();
        tc.setId(idTypeCarburant);
        Vehicule v = new Vehicule(id, reference, capacite, tc);
        vehiculeRepository.update(v);
    }

    public void delete(Integer id) throws Exception {
        vehiculeRepository.delete(id);
    }

    public List<Vehicule> getAll() throws Exception {
        return vehiculeRepository.getAll();
    }

    public Vehicule getDisponible(Reservation reservation, LocalDateTime windowEnd, Map<String, Integer> tripCount)
            throws Exception {
        List<Vehicule> vehicules = vehiculeRepository.getByCapacite(
                reservation.getNombrePassager(),
                windowEnd);
        
        if (vehicules == null || vehicules.isEmpty()) {
            return null;
        }

        Vehicule disponible = null;
        int min = Integer.MAX_VALUE;
        int minCount = Integer.MAX_VALUE;

        for (Vehicule v : vehicules) {
            int count = tripCount.getOrDefault(v.getReference(), 0);
            if (v.getCapacite() > min)
                break;
            if (min > v.getCapacite()) {
                min = v.getCapacite();
                minCount = count;
                disponible = v;
            } else if (min == v.getCapacite()) {
                if (minCount > count) {
                    minCount = count;
                    disponible = v;
                } else if (minCount == count
                        && "D".equalsIgnoreCase(v.getTypeCarburant().getCode())) {
                    disponible = v;
                }
            }
        }

        if (disponible != null)
            return disponible;
        Random random = new Random();
        return vehicules.get(random.nextInt(vehicules.size()));
    }
}
