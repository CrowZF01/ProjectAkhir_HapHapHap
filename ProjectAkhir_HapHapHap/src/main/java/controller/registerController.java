package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.UserService;

import java.io.IOException;

public class registerController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML Label statusLabel;

    @FXML
    public void handleDaftar(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            UserService.getInstance().register(username, password, confirmPassword);
            statusLabel.setText("Akun berhasil dibuat untuk: " + username);
            statusLabel.setStyle("-fx-text-fill: #E74C3C;");

            PauseTransition jeda = new PauseTransition(Duration.seconds(1.5));
            jeda.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    kembaliKeLogin();
                }
            });
            jeda.play();
        } catch (IllegalArgumentException e) {
            statusLabel.setText(e.getMessage());
            statusLabel.setStyle("-fx-text-fill: #E74C3C;");
        }
    }

    private void kembaliKeLogin(){
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/login.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleKembali(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/login.fxml")
        );
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
