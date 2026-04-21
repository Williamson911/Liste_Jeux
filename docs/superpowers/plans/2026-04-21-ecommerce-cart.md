# E-commerce Cart Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Ajouter un caddy d'achat temporaire (session HTTP) avec prix admin, page panier, et checkout simulé.

**Architecture:** Le caddy est un `Map<Integer, CartItem>` stocké dans `HttpSession`, accessible à tous les utilisateurs. `CartServlet` dispatche les actions `add/update/remove/checkout` via un paramètre POST. Pas de base de données pour le caddy — tout est en mémoire session.

**Tech Stack:** Jakarta EE 6, JSP / JSTL, PostgreSQL, Lombok, annotation `@WebServlet`

---

## Fichiers impactés

| Fichier | Action |
|---|---|
| `src/main/java/be/technifutur/introjakartaee/models/Jeux.java` | Modifier — ajout champ `prix` |
| `src/main/java/be/technifutur/introjakartaee/dao/JeuxPostgresDAO.java` | Modifier — inclure `prix` dans toutes les requêtes |
| `src/main/java/be/technifutur/introjakartaee/servlets/JeuCreateServlet.java` | Modifier — lire param `prix` |
| `src/main/java/be/technifutur/introjakartaee/servlets/JeuUpdateServlet.java` | Modifier — lire param `prix` |
| `src/main/java/be/technifutur/introjakartaee/models/CartItem.java` | Créer — POJO caddy |
| `src/main/java/be/technifutur/introjakartaee/servlets/CartServlet.java` | Créer — gestion du caddy |
| `src/main/webapp/pages/cart.jsp` | Créer — page caddy |
| `src/main/webapp/pages/create.jsp` | Modifier — champ prix |
| `src/main/webapp/pages/update.jsp` | Modifier — champ prix |
| `src/main/webapp/pages/listeJeux.jsp` | Modifier — prix + bouton caddy |
| `src/main/webapp/WEB-INF/fragments/navbar.jspf` | Modifier — lien caddy |

---

## Task 1 — Migration DB + champ `prix` sur `Jeux`

**Files:**
- Modify: `src/main/java/be/technifutur/introjakartaee/models/Jeux.java`

- [ ] **Step 1 : Appliquer la migration SQL**

Exécuter dans ta base PostgreSQL (via pgAdmin ou psql) :

```sql
ALTER TABLE jeux ADD COLUMN prix NUMERIC(10,2) DEFAULT 0.00;
```

- [ ] **Step 2 : Ajouter le champ `prix` dans `Jeux.java`**

Remplacer le contenu de `Jeux.java` par :

```java
package be.technifutur.introjakartaee.models;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Jeux {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String titre;
    @Getter @Setter
    private int annee;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String imageUrl;
    @Getter @Setter
    private double prix;
}
```

- [ ] **Step 3 : Vérifier que le projet compile**

Dans ton IDE (IntelliJ / Eclipse), lance un build. Attendu : erreurs de compilation sur `JeuxPostgresDAO` (constructeur `Jeux` manque le paramètre `prix`) — c'est normal, on les corrige à la task suivante.

- [ ] **Step 4 : Commit**

```bash
git add src/main/java/be/technifutur/introjakartaee/models/Jeux.java
git commit -m "feat: add prix field to Jeux model"
```

---

## Task 2 — Mettre à jour `JeuxPostgresDAO`

**Files:**
- Modify: `src/main/java/be/technifutur/introjakartaee/dao/JeuxPostgresDAO.java`

- [ ] **Step 1 : Mettre à jour `findAll`**

Remplacer la ligne :
```java
String sql = "SELECT id, titre, annee, description, image_url FROM jeux ORDER BY annee";
```
par :
```java
String sql = "SELECT id, titre, annee, description, image_url, prix FROM jeux ORDER BY annee";
```

- [ ] **Step 2 : Mettre à jour `findById`**

Remplacer la ligne :
```java
String sql = "SELECT id, titre, annee, description, image_url FROM jeux WHERE id = ?";
```
par :
```java
String sql = "SELECT id, titre, annee, description, image_url, prix FROM jeux WHERE id = ?";
```

- [ ] **Step 3 : Mettre à jour `save`**

Remplacer :
```java
String sql = "INSERT INTO jeux (titre, annee, description, image_url) VALUES (?, ?, ?, ?)";
```
par :
```java
String sql = "INSERT INTO jeux (titre, annee, description, image_url, prix) VALUES (?, ?, ?, ?, ?)";
```

