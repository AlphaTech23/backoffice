package com.example.backoffice.controller;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.service.TypeCarburantService;
import com.example.backoffice.service.VehiculeService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.PostMapping;
import com.example.framework.core.ModelView;

@Controller
public class VehiculeController {

    private final VehiculeService vehiculeService;
    private final TypeCarburantService typeCarburantService;
    private final DAO dao;
    public VehiculeController() {
        dao = new DAO();
        this.vehiculeService = new VehiculeService(dao);
        this.typeCarburantService = new TypeCarburantService(dao);
    }

    @GetMapping("/vehicules")
    public ModelView list() throws Exception {
        ModelView mv = new ModelView("/vehicule/list.jsp");

        try {
            dao.connect();
            mv.addAttribute("vehicules", vehiculeService.getAll());
            mv.addAttribute("typeCarburants", typeCarburantService.getAll());
        } catch (Exception e) {
            mv.addAttribute("error", "Erreur lors du chargement des données : " + e.getMessage());
        } finally {
            dao.close();
        }
        return mv;
    }

    @PostMapping("/vehicules")
    public ModelView actionVehicule(String action, Integer id, String reference, Integer capacite, Integer idTypeCarburant)
        throws Exception {
        ModelView mv = new ModelView("/vehicule/list.jsp");

        try {
            dao.connect();
            if ("create".equals(action)) {
                vehiculeService.create(reference, capacite, idTypeCarburant);
                mv.addAttribute("message", "Véhicule ajouté avec succès");
            } else if ("update".equals(action)) {
                vehiculeService.update(id, reference, capacite, idTypeCarburant);
                mv.addAttribute("message", "Véhicule mis à jour avec succès");
            } else if ("delete".equals(action)) {
                vehiculeService.delete(id);
                mv.addAttribute("message", "Véhicule supprimé avec succès");
            }
            mv.addAttribute("vehicules", vehiculeService.getAll());
            mv.addAttribute("typeCarburants", typeCarburantService.getAll());
        } catch (Exception e) {
            mv.addAttribute("error", "Erreur : " + e.getMessage());
        } finally {
            dao.close();
        }

        return mv;
    }
}
