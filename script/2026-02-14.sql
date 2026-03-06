-- Création des tables
CREATE TABLE hotel (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    id_client CHAR(4) NOT NULL CHECK (id_client ~ '^[0-9]{4}$'),
    nombre_passager INT NOT NULL CHECK (nombre_passager > 0),
    date_arrive TIMESTAMP NOT NULL,
    id_hotel INT NOT NULL,
    
    CONSTRAINT fk_hotel
        FOREIGN KEY (id_hotel)
        REFERENCES hotel(id)
        ON DELETE CASCADE
);