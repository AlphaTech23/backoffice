CREATE TABLE parametre(
   id SERIAL,
   cle VARCHAR(50)  NOT NULL,
   valeur VARCHAR(50)  NOT NULL,
   type VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(cle)
);

CREATE TABLE distance(
   id SERIAL,
   kilometre NUMERIC(15,2) NOT NULL,
   id_from_hotel INTEGER NOT NULL,
   id_to_hotel INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_from_hotel) REFERENCES hotel(id),
   FOREIGN KEY(id_to_hotel) REFERENCES hotel(id)
);

CREATE TABLE trajet(
   id SERIAL,
   date_trajet DATE NOT NULL,
   heure_depart TIME NOT NULL,
   heure_retour TIME,
   id_vehicule INTEGER NOT NULL,
   distance NUMERIC(15, 2) NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_vehicule) REFERENCES vehicule(id)
); 