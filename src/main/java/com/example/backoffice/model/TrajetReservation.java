package com.example.backoffice.model;

public class TrajetReservation {
    private Integer id;
    private Trajet trajet;
    private Reservation reservation;
    private Integer ordre;

    public TrajetReservation() {
        
    }

    public TrajetReservation(Integer id, Trajet trajet, Reservation reservation, Integer ordre) {
        this.id = id;
        this.trajet = trajet;
        this.reservation = reservation;
        this.ordre = ordre;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
