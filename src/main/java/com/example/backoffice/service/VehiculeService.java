package com.example.backoffice.service;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;
import com.example.backoffice.model.TypeCarburant;
import com.example.backoffice.model.Vehicule;
import com.example.backoffice.repository.VehiculeRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public Vehicule getRetourVehicule(List<Vehicule> vehicules, int nombrePassager) throws Exception {
        Vehicule vehiculeResultat = null;

        // Filtrer les véhicules capables de prendre tous les passagers
        List<Vehicule> fullFit = new ArrayList<>();
        for (Vehicule v : vehicules) {
            if (v.getCapaciteRestante() >= nombrePassager) {
                fullFit.add(v);
            }
        }

        // Trier par capaciteRestante croissante
        fullFit.sort(Comparator.comparingInt(Vehicule::getCapaciteRestante));

        if (!fullFit.isEmpty()) {
            vehiculeResultat = getMeilleurVehicule(fullFit);
        } else {
            // Filtrer les véhicules partiellement adaptés
            List<Vehicule> partialFit = new ArrayList<>();
            for (Vehicule v : vehicules) {
                if (v.getCapaciteRestante() < nombrePassager && v.getCapaciteRestante() > 0) {
                    partialFit.add(v);
                }
            }

            // Trier par capaciteRestante décroissante
            partialFit.sort(Comparator.comparingInt(Vehicule::getCapaciteRestante).reversed());

            if (!partialFit.isEmpty()) {
                vehiculeResultat = getMeilleurVehicule(partialFit);
            }
        }

        return vehiculeResultat;
    }

    public List<Vehicule> getPremiersVehicules(LocalDateTime dateHeureFin, LocalDateTime dateHeureProchain)
            throws Exception {
        List<Vehicule> vehicules = vehiculeRepository.getPremiersVehicules(dateHeureFin, dateHeureProchain);

        if (vehicules == null || vehicules.isEmpty()) {
            return null;
        }

        List<Vehicule> premiersVehicules = new ArrayList<>();

        LocalTime minRetour = vehiculeRepository.getHeureRetour(vehicules.get(0));

        for (int i = 0; i < vehicules.size(); i++) {
            LocalTime heureRetour = vehiculeRepository.getHeureRetour(vehicules.get(i));
            if (minRetour.equals(heureRetour)) {
                premiersVehicules.add(vehicules.get(i));
            } else {
                break;
            }
        }

        return premiersVehicules;
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
