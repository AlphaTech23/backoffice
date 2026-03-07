package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Hotel;

public class DistanceRepository {

    public Integer getBetween(Hotel h1, Hotel h2) throws Exception {

        String sql = """
            SELECT kilometre
            FROM distance
            WHERE (id_from_hotel = ? AND id_to_hotel = ?)
            OR (id_from_hotel = ? AND id_to_hotel = ?)
        """;

        String km = DAO.get(sql, String.class,
                h1.getId(), h2.getId(),
                h2.getId(), h1.getId());

        return km != null ? Integer.parseInt(km) : null;
    }
}