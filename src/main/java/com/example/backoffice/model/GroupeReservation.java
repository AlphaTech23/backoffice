package com.example.backoffice.model;

import java.time.LocalDateTime;
import java.util.List;

public class GroupeReservation {
    private List<Reservation> reservations;
    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;
    private LocalDateTime dateHeureProchain;
    
    public GroupeReservation(List<Reservation> reservations, LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin,
            LocalDateTime dateHeureProchain) {
        this.reservations = reservations;
        this.dateHeureDebut = dateHeureDebut;
        this.dateHeureFin = dateHeureFin;
        this.dateHeureProchain = dateHeureProchain;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }
    public LocalDateTime getDateHeureFin() {
        return dateHeureFin;
    }
    public void setDateHeureFin(LocalDateTime dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }
    public LocalDateTime getDateHeureProchain() {
        return dateHeureProchain;
    }
    public void setDateHeureProchain(LocalDateTime dateHeureProchain) {
        this.dateHeureProchain = dateHeureProchain;
    }

}
