package com.example.backoffice.model;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationGroup {

    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;
    private List<Reservation> reservations;

    public ReservationGroup(LocalDateTime windowStart, LocalDateTime windowEnd, List<Reservation> reservations) {
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
        this.reservations = reservations;
    }

    public LocalDateTime getWindowStart() {
        return windowStart;
    }

    public LocalDateTime getWindowEnd() {
        return windowEnd;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}