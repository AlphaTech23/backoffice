package com.example.backoffice.model;

public class Distance {

    private Integer id;
    private Hotel fromHotel;
    private Hotel toHotel;
    private Double kilometre;

    public Distance() {
    }

    public Distance(Integer id, Hotel fromHotel, Hotel toHotel, Double kilometre) {
        this.id = id;
        this.fromHotel = fromHotel;
        this.toHotel = toHotel;
        this.kilometre = kilometre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Hotel getFromHotel() {
        return fromHotel;
    }

    public void setFromHotel(Hotel fromHotel) {
        this.fromHotel = fromHotel;
    }

    public Hotel getToHotel() {
        return toHotel;
    }

    public void setToHotel(Hotel toHotel) {
        this.toHotel = toHotel;
    }

    public Double getKilometre() {
        return kilometre;
    }

    public void setKilometre(Double kilometre) {
        this.kilometre = kilometre;
    }
}