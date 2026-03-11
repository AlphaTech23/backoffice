package com.example.backoffice.repository;

import java.util.List;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Distance;

public class DistanceRepository {
    private final DAO dao;

    public DistanceRepository(DAO dao) {
        this.dao = dao;
    }

    public List<Distance> getAll() throws Exception {
        String sql = """
            SELECT *
            FROM distance
            ORDER BY kilometre
        """;
        List<Distance> distances = dao.getList(sql, Distance.class);
        return distances;
    }
}