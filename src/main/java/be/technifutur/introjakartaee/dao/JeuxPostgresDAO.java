package be.technifutur.introjakartaee.dao;

import be.technifutur.introjakartaee.models.Jeux;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JeuxPostgresDAO implements JeuxDAO {

    @Override
    public List<Jeux> findAll() {
        String sql = "SELECT id, titre, annee, description, image_url, prix FROM jeux ORDER BY annee";
        List<Jeux> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll jeux", e);
        }
        return list;
    }

    @Override
    public Jeux findById(int id) {
        String sql = "SELECT id, titre, annee, description, image_url, prix FROM jeux WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById jeux", e);
        }
        return null;
    }

    @Override
    public void save(Jeux jeux) {
        String sql = "INSERT INTO jeux (titre, annee, description, image_url, prix) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jeux.getTitre());
            ps.setInt(2, jeux.getAnnee());
            ps.setString(3, jeux.getDescription());
            ps.setString(4, jeux.getImageUrl());
            ps.setDouble(5, jeux.getPrix());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur save jeux", e);
        }
    }

    @Override
    public void update(Jeux jeux) {
        String sql = "UPDATE jeux SET titre = ?, annee = ?, description = ?, image_url = ?, prix = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jeux.getTitre());
            ps.setInt(2, jeux.getAnnee());
            ps.setString(3, jeux.getDescription());
            ps.setString(4, jeux.getImageUrl());
            ps.setDouble(5, jeux.getPrix());
            ps.setInt(6, jeux.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update jeux", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM jeux WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete jeux", e);
        }
    }

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
}
