package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Vehicule;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

public class VehiculeRepository {
    private final DAO dao;

    public VehiculeRepository(DAO dao) {
        this.dao = dao;
    }
    public void create(Vehicule v) throws Exception {
        String sql = "INSERT INTO vehicule(reference, capacite, id_type_carburant) VALUES (?, ?, ?)";
        dao.executeUpdate(sql, v.getReference(), v.getCapacite(), v.getTypeCarburant().getId());
    }

    public void update(Vehicule v) throws Exception {
        String sql = "UPDATE vehicule SET reference=?, capacite=?, id_type_carburant=? WHERE id=?";
        dao.executeUpdate(sql, v.getReference(), v.getCapacite(), v.getTypeCarburant().getId(), v.getId());
    }

    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM vehicule WHERE id=?";
        dao.executeUpdate(sql, id);
    }

    public List<Vehicule> getAll() throws Exception {
        return dao.getList("SELECT * FROM vehicule", Vehicule.class);
    }

    public List<Vehicule> getByCapacite(int capacite, LocalDateTime date) throws Exception {

        String sql = """
                    SELECT *
                        FROM vehicule v
                        WHERE v.capacite >= ?
                        AND NOT EXISTS (
                            SELECT 1
                            FROM trajet t
                            WHERE t.id_vehicule = v.id
                            AND t.date_trajet = ?
                            AND (
                                t.heure_retour IS NULL
                                OR (? >= t.heure_depart AND ? < t.heure_retour)
                            )
                        )
                        ORDER BY v.capacite
                """;

        return dao.getList(
                sql,
                Vehicule.class,
                capacite,
                Date.valueOf(date.toLocalDate()),
                Time.valueOf(date.toLocalTime()),
                Time.valueOf(date.toLocalTime())
            );
    }
}
