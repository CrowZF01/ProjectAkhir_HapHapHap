package service;

import dao.UserDao;
import database.userDB;
import model.User;
import util.sessionManager;

public class UserService {

    private static UserService instance;
    private final UserDao userDao;

    // hubungkan ke DAO
    private UserService() {
        this.userDao = userDB.getInstance(); // konek ke singleton userDB
    }

    // akses instance
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username dan Password tidak boleh kosong!");
        }

        User user = userDao.validasiLogin(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Username atau Password salah!");
        }

        sessionManager.setUser(user);
    }

    public void register(String username, String password, String confirmPassword) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Semua field harus diisi");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Konfirmasi kata sandi anda tidak cocok");
        }

        boolean success = userDao.registerUser(username, password);
        if (!success) {
            throw new IllegalArgumentException("Gagal mendaftarkan akun");
        }
    }
}
