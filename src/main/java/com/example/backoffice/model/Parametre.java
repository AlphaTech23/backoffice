package com.example.backoffice.model;

public class Parametre {

    private Integer id;
    private String cle;
    private String valeur;
    private String type;

    public Parametre() {
    }

    public Parametre(Integer id, String cle, String valeur, String type) {
        this.id = id;
        this.cle = cle;
        this.valeur = valeur;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}