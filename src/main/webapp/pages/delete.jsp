<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Supprimer un jeu — FromSoftware</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">
</head>
<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <p class="section-label">— Supprimer un titre</p>

    <div class="form-card">
        <form action="/delete" method="post" class="game-form">

            <input type="hidden" name="id" value="${jeu.id}">

            <div class="confirm-message">
                <img src="${jeu.imageUrl}" class="game-thumb" alt="${jeu.titre}">
                <p>Êtes-vous sûr de vouloir supprimer <strong>${jeu.titre}</strong> (${jeu.annee}) ?</p>
            </div>

            <div class="form-actions">
                <a href="/listeJeux" class="back-link">← Annuler</a>
                <button type="submit" class="btn-submit">Supprimer le jeu</button>
            </div>

        </form>
    </div>
</main>

</body>
</html>
