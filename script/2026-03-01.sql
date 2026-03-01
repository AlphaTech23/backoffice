-- Supprimer la base si elle existe
DROP DATABASE IF EXISTS "AeroAssign";

-- Création de la base en UTF-8
CREATE DATABASE "AeroAssign"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TEMPLATE = template0;

-- Se connecter à la base nouvellement créée
\c "AeroAssign";

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

CREATE TABLE type_carburant(
   id SERIAL,
   libelle VARCHAR(250)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE token(
   id SERIAL,
   token VARCHAR(250)  NOT NULL,
   date_expiration TIMESTAMP NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(token)
);

CREATE TABLE vehicule(
   id SERIAL,
   reference VARCHAR(50)  NOT NULL,
   capacite INTEGER NOT NULL,
   id_type_carburant INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_type_carburant) REFERENCES type_carburant(id)
);