CREATE TABLE trajet_reservation(
    id SERIAL PRIMARY KEY,
    id_trajet INTEGER,
    id_reservation INTEGER,
    ordre INTEGER,
    FOREIGN KEY(id_trajet) REFERENCES trajet(id),
    FOREIGN KEY(id_reservation) REFERENCES reservation(id)
);