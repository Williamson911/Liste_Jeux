package be.technifutur.introjakartaee.dao;

import be.technifutur.introjakartaee.models.User;

public interface UserDao {
    void save(User user);
    User finByEmail(String email);
}

