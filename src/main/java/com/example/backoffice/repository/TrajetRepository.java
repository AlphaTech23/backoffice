package com.example.backoffice.repository;

import com.example.backoffice.dao.DAO;
import com.example.backoffice.model.Trajet;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

            try (Connection conn = DAO.getConnection()) {

                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setDate(1, trajet.getDateTrajet() != null ? Date.valueOf(trajet.getDateTrajet()) : null);
                ps.setTime(2, trajet.getHeureDepart() != null ? Time.valueOf(trajet.getHeureDepart()) : null);
                ps.setTime(3, trajet.getHeureRetour() != null ? Time.valueOf(trajet.getHeureRetour()) : null);
                ps.setInt(4, trajet.getVehicule().getId());
                ps.setDouble(5, trajet.getDistance() != null ? trajet.getDistance() : 0);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    trajet.setId(rs.getInt(1)); // <-- on met l'id généré dans l'objet
                }

                rs.close();
                ps.close();
            }
        }
    }

    public List<Trajet> getByCapacite(int capacite, LocalDateTime dateTime) throws Exception {

        String sql = """
                SELECT *
                FROM v_trajet vt
                WHERE vt.places_restantes >= ?
                    AND vt.date_trajet = ?
                ORDER BY places_restantes
                """;

        Date sqlDate = Date.valueOf(dateTime.toLocalDate());

        return DAO.getList(sql, Trajet.class,
                capacite,
                sqlDate);
    }
}