package com.example.backoffice.controllers;

import com.example.backoffice.service.ReservationService;
import com.example.framework.annotations.Controller;
import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.PostMapping;
import com.example.framework.core.ModelView;
import com.example.backoffice.service.HotelService;
import com.example.backoffice.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReservationController {

    private ReservationService reservationService;
    private HotelService hotelService;

    public ReservationController() {
        this.reservationService = new ReservationService();
        this.hotelService = new HotelService();
    }

    @GetMapping("/reservation/form")
    public ModelView form() {

        ModelView mv = new ModelView("reservation/form");

        try {
            mv.addAttribute("hotels", hotelService.getAll());
        } catch (Exception e) {
            mv.addAttribute("hotels", List.of()); 
            mv.addAttribute("error", "Impossible de charger la liste des hôtels : " + e.getMessage());
        }

        return mv;
    }

    @PostMapping("/reservation/reserver")
    public ModelView reserver(String idClient,
            Integer nombrePassager,
            String dateArrive,
            Integer idHotel) {

        ModelView mv = new ModelView("reservation/form"); 

        try {
            reservationService.reserver(
                    idClient,
                    nombrePassager,
                    LocalDateTime.parse(dateArrive),
                    idHotel);

            mv.addAttribute("message", "Réservation effectuée avec succès");

        } catch (Exception e) {
            mv.addAttribute("error", "Erreur lors de la réservation : " + e.getMessage());
        }

        try {
            mv.addAttribute("hotels", hotelService.getAll());
        } catch (Exception e) {
            mv.addAttribute("hotels", List.of());
            mv.addAttribute("error", "Impossible de charger la liste des hôtels : " + e.getMessage());
        }

        return mv;
    }


@GetMapping("/api/reservations")
@RestAPI
public List<Reservation> getReservations(String date) throws Exception {
    return reservationService.getByDateArrive(date)
}
}
