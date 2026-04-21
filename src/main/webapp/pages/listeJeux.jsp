<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>FromSoftware — Catalogue</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">
</head>
<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <div class="list-header">
        <p class="section-label">— Catalogue des titres</p>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a href="/create" class="btn-add">+ Ajouter</a>
        </c:if>
    </div>
    <ul class="game-list">
        <c:forEach var="jeu" items="${listeJeux}">
            <li>
                <a href="/detail?id=${jeu.id}">
                    <img src="${jeu.imageUrl}" class="game-thumb" alt="${jeu.titre}">
                    <span class="game-title">${jeu.titre}</span>
                    <span class="game-year">${jeu.annee}</span>
                </a>
                <c:if test="${not empty sessionScope.user}">
                    <form method="post" action="/favoris">
                        <input type="hidden" name="nomElement"  value="${jeu.titre}">
                        <input type="hidden" name="typeElement" value="Jeu">
                        <button type="submit" class="btn-delete-form btn-favori">♥ Favori</button>
                    </form>
                </c:if>
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <a href="/update?id=${jeu.id}" class="btn-edit">Modifier</a>
                    <a href="/delete?id=${jeu.id}" class="btn-delete">Supprimer</a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</main>

</body>
</html>
