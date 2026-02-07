CREATE DATABASE "AeroAssign"
WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'fr_FR.UTF-8'
    LC_CTYPE = 'fr_FR.UTF-8'
    TEMPLATE = template0;

\c "AeroAssign"

CREATE TABLE hotel (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    id_client VARCHAR(4) NOT NULL,
    nombre_place INTEGER NOT NULL CHECK (nombre_place > 0),
    date_heure_arrivee TIMESTAMP NOT NULL,
    id_hotel INTEGER NOT NULL,
    CONSTRAINT fk_reservation_hotel
        FOREIGN KEY (id_hotel)
        REFERENCES hotel(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);
