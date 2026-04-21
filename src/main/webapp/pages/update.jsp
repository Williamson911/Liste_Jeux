<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modifier un jeu — FromSoftware</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">
</head>
<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <p class="section-label">— Modifier un titre</p>

    <div class="form-card">
        <form action="/update" method="post" class="game-form">

            <input type="hidden" name="id" value="${jeu.id}">

            <div class="form-group">
                <label for="titre">Titre</label>
                <input type="text" id="titre" name="titre" value="${jeu.titre}" required>
            </div>

            <div class="form-group">
                <label for="annee">Année</label>
                <input type="number" id="annee" name="annee" value="${jeu.annee}" required>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="4">${jeu.description}</textarea>
            </div>

            <div class="form-group">
                <label for="imageUrl">URL de l'image</label>
                <input type="text" id="imageUrl" name="imageUrl" value="${jeu.imageUrl}">
            </div>

            <div class="form-group">
                <label for="prix">Prix (€)</label>
                <input type="number" id="prix" name="prix" value="${jeu.prix}" min="0" step="0.01" required>
            </div>

            <div class="form-actions">
                <a href="/listeJeux" class="back-link">← Annuler</a>
                <button type="submit" class="btn-submit">Modifier le jeu</button>
            </div>

        </form>
    </div>
</main>

</body>
</html>
