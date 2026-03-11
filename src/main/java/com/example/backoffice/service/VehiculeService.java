package com.example.backoffice.service;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.TypeCarburant;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.VehiculeRepository;

import java.util.List;
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

    public Vehicule getDisponible(Reservation reservation) throws Exception {
        List<Vehicule> vehicules = vehiculeRepository.getByCapacite(
            reservation.getNombrePassager(),
            reservation.getDateArrivee()
        );
        if (vehicules == null || vehicules.isEmpty()) {
            return null;
        }
        
        Vehicule disponible = null;
        int min = Integer.MAX_VALUE;

        for (Vehicule v : vehicules) {
            if(v.getCapacite() > min) break;
            if(min > v.getCapacite()) {
                min = v.getCapacite();
                disponible = v;
            } else if (min == v.getCapacite() && "D".equalsIgnoreCase(v.getTypeCarburant().getCode())) {
                disponible = v;
            }
        }

        if(disponible != null) return disponible;
        Random random = new Random();
        return vehicules.get(random.nextInt(vehicules.size()));
    }
}
