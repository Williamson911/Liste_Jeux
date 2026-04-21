<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>FromSoftware — Connexion</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@600;700&family=Roboto:wght@300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/styles/style.css">
    <style>
        .error-message {
            font-size: 0.75rem;
            letter-spacing: 0.15em;
            text-transform: uppercase;
            color: var(--accent);
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/fragments/navbar.jspf" %>

<main>
    <p class="section-label">— Connexion</p>

    <div class="form-card">

        <c:if test="${not empty error}">
            <p class="error-message">${error}</p>
        </c:if>

        <form action="/auth/login" method="post" class="game-form">

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="exemple@mail.com" required>
            </div>

            <div class="form-group">
                <label for="password">Mot de passe</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required>
            </div>

            <div class="form-actions">
                <a href="/listeJeux" class="back-link">← Annuler</a>
                <button type="submit" class="btn-submit">Se connecter</button>
            </div>

        </form>
    </div>
</main>

</body>
</html>
