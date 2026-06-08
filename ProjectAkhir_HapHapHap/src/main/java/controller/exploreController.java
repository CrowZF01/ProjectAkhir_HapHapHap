package controller;

import service.RecipeService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Resep;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class exploreController {

    @FXML private TextField inputBahanField;
    @FXML private FlowPane tagContainer;
    @FXML private FlowPane resepContainer;
    @FXML private TextField searchField;

    private final RecipeService db = RecipeService.getInstance();
    private final List<String> listBahanTerpilih = new ArrayList<>();
    private final List<Resep> masterData = new ArrayList<>();

    private String kategoriAktif = "Semua";

    @FXML
    public void initialize() {
        masterData.clear();
        masterData.addAll(db.getAllResep());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            terapkanSemuaFilter();
        });
    }

    // Dipanggil oleh homeController untuk merubah tampilan berdasarkan sidebar
    public void setKategori(String kategori) {
        this.kategoriAktif = kategori;
        terapkanSemuaFilter();
    }

    @FXML
    public void handleCariResep() {
        terapkanSemuaFilter();
    }

    @FXML
    public void handleTambahBahan() {
        String bahan = inputBahanField.getText().trim();
        if (!bahan.isEmpty() && !containsIgnoreCase(listBahanTerpilih, bahan)) {
            listBahanTerpilih.add(bahan);
            inputBahanField.clear();
            renderTags();
        }
    }

    private void renderTags() {
        tagContainer.getChildren().clear();
        for (String bahan : listBahanTerpilih) {
            Label tag = new Label(bahan + "  ✕");
            tag.setStyle("-fx-background-color: #FBE2D1; -fx-text-fill: #555555; -fx-font-size: 11px; -fx-padding: 5 8; -fx-background-radius: 4; -fx-cursor: hand;");
            tag.setOnMouseClicked(event -> {
                listBahanTerpilih.remove(bahan);
                renderTags();
            });
            tagContainer.getChildren().add(tag);
        }
    }