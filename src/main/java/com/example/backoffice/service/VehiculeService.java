package com.example.backoffice.service;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.TypeCarburant;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.VehiculeRepository;

import java.time.LocalDateTime;
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

    public Vehicule getVehiculeDisponible(Reservation reservation, LocalDateTime dateHeureFin) throws Exception {
        List<Vehicule> vehicules = vehiculeRepository.getByCapacite(reservation.getNombrePassager(),
                dateHeureFin);
        if (vehicules == null || vehicules.isEmpty()) {
            return null;
        }
        return getMeilleurVehicule(vehicules);
    }

    public Vehicule getVehiculeDisponible(LocalDateTime dateHeureFin) throws Exception {
        List<Vehicule> vehicules = vehiculeRepository.getVehiculeDisponible(dateHeureFin);
        if (vehicules == null || vehicules.isEmpty()) {
            return null;
        }
        return getMeilleurVehicule(vehicules);
    }

    private Vehicule getMeilleurVehicule(List<Vehicule> vehicules) throws Exception {
        Vehicule vehiculeDisponible = vehicules.get(0);
        for (Vehicule vehicule : vehicules) {
            if (vehicule.getCapacite() > vehiculeDisponible.getCapacite()) {
                break;
            } else {
                Integer nombreTrajetsDisponible = vehiculeRepository.getNombreTrajets(vehiculeDisponible.getId());
                Integer nombreTrajets = vehiculeRepository.getNombreTrajets(vehicule.getId());
                if (nombreTrajets < nombreTrajetsDisponible) {
                    vehiculeDisponible = vehicule;
                } else if (nombreTrajets == nombreTrajetsDisponible) {
                    if (vehicule.getTypeCarburant().getCode().equals("D")) {
                        if (vehicule.getTypeCarburant().getCode().equals("D")) {
                            Random random = new Random();
                            int index = random.nextInt(1);
                            if (index == 0)
                                vehiculeDisponible = vehicule;
                        }
                        vehiculeDisponible = vehicule;
                    }
                }
            }
        }
        return vehiculeDisponible;
    }
}
