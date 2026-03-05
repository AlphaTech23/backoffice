package com.example.backoffice.controller;

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

    public VehiculeController() {
        this.vehiculeService = new VehiculeService();
        this.typeCarburantService = new TypeCarburantService();
    }

    @GetMapping("/vehicules")
    public ModelView list() {
        ModelView mv = new ModelView("/vehicule/list.jsp");

        try {
            mv.addAttribute("vehicules", vehiculeService.getAll());
            mv.addAttribute("typeCarburants", typeCarburantService.getAll());
        } catch (Exception e) {
            mv.addAttribute("error", "Erreur lors du chargement des données : " + e.getMessage());
        }

        return mv;
    }

    @PostMapping("/vehicule")
    public ModelView actionVehicule(String action, Integer id, String reference, Integer capacite, Integer idTypeCarburant) {
        ModelView mv = new ModelView("/vehicule/list.jsp");

        try {
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
        } catch (Exception e) {
            mv.addAttribute("error", "Erreur (" + action + ") : " + e.getMessage());
        }

        // Recharger les données pour l'affichage de la page
        try {
            mv.addAttribute("vehicules", vehiculeService.getAll());
            mv.addAttribute("typeCarburants", typeCarburantService.getAll());
        } catch (Exception e) {
            mv.addAttribute("error", "Erreur lors du rechargement des données : " + e.getMessage());
        }

        return mv;
    }
}
