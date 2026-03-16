INSERT INTO hotel (code, nom) VALUES
('COLB', 'Colbert'),
('NOVO', 'Novotel'),
('IBIS', 'Ibis'),
('LOKA', 'Lokanga'),
('IVAT', 'Ivato Aeroport');

INSERT INTO type_carburant(code, libelle) VALUES
('D', 'Diesel'),
('H', 'Helium'),
('ES', 'Essence');

INSERT INTO reservation 
(id_client, nombre_passager, date_arrivee,   id_hotel)
VALUES
('4631', 11, '2026-02-05 00:01:00', 3),
('4394', 1, '2026-02-05 23:55:00', 3),
('8054', 2, '2026-02-09 10:17:00', 1),
('1432', 4, '2026-02-01 15:25:00', 2),
('7861', 4, '2026-01-28 07:11:00', 1),
('3308', 5, '2026-01-28 07:45:00', 1),
('4484', 13, '2026-02-28 08:25:00', 2),
('9687', 8, '2026-02-28 13:00:00', 2),
('6302', 7, '2026-02-15 13:00:00', 1),
('8640', 1, '2026-02-18 22:55:00', 4);

INSERT INTO distance (kilometre, id_from_hotel, id_to_hotel) VALUES
(15, 5, 1), -- IVAT -> COLB
(14, 5, 2), -- IVAT -> NOVO
(13, 5, 3), -- IVAT -> IBIS
(18, 5, 4), -- IVAT -> LOKA

(1.2, 1, 2), -- COLB -> NOVO
(0.8, 1, 3), -- COLB -> IBIS
(4.5, 1, 4), -- COLB -> LOKA

(0.7, 2, 3), -- NOVO -> IBIS
(4.2, 2, 4), -- NOVO -> LOKA

(4.8, 3, 4); -- IBIS -> LOKA

INSERT INTO parametre (cle, valeur, type) VALUES
('vitesse_moyenne', '40', 'double'),
('temps_attente', '00:10:00', 'time');