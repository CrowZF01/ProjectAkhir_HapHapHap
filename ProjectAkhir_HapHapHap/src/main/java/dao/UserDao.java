package dao;

import model.User;

public interface UserDao {
    User validasiLogin(String username, String password);
    boolean registerUser(String username, String password);
}
