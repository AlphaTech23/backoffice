package com.example.backoffice.controller;

import java.time.LocalDate;

import com.example.backoffice.service.TrajetService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.core.ModelView;

@Controller
public class TrajetController {

    private TrajetService trajetService;

    public TrajetController() {
        this.trajetService = new TrajetService();
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
}
