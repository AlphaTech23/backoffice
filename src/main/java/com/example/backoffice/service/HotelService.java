package com.example.backoffice.service;

import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.model.Hotel;

import java.util.List;

public class HotelService {

    private HotelRepository hotelRepository;

    public HotelService() {
        this.hotelRepository = new HotelRepository();
    }

    public List<Hotel> getAll() throws Exception {
        return hotelRepository.getAll();
    }
}
