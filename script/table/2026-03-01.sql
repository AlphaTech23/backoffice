
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