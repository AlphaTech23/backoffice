CREATE OR REPLACE VIEW v_trajet AS
SELECT
    t.id AS id,
    t.date_trajet,
    t.heure_depart,
    t.heure_retour,
    t.distance,

    v.id AS id_vehicule,
    v.reference,
    v.capacite,
    v.id_type_carburant,

    COALESCE(SUM(r.nombre_passager),0) AS places_prises,
    v.capacite - COALESCE(SUM(r.nombre_passager),0) AS places_restantes

FROM trajet t
JOIN vehicule v ON t.id_vehicule = v.id

LEFT JOIN trajet_reservation tr ON tr.id_trajet = t.id
LEFT JOIN reservation r ON r.id = tr.id_reservation

GROUP BY
    t.id,
    t.date_trajet,
    t.heure_depart,
    t.heure_retour,
    t.distance,
    v.id,
    v.reference,
    v.capacite,
    v.id_type_carburant;