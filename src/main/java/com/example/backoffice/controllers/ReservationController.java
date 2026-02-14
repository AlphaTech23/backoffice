package com.example.backoffice.controllers;

import com.example.backoffice.models.Reservation;
import com.example.backoffice.services.ReservationService;
import com.example.framework.annotation.Controller;
import com.example.framework.annotation.Get;
import com.example.framework.annotation.UrlMapping;
import com.example.framework.annotation.RestAPI;
import java.util.List;

// Assuming framework annotations based on typical custom framework patterns in this context
@Controller
@UrlMapping("/api/reservations")
public class ReservationController {

    private ReservationService service = new ReservationService();

    @Get
    @RestAPI
    public List<Reservation> getReservations(String date) throws Exception {
        return service.getByDateArrive(date);
    }
}
