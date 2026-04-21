<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>FromSoftware — Mes Favoris</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">
</head>
<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <div class="list-header">
        <p class="section-label">— Mes Favoris</p>
    </div>

    <c:choose>
        <c:when test="${empty favoris}">
            <p class="empty-state">Aucun favori enregistré pour le moment.</p>
        </c:when>
        <c:otherwise>
            <ul class="game-list">
                <c:forEach var="favori" items="${favoris}">
                    <li>
                        <span class="favori-type">${favori.typeElement}</span>
                        <span class="game-title favori-nom">${favori.nomElement}</span>
                        <form method="post" action="/favoris" style="margin-left: auto;">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="id"     value="${favori.id}">
                            <button type="submit" class="btn-delete btn-delete-form">Retirer</button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>

    <a href="/listeJeux" class="back-link">← Retour au catalogue</a>
</main>

</body>
</html>
