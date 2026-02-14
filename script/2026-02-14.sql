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