package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label; // <-- Pastikan ini Label milik javafx.scene.control
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.UserService;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    public void handleLogin() {
        if (statusLabel != null) {
            statusLabel.setText("");
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            UserService.getInstance().login(username, password);
            System.out.println("Login berhasil: " + username);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IllegalArgumentException e) {
            tampilkanError(e.getMessage());
        } catch (Exception e) {
            tampilkanError("Sistem Error: Gagal memuat halaman Home!");
            e.printStackTrace();
        }
    }

    @FXML
    public void pindahDaftar(){
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/daftar.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            System.out.println("Gagal memuat halaman Daftar");
            e.printStackTrace();
        }
    }

    private void tampilkanError(String pesan) {
        if (statusLabel != null) {
            statusLabel.setText(pesan);
            statusLabel.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 13px;");
        } else {
            System.out.println("ERROR LOGIN: " + pesan);
        }
    }

    @FXML
    public void handleGuestLogin() {
        try {
            util.sessionManager.setUser(new model.User(-1, "Guest", "", "GUEST"));
            System.out.println("Masuk sebagai Guest");

            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            tampilkanError("Sistem Error: Gagal masuk sebagai Guest!");
            e.printStackTrace();
        }
    }
}