package be.technifutur.introjakartaee.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/detail")
public class detailJeuServelt extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        List<Jeux> listeJeux = (List<Jeux>) getServletContext().getAttribute("listeJeux");

        Jeux jeuTrouve = null;
        if (idParam != null && listeJeux != null) {
            int id = Integer.parseInt(idParam);
            for (Jeux jeu : listeJeux) {
                if (jeu.getId() == id) {
                    jeuTrouve = jeu;
                    break;
                }
            }
        }

        req.setAttribute("jeu", jeuTrouve);
        req.getRequestDispatcher("/pages/detail.jsp").forward(req, resp);
    }
}
