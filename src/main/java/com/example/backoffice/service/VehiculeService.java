package com.example.backoffice.service;

import com.example.backoffice.model.TypeCarburant;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.VehiculeRepository;

import java.util.List;

public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;

    public VehiculeService() {
        this.vehiculeRepository = new VehiculeRepository();
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
}
