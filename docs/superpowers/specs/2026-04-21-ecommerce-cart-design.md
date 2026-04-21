# Design — Fonctionnalité E-commerce (Caddy)

**Date :** 2026-04-21  
**Projet :** FromSoftware — Catalogue (Jakarta EE)  
**Scope :** Ajout d'un caddy d'achat temporaire (session HTTP)

---

## Contexte

Le projet est une application Jakarta EE avec authentification, catalogue de jeux FromSoftware, et gestion admin (CRUD). L'objectif est d'ajouter une fonctionnalité e-commerce simple : un caddy en session, accessible à tous les utilisateurs (connectés ou non).

---

## Décisions clés

| Question | Décision |
|---|---|
| Persistance du caddy | Temporaire (session HTTP) |
| Accès au caddy | Tous les utilisateurs (connectés ou non) |
| Prix des jeux | Défini par l'admin dans le formulaire create/update |
| Page caddy | Liste + prix unitaire + quantité modifiable + total + retrait |
| Checkout | Simulation : vide le caddy + message de confirmation |

---

## Section 1 — Modèle & DAO

### `Jeux.java`
Ajout du champ `double prix` avec getter/setter Lombok.

### Base de données
Migration SQL à appliquer :
```sql
ALTER TABLE jeux ADD COLUMN prix NUMERIC(10,2) DEFAULT 0.00;
```

### `JeuxDAO` / `JeuxPostgresDAO`
Les méthodes `save` et `update` incluent le champ `prix` dans les requêtes SQL (`INSERT` et `UPDATE`). La méthode `findById` et `findAll` mappent aussi la colonne `prix`.

### `CartItem.java` (nouveau)
POJO mémoire uniquement — pas de DAO, pas de table.

```
CartItem
  - Jeux jeu
  - int quantite
  + double getSousTotal()  // jeu.getPrix() * quantite
```

---

## Section 2 — Servlet & Session

### `CartServlet.java` (nouveau)
- **URL mapping :** `/cart`
- **GET :** calcule le total, place le map et le total en attributs de requête, forward vers `cart.jsp`
- **POST :** dispatche selon le paramètre `action`

| Action | Comportement |
|---|---|
| `add` | Récupère `jeuId`, charge le jeu via `JeuxDAO.findById()`, ajoute ou incrémente dans le map |
| `update` | Met à jour la quantité ; si ≤ 0, retire l'item |
| `remove` | Supprime l'item du map |
| `checkout` | Vide le map, redirige vers `/cart` avec paramètre `success=true` |

### Gestion de la session
Le map `Map<Integer, CartItem>` est récupéré via `session.getAttribute("cart")`. S'il est `null`, un nouveau `HashMap` est créé et mis en session avec `session.setAttribute("cart", cart)`.

Le total est calculé dans le servlet (somme des sous-totaux de chaque `CartItem`) et transmis à la JSP via `request.setAttribute("total", total)`.

---

## Section 3 — UI

### Navbar (`navbar.jspf`)
Ajout d'un lien caddy visible pour tous :
```
🛒 Caddy (N)
```
où N = taille du map en session (`${sessionScope.cart.size()}`). Lien vers `/cart`.

### `listeJeux.jsp`
Pour chaque jeu dans la liste :
- Affichage du prix sous le titre
- Bouton "+ Caddy" (form POST vers `/cart?action=add` avec `jeuId` en hidden input), visible pour tous

### `cart.jsp` (nouvelle page)
Tableau avec pour chaque `CartItem` :
- Image miniature + titre du jeu
- Prix unitaire
- Input numérique pour la quantité (form POST vers `/cart?action=update` avec `jeuId`)
- Bouton "Retirer" (form POST vers `/cart?action=remove` avec `jeuId`)

Pied de page :
- Total général
- Bouton "Commander" (POST vers `/cart?action=checkout`)

États particuliers :
- Caddy vide → message "Votre caddy est vide."
- Après checkout → message "Commande simulée avec succès !"

### Formulaires admin (create/update)
Ajout d'un champ `prix` (input number, step 0.01) dans `create.jsp` et `update.jsp`.

---

## Fichiers impactés

| Fichier | Type |
|---|---|
| `models/Jeux.java` | Modifié |
| `dao/JeuxPostgresDAO.java` | Modifié |
| `models/CartItem.java` | Nouveau |
| `servlets/CartServlet.java` | Nouveau |
| `webapp/pages/cart.jsp` | Nouveau |
| `webapp/pages/create.jsp` | Modifié |
| `webapp/pages/update.jsp` | Modifié |
| `webapp/pages/listeJeux.jsp` | Modifié |
| `webapp/WEB-INF/fragments/navbar.jspf` | Modifié |
| `webapp/WEB-INF/web.xml` | Modifié (mapping `/cart`) |
