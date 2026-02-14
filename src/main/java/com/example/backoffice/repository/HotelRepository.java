package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.model.Hotel;

import java.util.List;

public class HotelRepository {

    public List<Hotel> getAll() throws Exception {
        String sql = "SELECT id, nom FROM hotel ORDER BY id";
        return DAO.getList(sql, Hotel.class);
    }
}
