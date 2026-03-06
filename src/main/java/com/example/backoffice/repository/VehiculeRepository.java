package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Vehicule;

import java.util.List;

public class VehiculeRepository {

    public void create(Vehicule v) throws Exception {
        String sql = "INSERT INTO vehicule(reference, capacite, id_type_carburant) VALUES (?, ?, ?)";
        DAO.executeUpdate(sql, v.getReference(), v.getCapacite(), v.getTypeCarburant().getId());
    }

    public void update(Vehicule v) throws Exception {
        String sql = "UPDATE vehicule SET reference=?, capacite=?, id_type_carburant=? WHERE id=?";
        DAO.executeUpdate(sql, v.getReference(), v.getCapacite(), v.getTypeCarburant().getId(), v.getId());
    }

    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM vehicule WHERE id=?";
        DAO.executeUpdate(sql, id);
    }

    public List<Vehicule> getAll() throws Exception {
        return DAO.getList("SELECT * FROM vehicule", Vehicule.class);
    }
}
