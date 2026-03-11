package com.example.backoffice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.backoffice.service.TrajetService;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.dto.TrajetDTO;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.service.ReservationService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.PostMapping;
import com.example.framework.core.ModelView;

@Controller
public class TrajetController {

    private final TrajetService trajetService;
    private final ReservationService reservationService;
    private final DAO dao;

    public TrajetController() {
        dao = new DAO();
        this.trajetService = new TrajetService(dao);
        this.reservationService = new ReservationService(dao);
    }

    @GetMapping("/trajets")
    public ModelView liste(LocalDate date) throws Exception {
        ModelView mv = new ModelView("/trajet/list.jsp");
        try {
            dao.connect();
            List<Trajet> trajets = trajetService.getByDate(date);
            List<TrajetDTO> trajetDTOs = new ArrayList<>();
            for (Trajet trajet : trajets) {
                TrajetDTO trajetDTO = new TrajetDTO(trajet);
                trajetDTO.setReservations(reservationService.getByTrajet(trajet.getId()));
                trajetDTOs.add(trajetDTO);
            }
            mv.addAttribute("trajets", trajetDTOs);
            mv.addAttribute("date", date);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            dao.close();
        }
        return mv;
    }

    @GetMapping("/trajets/planifier")
    public ModelView planification() {
        return new ModelView("/trajet/planification.jsp");
    }

    @PostMapping("/trajets/assigner")
    public ModelView assignation() throws Exception {
        ModelView m = new ModelView("/trajet/planification.jsp");
        try {
            dao.connect();
            reservationService.assignation();
            m.addAttribute("message", "Planification effecutee avec succes");
        } catch (Exception e) {
            m.addAttribute("erreur", e.getMessage());
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return m;
    }
}
