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
                String referer = req.getHeader("Referer");
                resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/listeJeux");
                return;
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
