package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;

import java.util.List;

public class HotelRepository {
    private final DAO dao;
    
    public HotelRepository(DAO dao) {
        this.dao = dao;
    }

    public List<Hotel> getAll() throws Exception {
        String sql = "SELECT id, nom FROM hotel ORDER BY id";
        return dao.getList(sql, Hotel.class);
    }

    public Hotel getAeroport() throws Exception {
        String sql = "SELECT * FROM hotel WHERE code = ?";
        return dao.get(sql, Hotel.class, "IVAT");
    }
}
