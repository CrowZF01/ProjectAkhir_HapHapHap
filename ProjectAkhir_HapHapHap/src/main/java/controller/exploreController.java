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

    @FXML
    private TextField inputBahanField;
    @FXML
    private FlowPane tagContainer;
    @FXML
    private FlowPane resepContainer;
    @FXML
    private TextField searchField;

    private final RecipeService db = RecipeService.getInstance();
    private final List<String> listBahanTerpilih = new ArrayList<>();
    private final List<Resep> masterData = new ArrayList<>();
    private String kategoriAktif = "Semua";

    @FXML
    public void initialize() {
        masterData.clear();
        masterData.addAll(db.getAllResep());
        // Listener untuk textfield search agar bisa auto filter setiap keyup
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
            tag.setStyle(
                    "-fx-background-color: #FBE2D1; -fx-text-fill: #555555; -fx-font-size: 11px; -fx-padding: 5 8; -fx-background-radius: 4; -fx-cursor: hand;");
            tag.setOnMouseClicked(event -> {
                listBahanTerpilih.remove(bahan);
                renderTags();
            });
            tagContainer.getChildren().add(tag);
        }
    }

    @FXML
    public void handleTerapkanFilter() {
        terapkanSemuaFilter();
    }

    // Fungsi yang memfilter data resep berdasarkan keyword, kategori, dan bahan
    private void terapkanSemuaFilter() {
        String keyword = "";
        if (searchField.getText() != null) {
            keyword = searchField.getText().trim().toLowerCase(Locale.ROOT);
        }
        List<Resep> hasil = new ArrayList<>();
        for (Resep resep : masterData) {
            if (cocokKeyword(resep, keyword) && cocokKategori(resep) && cocokBahan(resep)) {
                hasil.add(resep);
            }
        }
        tampilkanKeLayar(hasil);
    }

    private boolean cocokKeyword(Resep resep, String keyword) {
        if (keyword.isEmpty()) {
            return true;
        }
        String judul = "";
        if (resep.getJudul() != null) {
            judul = resep.getJudul().toLowerCase(Locale.ROOT);
        }
        return judul.contains(keyword);
    }

    private boolean cocokKategori(Resep resep) {
        if ("Semua".equalsIgnoreCase(kategoriAktif)) {
            return true;
        }
        String kategoriResep = resep.getJenisMakanan();
        if (kategoriResep == null) {
            return false;
        }
        if (kategoriResep.equalsIgnoreCase(kategoriAktif)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean cocokBahan(Resep resep) {
        if (listBahanTerpilih.isEmpty()) {
            return true;
        }
        String bahanResep = "";
        if (resep.getBahan() != null) {
            bahanResep = resep.getBahan().toLowerCase(Locale.ROOT);
        }
        for (String bahan : listBahanTerpilih) {
            String bahanYangDicari = bahan.toLowerCase(Locale.ROOT);
            if (!bahanResep.contains(bahanYangDicari)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsIgnoreCase(List<String> list, String value) {
        for (String item : list) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private void tampilkanKeLayar(List<Resep> daftarResep) {
        resepContainer.getChildren().clear();
        if (daftarResep == null || daftarResep.isEmpty()) {
            Label kosong = new Label("Tidak ada resep yang cocok.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");
            resepContainer.getChildren().add(kosong);
            return;
        }

        // tampilkan resep
        for (Resep resep : daftarResep) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/itemResep.fxml"));
                VBox card = loader.load();
                itemResepController controller = loader.getController(); // dapat controllerny
                controller.setData(resep, this); // kirim data resep dan controller explore agar bisa refresh data
                resepContainer.getChildren().add(card); // kartu visual fxml nya dimasukkan
            } catch (Exception e) {
                System.out.println("Gagal memuat itemResep.fxml");
                e.printStackTrace();
            }
        }
    }

    public void refreshData() {
        masterData.clear();
        masterData.addAll(db.getAllResep());
        terapkanSemuaFilter();
    }
}