Et dans le bloc try, après `ps.setString(4, jeux.getImageUrl());`, ajouter :
```java
ps.setDouble(5, jeux.getPrix());
```

- [ ] **Step 4 : Mettre à jour `update`**

Remplacer :
```java
String sql = "UPDATE jeux SET titre = ?, annee = ?, description = ?, image_url = ? WHERE id = ?";
```
par :
```java
String sql = "UPDATE jeux SET titre = ?, annee = ?, description = ?, image_url = ?, prix = ? WHERE id = ?";
```

Et dans le bloc try, après `ps.setString(4, jeux.getImageUrl());`, ajouter :
```java
ps.setDouble(5, jeux.getPrix());
ps.setInt(6, jeux.getId());
```
Et **supprimer** l'ancienne ligne `ps.setInt(5, jeux.getId());`.

- [ ] **Step 5 : Mettre à jour `mapRow`**

Remplacer :
```java
private Jeux mapRow(ResultSet rs) throws SQLException {
    return new Jeux(
            rs.getInt("id"),
            rs.getString("titre"),
            rs.getInt("annee"),
            rs.getString("description"),
            rs.getString("image_url")
    );
}
```
par :
```java
private Jeux mapRow(ResultSet rs) throws SQLException {
    return new Jeux(
            rs.getInt("id"),
            rs.getString("titre"),
            rs.getInt("annee"),
            rs.getString("description"),
            rs.getString("image_url"),
            rs.getDouble("prix")
    );
}
```

- [ ] **Step 6 : Vérifier que le projet compile sans erreurs**

- [ ] **Step 7 : Commit**

```bash
git add src/main/java/be/technifutur/introjakartaee/dao/JeuxPostgresDAO.java
git commit -m "feat: include prix in all JeuxPostgresDAO queries"
```

---

## Task 3 — Formulaires admin : champ `prix`

**Files:**
- Modify: `src/main/java/be/technifutur/introjakartaee/servlets/JeuCreateServlet.java`
- Modify: `src/main/java/be/technifutur/introjakartaee/servlets/JeuUpdateServlet.java`
- Modify: `src/main/webapp/pages/create.jsp`
- Modify: `src/main/webapp/pages/update.jsp`

- [ ] **Step 1 : Ajouter le champ prix dans `create.jsp`**

Après le bloc `form-group` de `imageUrl` et avant `form-actions`, insérer :

```html
<div class="form-group">
    <label for="prix">Prix (€)</label>
    <input type="number" id="prix" name="prix" placeholder="Ex : 59.99" min="0" step="0.01" required>
</div>
```

- [ ] **Step 2 : Lire `prix` dans `JeuCreateServlet.doPost`**

Dans `doPost`, après la ligne `String imageUrl = req.getParameter("imageUrl");`, ajouter :
```java
double prix = Double.parseDouble(req.getParameter("prix"));
```

Remplacer :
```java
Jeux jeu = new Jeux(0, titre, annee, description, imageUrl);
```
par :
```java
Jeux jeu = new Jeux(0, titre, annee, description, imageUrl, prix);
```

- [ ] **Step 3 : Ajouter le champ prix dans `update.jsp`**

Après le bloc `form-group` de `imageUrl` et avant `form-actions`, insérer :

```html
<div class="form-group">
    <label for="prix">Prix (€)</label>
    <input type="number" id="prix" name="prix" value="${jeu.prix}" min="0" step="0.01" required>
</div>
```

- [ ] **Step 4 : Lire `prix` dans `JeuUpdateServlet.doPost`**

Dans `doPost`, après la ligne `String imageUrl = req.getParameter("imageUrl");`, ajouter :
```java
double prix = Double.parseDouble(req.getParameter("prix"));
```

Remplacer :
```java
Jeux jeu = new Jeux(id, titre, annee, description, imageUrl);
```
par :
```java
Jeux jeu = new Jeux(id, titre, annee, description, imageUrl, prix);
```

- [ ] **Step 5 : Tester manuellement**

- Déployer l'application
- Connecte-toi en admin
- Crée un jeu avec un prix (ex: 59.99) → vérifie dans la DB que la colonne `prix` est remplie
- Modifie un jeu et change le prix → vérifie en DB

- [ ] **Step 6 : Commit**

```bash
git add src/main/java/be/technifutur/introjakartaee/servlets/JeuCreateServlet.java
git add src/main/java/be/technifutur/introjakartaee/servlets/JeuUpdateServlet.java
git add src/main/webapp/pages/create.jsp
git add src/main/webapp/pages/update.jsp
git commit -m "feat: add prix field to admin create/update forms"
```

---

