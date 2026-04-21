<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Caddy — FromSoftware</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">
</head>
<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <div class="list-header">
        <p class="section-label">— Mon caddy</p>
    </div>

    <c:if test="${param.success == 'true'}">
        <p class="cart-success">Commande simulée avec succès !</p>
    </c:if>

    <c:choose>
        <c:when test="${empty cart}">
            <p class="cart-empty">Votre caddy est vide.</p>
        </c:when>
        <c:otherwise>
            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Jeu</th>
                        <th>Prix unitaire</th>
                        <th>Quantité</th>
                        <th>Sous-total</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="entry" items="${cart}">
                        <c:set var="item" value="${entry.value}" />
                        <tr>
                            <td class="cart-game-cell">
                                <img src="${item.jeu.imageUrl}" class="game-thumb" alt="${item.jeu.titre}">
                                <span>${item.jeu.titre}</span>
                            </td>
                            <td><fmt:formatNumber value="${item.jeu.prix}" type="currency" currencySymbol="€" maxFractionDigits="2"/></td>
                            <td>
                                <form method="post" action="/cart" class="cart-qty-form">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="jeuId" value="${item.jeu.id}">
                                    <input type="number" name="quantite" value="${item.quantite}" min="0" class="cart-qty-input" onchange="this.form.submit()">
                                </form>
                            </td>
                            <td><fmt:formatNumber value="${item.sousTotal}" type="currency" currencySymbol="€" maxFractionDigits="2"/></td>
                            <td>
                                <form method="post" action="/cart">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="jeuId" value="${item.jeu.id}">
                                    <button type="submit" class="btn-delete-form">Retirer</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" class="cart-total-label">Total</td>
                        <td colspan="2" class="cart-total-value">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="€" maxFractionDigits="2"/>
                        </td>
                    </tr>
                </tfoot>
            </table>

            <div class="cart-actions">
                <form method="post" action="/cart">
                    <input type="hidden" name="action" value="checkout">
                    <button type="submit" class="btn-submit">Commander</button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>
