package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Trajet;
import com.example.backoffice.model.Vehicule;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetRepository {

    public List<Trajet> getByDate(LocalDate date) throws Exception {

        String sql = """
                    SELECT t.*
                    FROM trajet t
                    LEFT JOIN reservation r ON r.id_trajet = t.id
                    WHERE t.date_trajet = ?
                    GROUP BY t.id
                    ORDER BY COALESCE(SUM(r.nombre_passager),0)
                """;

        return DAO.getList(sql, Trajet.class, date);
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

        // extraire la date et l'heure
        Date sqlDate = Date.valueOf(dateTime.toLocalDate());
        Time sqlTime = Time.valueOf(dateTime.toLocalTime());

        return DAO.getList(sql, Trajet.class, vehiculeId, sqlDate, sqlTime, sqlTime);
    }

    public void save(Trajet trajet) throws Exception {

        if (trajet.getId() != null) {
            // UPDATE
            String sql = """
                        UPDATE trajet
                        SET date_trajet = ?,
                            heure_depart = ?,
                            heure_retour = ?,
                            id_vehicule = ?,
                            distance = ?
                        WHERE id = ?
                    """;

            DAO.executeUpdate(sql,
                    Date.valueOf(trajet.getDateTrajet()),
                    Time.valueOf(trajet.getHeureDepart()),
                    Time.valueOf(trajet.getHeureRetour()),
                    trajet.getVehicule().getId(),
                    trajet.getDistance(),
                    trajet.getId());

        } else {
            // INSERT
            String sql = """
                        INSERT INTO trajet(date_trajet, heure_depart, heure_retour, id_vehicule, distance)
                        VALUES (?, ?, ?, ?, ?)
                    """;

            DAO.executeUpdate(sql,
                    Date.valueOf(trajet.getDateTrajet()),
                    Time.valueOf(trajet.getHeureDepart()),
                    Time.valueOf(trajet.getHeureRetour()),
                    trajet.getVehicule().getId(),
                    trajet.getDistance());
        }
    }
}