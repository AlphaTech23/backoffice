package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Trajet;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TrajetRepository {
    private final DAO dao;

    public TrajetRepository(DAO dao) {
        this.dao = dao;
    }

    public List<Trajet> getByDate(LocalDate date) throws Exception {

        String sql = "SELECT * FROM trajet";
        
        if(date == null) return dao.getList(sql, Trajet.class);

        sql += " WHERE date_trajet = ?";
        return dao.getList(sql, Trajet.class, date);
    }

    public List<Trajet> getByVehicule(Integer vehiculeId, LocalDateTime dateTime) throws Exception {

        String sql = """
                    SELECT *
                    FROM trajet
                    WHERE id_vehicule = ?
                      AND date_trajet = ?
                      AND heure_depart <= ?
                      AND heure_retour > ?
                    ORDER BY heure_depart
                """;

        Date sqlDate = Date.valueOf(dateTime.toLocalDate());
        Time sqlTime = Time.valueOf(dateTime.toLocalTime());

        return dao.getList(sql, Trajet.class, vehiculeId, sqlDate, sqlTime, sqlTime);
    }

    public void save(Trajet trajet) throws Exception {

        if (trajet.getId() != null) {
            String sql = """
                        UPDATE trajet
                        SET date_trajet = ?,
                            heure_depart = ?,
                            heure_retour = ?,
                            id_vehicule = ?,
                            distance = ?
                        WHERE id = ?
                    """;

            dao.executeUpdate(sql,
                    Date.valueOf(trajet.getDateTrajet()),
                    Time.valueOf(trajet.getHeureDepart()),
                    Time.valueOf(trajet.getHeureRetour()),
                    trajet.getVehicule().getId(),
                    trajet.getDistance(),
                    trajet.getId());

        } else {
            String sql = """
                        INSERT INTO trajet(id_vehicule, distance, date_trajet, heure_depart, heure_retour)
                        VALUES (?, ?, ?, ?, ?)
                    """;

            Integer id = dao.executeUpdate(sql, trajet.getVehicule().getId(),
                    trajet.getDistance() != null ? trajet.getDistance() : 0,
                    trajet.getDateTrajet() != null ? Date.valueOf(trajet.getDateTrajet()) : null,
                    trajet.getHeureDepart() != null ? Time.valueOf(trajet.getHeureDepart()) : null,
                    trajet.getHeureRetour() != null ? Time.valueOf(trajet.getHeureRetour()) : null);

            if (id > 0)
                trajet.setId(id);
        }
    }

    public Trajet getByCapacite(int capacite, LocalDateTime min, LocalDateTime max) throws Exception {

        String sql = """
                SELECT *
                    FROM v_trajet
                    WHERE places_restantes >= ?
                    AND (date_trajet + heure_depart)
                        BETWEEN ?
                        AND ?
                    ORDER BY id
                    LIMIT 1;
                """;

        return dao.get(sql, Trajet.class,
                capacite, min, max);
    }

    public void deleteByDate(LocalDate date) throws Exception {
        String sql = "DELETE FROM trajet WHERE date_trajet = ?";
        dao.executeUpdate(sql, date);
    }
}