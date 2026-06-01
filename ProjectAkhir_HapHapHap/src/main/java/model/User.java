package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role; // 'USER', 'ADMIN', or 'GUEST'

    public User() {
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = "USER";
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.role = "USER";
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}