package com.example.backoffice.controller;

import com.example.backoffice.service.ReservationService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.core.ModelView;

@Controller
public class TrajetController {

    private ReservationService reservationService;

    public TrajetController() {
        this.reservationService = new ReservationService();
    }
    
    @GetMapping("trajet/planifier")
    public ModelView planification() {
        return new ModelView("/trajet/planification.jsp");
    }

    @GetMapping("/trajet/assignation")
    public ModelView assignation() {
        try {
            reservationService.assignation();
        } catch (Exception e) {
            e.printStackTrace(); // ou loger l'erreur
        }

        // rediriger vers la page de planification après assignation
        return new ModelView("/trajet/planification.jsp");
    }
}
