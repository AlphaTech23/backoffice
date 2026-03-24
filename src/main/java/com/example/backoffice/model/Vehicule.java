package com.example.backoffice.model;

import java.time.LocalTime;

public class Vehicule {

    private Integer id;
    private String reference;
    private Integer capacite;
    private Integer capaciteRestante;
    private TypeCarburant typeCarburant;
    private LocalTime heureDisponible;

    public Vehicule() {
    }

    public Vehicule(Integer id, String reference, Integer capacite, TypeCarburant typeCarburant) {
        this.id = id;
        this.reference = reference;
        this.capacite = capacite;
        this.typeCarburant = typeCarburant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Integer getCapaciteRestante() {
        if(capaciteRestante == null) capaciteRestante = capacite;
        return capaciteRestante;
    }

    public void diminuerCapaciteRestante(Integer difference) {
        if(capaciteRestante == null) capaciteRestante = capacite;
        this.capaciteRestante -= difference;
    }

    public TypeCarburant getTypeCarburant() {
        return typeCarburant;
    }

    public void setTypeCarburant(TypeCarburant typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    
    public LocalTime getHeureDisponible() {
        return heureDisponible;
    }

    public void setHeureDisponible(LocalTime heureDisponible) {
        this.heureDisponible = heureDisponible;
    }
}