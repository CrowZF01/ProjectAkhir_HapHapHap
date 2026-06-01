package com.felix_71241153.app.projectakhir_haphaphap;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class mainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL lokasiFXML = getClass().getResource("login.fxml");

        System.out.println("Lokasi FXML: " + lokasiFXML);

        if (lokasiFXML == null) {
            System.out.println("login.fxml tidak ditemukan. Cek lokasi file di resources.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(lokasiFXML);
        Scene scene = new Scene(loader.load());

        stage.setTitle("HapHapHap");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}