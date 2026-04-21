package be.technifutur.introjakartaee.servlets;

import be.technifutur.introjakartaee.dao.JeuxDAO;
import be.technifutur.introjakartaee.dao.JeuxPostgresDAO;
import be.technifutur.introjakartaee.models.Jeux;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete")
public class JeuDeleteServlet extends HttpServlet {

    private final JeuxDAO dao = new JeuxPostgresDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        Jeux jeuTrouve = null;
        if (idParam != null) {
            jeuTrouve = dao.findById(Integer.parseInt(idParam));
        }

        req.setAttribute("jeu", jeuTrouve);
        req.getRequestDispatcher("/pages/delete.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        dao.delete(id);

        resp.sendRedirect(req.getContextPath() + "/listeJeux");
    }
}
