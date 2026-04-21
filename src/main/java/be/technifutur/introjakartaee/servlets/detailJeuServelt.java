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

@WebServlet("/detail")
public class detailJeuServelt extends HttpServlet {

    private final JeuxDAO dao = new JeuxPostgresDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        Jeux jeuTrouve = null;
        if (idParam != null) {
            jeuTrouve = dao.findById(Integer.parseInt(idParam));
        }

        req.setAttribute("jeu", jeuTrouve);
        req.getRequestDispatcher("/pages/detail.jsp").forward(req, resp);
    }
}
