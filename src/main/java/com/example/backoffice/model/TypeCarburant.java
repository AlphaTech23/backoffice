package com.example.backoffice.model;

public class TypeCarburant {

    private Integer id;
    private String libelle;

    public TypeCarburant() {
    }

    public TypeCarburant(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}