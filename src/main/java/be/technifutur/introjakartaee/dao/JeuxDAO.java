package be.technifutur.introjakartaee.dao;

import be.technifutur.introjakartaee.servlets.Jeux;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeuxDAO {

    public List<Jeux> findAll() {
        List<Jeux> liste = new ArrayList<>();
        String sql = "SELECT id, titre, annee, description, image_url FROM jeux ORDER BY annee";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll", e);
        }
        return liste;
    }

    public Jeux findById(int id) {
        String sql = "SELECT id, titre, annee, description, image_url FROM jeux WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById", e);
        }
        return null;
    }

    public void create(Jeux jeu) {
        String sql = "INSERT INTO jeux (titre, annee, description, image_url) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jeu.getTitre());
            ps.setInt(2, jeu.getAnnee());
            ps.setString(3, jeu.getDescription());
            ps.setString(4, jeu.getImageUrl());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur create", e);
        }
    }

    public void update(Jeux jeu) {
        String sql = "UPDATE jeux SET titre = ?, annee = ?, description = ?, image_url = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jeu.getTitre());
            ps.setInt(2, jeu.getAnnee());
            ps.setString(3, jeu.getDescription());
            ps.setString(4, jeu.getImageUrl());
            ps.setInt(5, jeu.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM jeux WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete", e);
        }
    }

    private Jeux mapRow(ResultSet rs) throws SQLException {
        return new Jeux(
                rs.getInt("id"),
                rs.getString("titre"),
                rs.getInt("annee"),
                rs.getString("description"),
                rs.getString("image_url")
        );
    }
}
