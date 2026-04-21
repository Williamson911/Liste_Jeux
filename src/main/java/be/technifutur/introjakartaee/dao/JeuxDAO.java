package be.technifutur.introjakartaee.dao;

import be.technifutur.introjakartaee.models.Jeux;

import java.util.List;

public interface JeuxDAO {
    List<Jeux> findAll();
    Jeux findById(int id);
    void save(Jeux jeux);
    void update(Jeux jeux);
    void delete(int id);
}