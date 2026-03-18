INSERT INTO hotel (id, nom, code) VALUES
(0, 'Aeroport', 'IVAT'),
(1, 'Hotel 1','H1'),
(2, 'Hotel 2', 'H2'),
(3, 'Hotel 3', 'H3');

INSERT INTO type_carburant (id, libelle, code) VALUES
(1, 'Essence', 'ES'),
(2, 'Diesel', 'D');

INSERT INTO vehicule (id, reference, capacite, id_type_carburant) VALUES
(1, 'V1', 4, 1),
(2, 'V2', 6, 2);

INSERT INTO parametre (cle, valeur, type) VALUES
('temps_attente', '30', 'INT'),
('vitesse_moyenne', '60', 'INT');

INSERT INTO reservation (id_client, nombre_passager, date_arrivee, id_hotel) VALUES
('0001', 2, '2026-03-17 08:00:00', 1), -- R1
('0002', 4, '2026-03-17 08:05:00', 2), -- R2
('0003', 3, '2026-03-17 08:10:00', 3), -- R3
('0004', 6, '2026-03-17 08:35:00', 1), -- R4
('0005', 4, '2026-03-17 08:40:00', 2), -- R5
('0006', 3, '2026-03-17 08:45:00', 3), -- R6
('0007', 1, '2026-03-17 09:00:00', 1), -- R7
('0008', 5, '2026-03-17 09:05:00', 2), -- R8
('0009', 4, '2026-03-17 09:30:00', 2); -- R9

INSERT INTO distance (kilometre, id_from_hotel, id_to_hotel) VALUES
(10, 0, 1),
(15, 0, 2),
(20, 0, 3),
(5, 1, 2),
(10, 1, 3),
(8, 2, 3);