## Task 4 — Créer `CartItem`

**Files:**
- Create: `src/main/java/be/technifutur/introjakartaee/models/CartItem.java`

- [ ] **Step 1 : Créer `CartItem.java`**

```java
package be.technifutur.introjakartaee.models;

public class CartItem {
    private Jeux jeu;
    private int quantite;

    public CartItem(Jeux jeu, int quantite) {
        this.jeu = jeu;
        this.quantite = quantite;
    }

    public Jeux getJeu() { return jeu; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getSousTotal() {
        return jeu.getPrix() * quantite;
    }
}
```

- [ ] **Step 2 : Commit**

```bash
git add src/main/java/be/technifutur/introjakartaee/models/CartItem.java
git commit -m "feat: add CartItem model"
```

---

## Task 5 — Créer `CartServlet`

**Files:**
- Create: `src/main/java/be/technifutur/introjakartaee/servlets/CartServlet.java`

- [ ] **Step 1 : Créer `CartServlet.java`**

```java
package be.technifutur.introjakartaee.servlets;

import be.technifutur.introjakartaee.dao.JeuxDAO;
import be.technifutur.introjakartaee.dao.JeuxPostgresDAO;
import be.technifutur.introjakartaee.models.CartItem;
import be.technifutur.introjakartaee.models.Jeux;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final JeuxDAO dao = new JeuxPostgresDAO();

    @SuppressWarnings("unchecked")
    private Map<Integer, CartItem> getCart(HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Integer, CartItem> cart = getCart(req.getSession());
        double total = cart.values().stream().mapToDouble(CartItem::getSousTotal).sum();
        req.setAttribute("cart", cart);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/pages/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Map<Integer, CartItem> cart = getCart(req.getSession());

        switch (action) {
            case "add" -> {
                int jeuId = Integer.parseInt(req.getParameter("jeuId"));
                Jeux jeu = dao.findById(jeuId);
                if (jeu != null) {
                    cart.merge(jeuId, new CartItem(jeu, 1),
                            (existing, newItem) -> { existing.setQuantite(existing.getQuantite() + 1); return existing; });
                }
            }
            case "update" -> {
                int jeuId = Integer.parseInt(req.getParameter("jeuId"));
                int quantite = Integer.parseInt(req.getParameter("quantite"));
                if (quantite <= 0) {
                    cart.remove(jeuId);
                } else if (cart.containsKey(jeuId)) {
                    cart.get(jeuId).setQuantite(quantite);
                }
            }
            case "remove" -> {
                int jeuId = Integer.parseInt(req.getParameter("jeuId"));
                cart.remove(jeuId);
            }
            case "checkout" -> {
                cart.clear();
                resp.sendRedirect(req.getContextPath() + "/cart?success=true");
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
```

- [ ] **Step 2 : Vérifier que le projet compile**

- [ ] **Step 3 : Commit**

```bash
git add src/main/java/be/technifutur/introjakartaee/servlets/CartServlet.java
git commit -m "feat: add CartServlet with add/update/remove/checkout actions"
```

---

## Task 6 — Créer `cart.jsp`

**Files:**
- Create: `src/main/webapp/pages/cart.jsp`

- [ ] **Step 1 : Créer `cart.jsp`**

```jsp
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
```

- [ ] **Step 2 : Commit**

```bash
git add src/main/webapp/pages/cart.jsp
git commit -m "feat: add cart.jsp page"
```

---

## Task 7 — Mettre à jour `listeJeux.jsp`

**Files:**
- Modify: `src/main/webapp/pages/listeJeux.jsp`

- [ ] **Step 1 : Ajouter le prix et le bouton caddy pour chaque jeu**

Dans la boucle `<c:forEach>`, après `<span class="game-year">${jeu.annee}</span>`, ajouter :

```jsp
<span class="game-price"><fmt:formatNumber value="${jeu.prix}" type="currency" currencySymbol="€" maxFractionDigits="2"/></span>
<form method="post" action="/cart">
    <input type="hidden" name="action" value="add">
    <input type="hidden" name="jeuId" value="${jeu.id}">
    <button type="submit" class="btn-cart">+ Caddy</button>
</form>
```

- [ ] **Step 2 : Ajouter la directive `fmt` en haut du fichier**

Après `<%@ taglib prefix="c" uri="jakarta.tags.core" %>`, ajouter :
```jsp
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
```

- [ ] **Step 3 : Tester manuellement**

- Déployer et aller sur `/listeJeux`
- Vérifier que le prix s'affiche et que le bouton "+ Caddy" est visible
- Cliquer sur "+ Caddy" pour un jeu → vérifie que tu es redirigé et que le compteur dans la navbar augmente

