package util;

import model.User;

public class sessionManager {

    private static User userSaatIni;

    public static void setUser(User user) {
        userSaatIni = user;
    }

    public static User getUser() {
        return userSaatIni;
    }

    public static boolean isLogin() {
        return userSaatIni != null;
    }

    public static void logout() {
        userSaatIni = null;
    }
}