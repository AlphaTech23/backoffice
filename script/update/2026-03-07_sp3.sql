ALTER TABLE hotel ADD COLUMN code VARCHAR(10);

ALTER TABLE hotel ALTER COLUMN code SET NOT NULL;

ALTER TABLE type_carburant ADD COLUMN code VARCHAR(5);

ALTER TABLE type_carburant ALTER COLUMN code SET NOT NULL;

ALTER TABLE reservation ADD COLUMN id_trajet INTEGER;

ALTER TABLE reservation ADD CONSTRAINT reservation_id_trajet_fkey
FOREIGN KEY(id_trajet) REFERENCES trajet(id);

ALTER TABLE reservation ADD COLUMN ordre INTEGER;