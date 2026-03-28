package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Vehicule;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                        AND (
                            v.heure_disponible IS NULL
                            OR
                            v.heure_disponible <= ?
                        )
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
                        ORDER BY v.capacite ASC
                """;

        return dao.getList(
                sql,
                Vehicule.class,
                capacite,
                Time.valueOf(date.toLocalTime()),
                Date.valueOf(date.toLocalDate()),
                Time.valueOf(date.toLocalTime()),
                Time.valueOf(date.toLocalTime()));
    }

    public Integer getNombreTrajets(Integer idVehicule) throws Exception {
        String sql = """
                SELECT COUNT(*) FROM trajet WHERE id_vehicule = ?
                """;
        return dao.get(sql, Long.class, idVehicule).intValue();
    }

    public LocalTime getHeureRetour(Vehicule vehicule) throws Exception {
        String sql = """
                SELECT MAX(heure_retour) FROM trajet WHERE id_vehicule = ?
                """;
        LocalTime heureRetour = dao.get(sql, LocalTime.class, vehicule.getId());
        if (heureRetour == null)
            return vehicule.getHeureDisponible();
        return heureRetour;
    }

    public List<Vehicule> getVehiculeDisponible(LocalDateTime date) throws Exception {
        String sql = """
                    SELECT *
                        FROM vehicule v
                        WHERE (
                            v.heure_disponible IS NULL
                            OR
                            v.heure_disponible <= ?
                        )
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
                        ORDER BY v.capacite DESC
                """;
        return dao.getList(
                sql,
                Vehicule.class,
                Time.valueOf(date.toLocalTime()),
                Date.valueOf(date.toLocalDate()),
                Time.valueOf(date.toLocalTime()),
                Time.valueOf(date.toLocalTime()));
    }

    public List<Vehicule> getPremiersVehicules(LocalDateTime dateHeureFin,
            LocalDateTime dateHeureProchain) throws Exception {
        String sql = """
                    SELECT
                        v.*
                    FROM vehicule v
                    LEFT JOIN trajet t ON t.id_vehicule = v.id
                    AND t.date_trajet = ?
                    GROUP BY v.id, v.reference, v.capacite, v.heure_disponible
                    HAVING (
                        MAX(t.heure_retour) IS NULL AND (
                            v.heure_disponible IS NULL
                            OR v.heure_disponible BETWEEN ? AND ?
                        )
                    ) OR (
                        MAX(t.heure_retour) IS NOT NULL AND
                        MAX(t.heure_retour) BETWEEN ? AND ?
                    )
                    ORDER BY MAX(t.heure_retour)
                """;
        return dao.getList(
                sql,
                Vehicule.class,
                Date.valueOf(dateHeureFin.toLocalDate()),
                Time.valueOf(dateHeureFin.toLocalTime()),
                Time.valueOf(dateHeureProchain.toLocalTime()),
                Time.valueOf(dateHeureFin.toLocalTime()),
                Time.valueOf(dateHeureProchain.toLocalTime()));
    }
}
