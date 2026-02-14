package com.example.backoffice.models;

import java.time.LocalDateTime;

public class Reservation {
    private Integer id;
    private String idClient;
    private Integer nombrePassager;
    private LocalDateTime dateArrive;
    private Hotel hotel;

    public Reservation() {
    }

    public Reservation(Integer id, String idClient, Integer nombrePassager, LocalDateTime dateArrive, Hotel hotel) {
        this.id = id;
        this.idClient = idClient;
        this.nombrePassager = nombrePassager;
        this.dateArrive = dateArrive;
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

    public LocalDateTime getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(LocalDateTime dateArrive) {
        this.dateArrive = dateArrive;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
