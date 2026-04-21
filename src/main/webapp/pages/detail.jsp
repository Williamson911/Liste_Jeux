<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>${jeu.titre}</title>

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@300;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">

</head>

<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <div class="section-label">Détails du jeu</div>

    <div class="game-detail">

        <div class="game-header">
            <img src="${jeu.imageUrl}" alt="${jeu.titre}" class="game-image">

            <div>
                <div class="game-title">${jeu.titre}</div>
                <div class="game-meta">Année — ${jeu.annee}</div>
            </div>
        </div>

        <div class="game-description">
            ${jeu.description}
        </div>

    </div>

    <a href="/listeJeux" class="back-link">← Retour à la liste</a>
</main>

</body>
</html>