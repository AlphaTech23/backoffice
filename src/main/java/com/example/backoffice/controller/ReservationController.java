package com.example.backoffice.controller;

import com.example.backoffice.service.ReservationService;
import com.example.framework.annotations.Authorized;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.Json;
import com.example.framework.annotations.PostMapping;
import com.example.framework.core.ModelView;
import com.example.backoffice.service.HotelService;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final HotelService hotelService;
    private final DAO dao;

    public ReservationController() {
        dao = new DAO();
        this.reservationService = new ReservationService(dao);
        this.hotelService = new HotelService(dao);
    }

    @GetMapping("/reservation/form")
    public ModelView form() throws Exception {

        ModelView mv = new ModelView("/reservation/form.jsp");

        try {
            dao.connect();
            mv.addAttribute("hotels", hotelService.getAll());
        } catch (Exception e) {
            mv.addAttribute("hotels", List.of());
            mv.addAttribute("error", "Impossible de charger la liste des hôtels : " + e.getMessage());
        } finally {
            dao.close();
        }

        return mv;
    }

    @PostMapping("/reservation/reserver")
    public ModelView reserver(String idClient,
            Integer nombrePassager,
            String dateArrivee,
            Integer idHotel) throws Exception {

        ModelView mv = new ModelView("/reservation/form.jsp");

        try {
            dao.connect();
            reservationService.reserver(
                    idClient,
                    nombrePassager,
                    LocalDateTime.parse(dateArrivee),
                    idHotel);

            mv.addAttribute("message", "Réservation effectuée avec succès");
            mv.addAttribute("hotels", hotelService.getAll());
        } catch (Exception e) {
            mv.addAttribute("hotels", List.of());
            mv.addAttribute("error", "Impossible de charger la liste des hôtels : " + e.getMessage());
        } finally {
            dao.close();
        }

        return mv;
    }

    @GetMapping("/api/reservations")
    @Json
    @Authorized
    public List<Reservation> getReservations(String date) throws Exception {
        try {
            dao.connect();
            List<Reservation> reservations = reservationService.getByDateArrivee(date);
            return reservations;
        } catch(Exception e) {
            throw e;
        } finally {
            dao.close();
        }
    }

    @GetMapping("/reservations/non-assigner")
    public ModelView getNonAssigne(LocalDate date) throws Exception {

        ModelView mv = new ModelView("/reservation/unassigned-list.jsp");

        try {
            dao.connect();
            if(date == null) 
            mv.addAttribute("reservations", reservationService.getNonAssigne(date));
            mv.addAttribute("date", date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        return mv;
    }
}
