package com.example.backoffice.model;

public class Vehicule {

    private Integer id;
    private String reference;
    private Integer capacite;
    private TypeCarburant typeCarburant;

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

    public TypeCarburant getTypeCarburant() {
        return typeCarburant;
    }

    public void setTypeCarburant(TypeCarburant typeCarburant) {
        this.typeCarburant = typeCarburant;
    }
}