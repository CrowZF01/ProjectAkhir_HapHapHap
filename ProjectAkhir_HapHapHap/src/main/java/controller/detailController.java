package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Bahan;
import model.Resep;
import service.RecipeService;
import util.imageUtil;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Collections;
import javafx.scene.control.Alert;

import java.util.List;

public class detailController {

    @FXML
    private VBox bahanContainer;

    @FXML
    private ImageView fotoResepDetail;

    @FXML
    private Label judulResep;

    @FXML
    private Label kategoriLabel;

    @FXML
    private VBox langkahContainer;

    @FXML
    private Label pedasLabel;

    @FXML
    private Label placeholderIcon;

    @FXML
    private Label waktuLabel;
    @FXML private Label porsiLabel;

    @FXML private Button btnFavorit;
    private Resep resepAktif;
    private boolean isFavorit = false;

    public void setResepData(Resep resep){
        this.resepAktif = resep;
        judulResep.setText(resep.getJudul());
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");
        pedasLabel.setText("🌶 Level " + resep.getTingkatKepedasan());
        kategoriLabel.setText(resep.getJenisMakanan().toUpperCase());
        porsiLabel.setText("🍽 " + resep.getPorsiSajian() + " Porsi");

        // Render langkah memasak secara dinamis
        langkahContainer.getChildren().clear();
        if (resep.getLangkahPembuatan() != null && !resep.getLangkahPembuatan().isEmpty()) {
            String[] steps = resep.getLangkahPembuatan().split("\\r?\\n");
            int stepNum = 1;
            for (String stepText : steps) {
                if (stepText.trim().isEmpty()) {
                    continue;
                }

                // Bersihkan angka prefix (seperti "1. ")
                String cleanText = stepText.trim().replaceAll("^\\d+([\\.\\)\\s]+|\\s+)", "");
                if (cleanText.trim().isEmpty()) {
                    continue;
                }

                HBox row = new HBox(20);
                row.setAlignment(javafx.geometry.Pos.TOP_LEFT);

                // Angka langkah besar berwarna cokelat muda
                Label stepNumber = new Label(String.valueOf(stepNum));
                stepNumber.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #EBD6C8; -fx-min-width: 35px; -fx-alignment: center-right; -fx-translate-y: -8;");

                VBox textBox = new VBox(5);
                Label descLabel = new Label(cleanText);
                descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444444; -fx-line-spacing: 4px;");
                descLabel.setWrapText(true);
                HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);

                textBox.getChildren().add(descLabel);
                row.getChildren().addAll(stepNumber, textBox);
                langkahContainer.getChildren().add(row);

                stepNum++;
            }
        } else {
            Label kosong = new Label("Langkah memasak tidak tersedia.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-font-style: italic;");
            langkahContainer.getChildren().add(kosong);
        }

        if (util.sessionManager.isLogin()) {
            int idUser = util.sessionManager.getUser().getId();
            isFavorit = RecipeService.getInstance().cekFavorit(idUser, resep.getIdResep());
            renderTombolFavorit(); // Ubah tampilan tombol sesuai status
        }

        try {
            Image img = imageUtil.getImage(resep.getFoto());

            if (img != null) {
                fotoResepDetail.setImage(img);
                placeholderIcon.setVisible(false);
            } else {
                fotoResepDetail.setImage(null);
                placeholderIcon.setVisible(true);
            }

        } catch (Exception e) {
            System.out.println("Gambar tidak ditemukan");
            fotoResepDetail.setImage(null);
            placeholderIcon.setVisible(true);
        }
        loadBahan(resep.getIdResep());
    }

    private void loadBahan(int idResep) {
        bahanContainer.getChildren().clear();
        List<Bahan> listBahan = RecipeService.getInstance().getBahanByResep(idResep);
        if (listBahan.isEmpty()){
            Label kosong = new Label("Daftar bahan tidak tersedia");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-font-style: italic;");
            bahanContainer.getChildren().add(kosong);
            return;
        }

        for (Bahan bahan : listBahan){
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            // Bullets warna cokelat hangat ●
            Label dot = new Label("●");
            dot.setStyle("-fx-text-fill: #A65021; -fx-font-size: 10px;");

            Label namaBahan = new Label(bahan.getNamaBahan());
            namaBahan.setStyle("-fx-text-fill: #444444; -fx-font-size: 14px; -fx-font-weight: bold;");
            namaBahan.setWrapText(true);
            HBox.setHgrow(namaBahan, javafx.scene.layout.Priority.ALWAYS);

            row.getChildren().addAll(dot, namaBahan);
            bahanContainer.getChildren().add(row);
        }
    }

    @FXML
    private void handleKembali() {
        try {
            Stage stage = (Stage) judulResep.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/home.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderTombolFavorit() {
        if (isFavorit) {
            btnFavorit.setText("♥ Hapus Favorit");
            // Warna merah kalau sudah favorit
            btnFavorit.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 12; -fx-cursor: hand;");
        } else {
            btnFavorit.setText("♡ Simpan Favorit");
            // Warna cokelat kalau belum
            btnFavorit.setStyle("-fx-background-color: #A65021; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 12; -fx-cursor: hand;");
        }
    }

    @FXML
    public void handleToggleFavorit() {
        if (!util.sessionManager.isLogin() || util.sessionManager.getUser().getUsername().equalsIgnoreCase("GUEST")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Akses Ditolak");
            alert.setHeaderText(null);
            alert.setContentText("Anda harus login terlebih dahulu untuk bisa menyimpan resep ke daftar favorit");
            alert.showAndWait();

            System.out.println("Akses ditolak: User GUEST mencoba menambah favorit.");

            return;
        }
        int idUser = util.sessionManager.getUser().getId();

        RecipeService.getInstance().toggleFavorit(idUser, resepAktif.getIdResep(), isFavorit);
        isFavorit = !isFavorit;
        renderTombolFavorit();
    }

    @FXML
    public void handleEksporResep() {
        if (this.resepAktif == null) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ekspor Resep ke TXT");

        String fileName = resepAktif.getJudul().replaceAll(" ", "_") + ".txt";
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(btnFavorit.getScene().getWindow());

        if (file != null) {
            RecipeService.getInstance().eksporKeTxt(Collections.singletonList(resepAktif), file);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ekspor Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Resep masakan berhasil diekspor");
            alert.showAndWait();
        }
    }

}