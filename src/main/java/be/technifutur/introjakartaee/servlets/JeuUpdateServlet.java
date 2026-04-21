package be.technifutur.introjakartaee.servlets;

import be.technifutur.introjakartaee.dao.JeuxDAO;
import be.technifutur.introjakartaee.dao.JeuxPostgresDAO;
import be.technifutur.introjakartaee.enums.UserRole;
import be.technifutur.introjakartaee.models.Jeux;
import be.technifutur.introjakartaee.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/update")
public class JeuUpdateServlet extends HttpServlet {

    private final JeuxDAO dao = new JeuxPostgresDAO();

    private boolean isAdmin(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdmin(req)) { resp.sendRedirect(req.getContextPath() + "/listeJeux"); return; }
        String idParam = req.getParameter("id");

        Jeux jeuTrouve = null;
        if (idParam != null) {
            jeuTrouve = dao.findById(Integer.parseInt(idParam));
        }

        req.setAttribute("jeu", jeuTrouve);
        req.getRequestDispatcher("/pages/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdmin(req)) { resp.sendRedirect(req.getContextPath() + "/listeJeux"); return; }
        int id             = Integer.parseInt(req.getParameter("id"));
        String titre       = req.getParameter("titre");
        int annee          = Integer.parseInt(req.getParameter("annee"));
        String description = req.getParameter("description");
        String imageUrl    = req.getParameter("imageUrl");

        Jeux jeu = new Jeux(id, titre, annee, description, imageUrl);
        dao.update(jeu);

        resp.sendRedirect(req.getContextPath() + "/listeJeux");
    }
}
