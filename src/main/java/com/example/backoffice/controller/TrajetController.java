package com.example.backoffice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.backoffice.service.TrajetService;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.dto.TrajetDTO;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.TrajetReservation;
import com.example.backoffice.service.ReservationService;
import com.example.backoffice.service.TrajetReservationService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.PostMapping;
import com.example.framework.core.ModelView;

@Controller
public class TrajetController {

    private final TrajetService trajetService;
    private final ReservationService reservationService;
    private final TrajetReservationService trajetReservationService;
    private final DAO dao;

    public TrajetController() {
        dao = new DAO();
        this.trajetService = new TrajetService(dao);
        this.reservationService = new ReservationService(dao);
        this.trajetReservationService = new TrajetReservationService(dao);
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
                List<TrajetReservation> tr = trajetReservationService.getByTrajet(trajet.getId(), false);
                trajetDTO.setTrajetReservations(tr);
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
    public ModelView planification(String s, String error) {
        ModelView mv = new ModelView("/trajet/planification.jsp");
        if ("true".equals(s))
            mv.addAttribute("message", "Planification effecutee avec succes");
        else if (s != null)
            if (error == null)
                mv.addAttribute("erreur", "Une erreur s'est produite durant la planification");
            else
                mv.addAttribute("erreur", error);
        return mv;
    }

    @PostMapping("/trajets/assigner")
    public ModelView assignation(LocalDate date) throws Exception {
        ModelView mv = new ModelView("planifier?s=true");
        try {
            dao.connect();
            if (dao.getConnection() != null)
                dao.getConnection().setAutoCommit(false);
            reservationService.assignation(date);
            dao.getConnection().commit();
        } catch (Exception e) {
            mv.setView("planifier?s=false&error=" + e.getMessage());
            if (dao.getConnection() != null)
                dao.getConnection().rollback();
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return mv;
    }
}
