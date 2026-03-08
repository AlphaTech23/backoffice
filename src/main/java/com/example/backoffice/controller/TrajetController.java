package com.example.backoffice.controller;

import java.time.LocalDate;

import com.example.backoffice.service.TrajetService;
import com.example.backoffice.service.ReservationService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.core.ModelView;

@Controller
public class TrajetController {

    private TrajetService trajetService;
    private ReservationService reservationService;

    public TrajetController() {
        this.trajetService = new TrajetService();
        this.reservationService = new ReservationService();
    }

    @GetMapping("/trajets")
    public ModelView liste(LocalDate date) {

        ModelView mv = new ModelView("/trajet/list.jsp");

        try {
            mv.addAttribute("trajets", trajetService.getByDate(date));
            mv.addAttribute("date", date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }
    
    @GetMapping("/trajets/planifier")
    public ModelView planification() {
        return new ModelView("/trajet/planification.jsp");
    }

    @GetMapping("/trajets/assigner")
    public ModelView assignation() {
        ModelView m = new ModelView("/trajet/planification.jsp");
        try {
            reservationService.assignation();
        } catch (Exception e) {
            m.addAttribute("erreur", e.getMessage());
        }
        return m;
    }
}
