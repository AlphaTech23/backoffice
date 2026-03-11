package com.example.backoffice.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.backoffice.model.TrajetReservation;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.Vehicule;

public class TrajetDTO {

    private Integer id;
    private LocalDate dateTrajet;
    private LocalTime heureDepart;
    private LocalTime heureRetour;
    private Vehicule vehicule;
    private Double distance;
    private List<TrajetReservation> trajetReservations;

    public TrajetDTO() {
    }

    public TrajetDTO(Trajet trajet) {
        this.id = trajet.getId();
        this.dateTrajet = trajet.getDateTrajet();
        this.heureDepart = trajet.getHeureDepart();
        this.heureRetour = trajet.getHeureRetour();
        this.vehicule = trajet.getVehicule();
        this.distance = trajet.getDistance();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrajetReservation> getTrajetReservations() {
        return trajetReservations;
    }

    public void setTrajetReservations(List<TrajetReservation> reservations) {
        this.trajetReservations = reservations;
    }

    public LocalDate getDateTrajet() {
        return dateTrajet;
    }

    public void setDateTrajet(LocalDate dateTrajet) {
        this.dateTrajet = dateTrajet;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalTime getHeureRetour() {
        return heureRetour;
    }

    public void setHeureRetour(LocalTime heureRetour) {
        this.heureRetour = heureRetour;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}