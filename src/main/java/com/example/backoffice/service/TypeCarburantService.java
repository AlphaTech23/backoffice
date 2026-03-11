package com.example.backoffice.service;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.TypeCarburant;
import com.example.backoffice.repository.TypeCarburantRepository;

import java.util.List;

public class TypeCarburantService {

    private final TypeCarburantRepository typeCarburantRepository;

    public TypeCarburantService(DAO dao) {
        this.typeCarburantRepository = new TypeCarburantRepository(dao);
    }

    public List<TypeCarburant> getAll() throws Exception {
        return typeCarburantRepository.getAll();
    }
}
