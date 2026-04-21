package be.technifutur.introjakartaee.servlets;

import be.technifutur.introjakartaee.dao.JeuxDAO;
import be.technifutur.introjakartaee.dao.JeuxPostgresDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/listeJeux")
public class ListeJeuxServlet extends HttpServlet {

    private final JeuxDAO dao = new JeuxPostgresDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listeJeux", dao.findAll());
        req.getRequestDispatcher("/pages/listeJeux.jsp").forward(req, resp);
    }
}
