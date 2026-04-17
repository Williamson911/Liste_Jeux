-- =========================================================
-- Requêtes CRUD pour la table jeux (PostgreSQL)
-- Les ? sont les placeholders pour les PreparedStatement JDBC.
-- =========================================================

-- ---------- READ ----------
-- Liste de tous les jeux
SELECT id, titre, annee, description, image_url
FROM jeux
ORDER BY annee;

-- Détail d'un jeu par id
SELECT id, titre, annee, description, image_url
FROM jeux
WHERE id = ?;

-- ---------- CREATE ----------
INSERT INTO jeux (titre, annee, description, image_url)
VALUES (?, ?, ?, ?);

-- ---------- UPDATE ----------
UPDATE jeux
SET titre       = ?,
    annee       = ?,
    description = ?,
    image_url   = ?
WHERE id = ?;

-- ---------- DELETE ----------
DELETE FROM jeux
WHERE id = ?;
