package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.TypeCarburant;

import java.util.List;

public class TypeCarburantRepository {

    public List<TypeCarburant> getAll() throws Exception {
        return DAO.getList("SELECT * FROM type_carburant", TypeCarburant.class);
    }
}
