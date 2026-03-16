ALTER TABLE reservation DROP COLUMN id_trajet CASCADE;
ALTER TABLE reservation DROP COLUMN ordre;

ALTER TABLE trajet_reservation
DROP CONSTRAINT trajet_reservation_id_trajet_fkey;

ALTER TABLE trajet_reservation
ADD CONSTRAINT trajet_reservation_id_trajet_fkey
FOREIGN KEY (id_trajet)
REFERENCES trajet(id)
ON DELETE CASCADE;