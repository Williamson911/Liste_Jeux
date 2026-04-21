package be.technifutur.introjakartaee.servlets;

import be.technifutur.introjakartaee.dao.DatabaseConfig;
import be.technifutur.introjakartaee.models.Favori;
import be.technifutur.introjakartaee.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/favoris")
public class FavoriServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        List<Favori> favoris = new ArrayList<>();
        String sql = "SELECT * FROM favoris WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Favori f = new Favori();
                f.setId(rs.getLong("id"));
                f.setNomElement(rs.getString("nom_element"));
                f.setTypeElement(rs.getString("type_element"));
                f.setUserId(String.valueOf(rs.getInt("user_id")));
                favoris.add(f);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la récupération des favoris", e);
        }

        req.setAttribute("favoris", favoris);
        req.getRequestDispatcher("/pages/favoris.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String action = req.getParameter("action");

        if ("supprimer".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            String sql = "DELETE FROM favoris WHERE id = ? AND user_id = ?";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                ps.setInt(2, user.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ServletException("Erreur lors de la suppression du favori", e);
            }
        } else {
            String nomElement  = req.getParameter("nomElement");
            String typeElement = req.getParameter("typeElement");
            String sql = "INSERT INTO favoris (user_id, nom_element, type_element) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, user.getId());
                ps.setString(2, nomElement);
                ps.setString(3, typeElement);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ServletException("Erreur lors de l'ajout aux favoris", e);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/favoris");
    }
}