- [ ] **Step 4 : Commit**

```bash
git add src/main/webapp/pages/listeJeux.jsp
git commit -m "feat: show prix and add-to-cart button on game list"
```

---

## Task 8 — Mettre à jour `navbar.jspf`

**Files:**
- Modify: `src/main/webapp/WEB-INF/fragments/navbar.jspf`

- [ ] **Step 1 : Ajouter le lien caddy dans la navbar**

Dans `navbar.jspf`, dans la balise `<nav class="nav-auth">`, ajouter avant le bloc `<c:choose>` :

```jsp
<a href="/cart" class="btn-nav-outline">
    🛒 Caddy
    <c:if test="${not empty sessionScope.cart && sessionScope.cart.size() > 0}">
        (${sessionScope.cart.size()})
    </c:if>
</a>
```

- [ ] **Step 2 : Tester manuellement**

- Aller sur `/listeJeux`, vérifier que "🛒 Caddy" apparaît dans la navbar
- Ajouter un jeu au caddy → le compteur doit passer à "(1)"
- Cliquer sur "🛒 Caddy" → doit ouvrir `/cart` avec la liste des jeux

- [ ] **Step 3 : Commit**

```bash
git add src/main/webapp/WEB-INF/fragments/navbar.jspf
git commit -m "feat: add cart link with item count to navbar"
```

---

## Task 9 — CSS pour les éléments caddy

**Files:**
- Modify: `src/main/webapp/styles/style.css`

- [ ] **Step 1 : Ajouter les styles caddy à la fin de `style.css`**

```css
/* ===== CART ===== */
.btn-cart {
    background: transparent;
    border: 1px solid var(--accent, #8b1a1a);
    color: var(--accent, #8b1a1a);
    padding: 0.3rem 0.8rem;
    font-family: 'Oswald', sans-serif;
    font-size: 0.75rem;
    letter-spacing: 0.05em;
    cursor: pointer;
    transition: background 0.2s, color 0.2s;
}

.btn-cart:hover {
    background: var(--accent, #8b1a1a);
    color: #fff;
}

.game-price {
    font-size: 0.85rem;
    color: #aaa;
    margin-top: 0.2rem;
}

.cart-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 2rem;
    color: #ccc;
}

.cart-table th,
.cart-table td {
    padding: 0.9rem 1.2rem;
    border-bottom: 1px solid rgba(255,255,255,0.08);
    text-align: left;
}

.cart-table th {
    font-family: 'Oswald', sans-serif;
    font-size: 0.8rem;
    letter-spacing: 0.1em;
    text-transform: uppercase;
    color: #888;
}

.cart-game-cell {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.cart-qty-input {
    width: 60px;
    background: transparent;
    border: 1px solid rgba(255,255,255,0.2);
    color: #ccc;
    padding: 0.3rem 0.5rem;
    font-size: 0.9rem;
    text-align: center;
}

.cart-qty-form {
    display: inline;
}

.cart-total-label {
    font-family: 'Oswald', sans-serif;
    text-align: right;
    color: #888;
    font-size: 0.9rem;
    letter-spacing: 0.05em;
}

.cart-total-value {
    font-family: 'Oswald', sans-serif;
    font-size: 1.2rem;
    color: #fff;
}

.cart-actions {
    margin-top: 2rem;
    display: flex;
    justify-content: flex-end;
}

.cart-empty {
    color: #888;
    text-align: center;
    margin-top: 4rem;
    font-size: 1rem;
}

.cart-success {
    color: #6abf69;
    text-align: center;
    margin-top: 1rem;
    font-family: 'Oswald', sans-serif;
    font-size: 1rem;
    letter-spacing: 0.05em;
}
```

- [ ] **Step 2 : Tester le rendu final**

- Déployer et tester le parcours complet :
  1. Aller sur `/listeJeux` → les prix et boutons "+ Caddy" s'affichent correctement
  2. Ajouter plusieurs jeux → le compteur dans la navbar s'incrémente
  3. Aller sur `/cart` → tableau avec jeux, prix, quantités, total
  4. Modifier une quantité → le sous-total et total se mettent à jour
  5. Cliquer "Retirer" → le jeu disparaît du caddy
  6. Cliquer "Commander" → caddy vidé + message "Commande simulée avec succès !"
  7. Tester sans être connecté → tout fonctionne pareil

- [ ] **Step 3 : Commit final**

```bash
git add src/main/webapp/styles/style.css
git commit -m "feat: add cart styles"
```
