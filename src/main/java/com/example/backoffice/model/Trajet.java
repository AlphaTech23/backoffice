package com.example.backoffice.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Trajet {

    private Integer id;
    private LocalDate dateTrajet;
    private LocalTime heureDepart;
    private LocalTime heureRetour;
    private Vehicule vehicule;
    private Double distance;

    public Trajet() {
    }

    public Trajet(Integer id, LocalDate dateTrajet, LocalTime heureDepart, 
            LocalTime heureRetour, Vehicule vehicule, Double distance, Integer placesRestantes) {
        this.id = id;
        this.dateTrajet = dateTrajet;
        this.heureDepart = heureDepart;
        this.heureRetour = heureRetour;
        this.vehicule = vehicule;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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