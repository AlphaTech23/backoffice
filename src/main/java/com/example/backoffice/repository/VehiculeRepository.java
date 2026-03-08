package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Vehicule;

import java.time.LocalDateTime;
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

    public List<Vehicule> getByCapacite(int capacite, LocalDateTime date) throws Exception {

        String sql = """
            SELECT v.*
            FROM v_trajet vt
            JOIN vehicule v ON vt.vehicule_id = v.id
            WHERE vt.date_trajet = ?
            AND vt.places_restantes >= ?
            AND vt.places_prises = 0
            AND vt.places_restantes = (
                SELECT MIN(places_restantes)
                FROM v_trajet
                WHERE date_trajet = ?
                AND places_restantes >= ?
                AND places_prises = 0
            )
        """;

        return DAO.getList(sql, Vehicule.class, date.toLocalDate(), capacite, date.toLocalDate(), capacite);
    }
}
