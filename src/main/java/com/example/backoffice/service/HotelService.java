package com.example.backoffice.service;

import com.example.backoffice.repository.HotelRepository;
import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;

import java.util.List;

public class HotelService {

    private HotelRepository hotelRepository;

    public HotelService(DAO dao) {
        this.hotelRepository = new HotelRepository(dao);
    }

    public List<Hotel> getAll() throws Exception {
        return hotelRepository.getAll();
    }
}
