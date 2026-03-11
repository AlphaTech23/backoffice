package com.example.backoffice.model;

import java.time.LocalDateTime;

public class Reservation {

    private Integer id;
    private String idClient;
    private Integer nombrePassager;
    private LocalDateTime dateArrivee;
    private Hotel hotel;

    public Reservation() {
    }

    public Reservation(Integer id, String idClient, Integer nombrePassager, LocalDateTime dateArrivee, Hotel hotel) {
        this.id = id;
        this.idClient = idClient;
        this.nombrePassager = nombrePassager;
        this.dateArrivee = dateArrivee;
        this.hotel = hotel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Integer getNombrePassager() {
        return nombrePassager;
    }

    public void setNombrePassager(Integer nombrePassager) {
        this.nombrePassager = nombrePassager;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
