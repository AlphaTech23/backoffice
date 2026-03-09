package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Distance;
import com.example.backoffice.model.Hotel;

public class DistanceRepository {

    public Double getBetween(Hotel h1, Hotel h2) throws Exception {

        String sql = """
            SELECT *
            FROM distance
            WHERE (id_from_hotel = ? AND id_to_hotel = ?)
            OR (id_from_hotel = ? AND id_to_hotel = ?)
        """;

        Distance km = DAO.get(sql, Distance.class,
                h1.getId(), h2.getId(),
                h2.getId(), h1.getId());
        System.out.println(km.getKilometre());

        return km != null ? km.getKilometre() : null;
    }
}