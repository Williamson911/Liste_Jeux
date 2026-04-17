-- =========================================================
-- Base de données : FromSoftware - Liste des jeux
-- SGBD : PostgreSQL
-- =========================================================
-- À exécuter depuis psql (connecté en super-user) :
--     \i schema.sql
-- ou commande par commande.
-- =========================================================

DROP DATABASE IF EXISTS fromsoftware_db;
CREATE DATABASE fromsoftware_db
    WITH ENCODING = 'UTF8';

\c fromsoftware_db;

-- =========================================================
-- Table : jeux
-- =========================================================
DROP TABLE IF EXISTS jeux;

CREATE TABLE jeux (
    id          SERIAL PRIMARY KEY,
    titre       VARCHAR(100) NOT NULL,
    annee       INTEGER      NOT NULL,
    description TEXT,
    image_url   VARCHAR(255)
);

-- =========================================================
-- Données initiales
-- =========================================================
INSERT INTO jeux (titre, annee, description, image_url) VALUES
    ('Demon''s Souls',  2009, 'Le premier souls, sombre et impitoyable.',           '/images/DemonsSouls.jpg'),
    ('Dark Souls',      2011, 'Un classique culte, interconnecté et mystérieux.',   '/images/DarkSouls.jpg'),
    ('Dark Souls II',   2014, 'Plus grand, plus difficile, plus controversé.',      '/images/DarkSouls2.jpg'),
    ('Bloodborne',      2015, 'Ambiance gothique lovecraftienne, combat agressif.', '/images/bloodborne.jpg'),
    ('Dark Souls III',  2016, 'La conclusion épique de la trilogie.',               '/images/DarkSouls3.jpg'),
    ('Sekiro',          2019, 'Samouraï, parry et boss brutaux.',                   '/images/Sekiro.jpg'),
    ('Elden Ring',      2022, 'Open world épique co-écrit avec G.R.R. Martin.',     '/images/eldenring.jpg');
