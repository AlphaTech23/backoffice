package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.TypeCarburant;

import java.util.List;

public class TypeCarburantRepository {
    public final DAO dao;

    public TypeCarburantRepository(DAO dao) {
        this.dao = dao;
    }
    public List<TypeCarburant> getAll() throws Exception {
        return dao.getList("SELECT * FROM type_carburant", TypeCarburant.class);
    }
}
