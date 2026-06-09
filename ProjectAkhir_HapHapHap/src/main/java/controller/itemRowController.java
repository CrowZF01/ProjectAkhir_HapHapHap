package controller;

import service.RecipeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Resep;
import util.imageUtil;
import util.sessionManager;

import java.io.File;
import java.util.Collections;
import java.util.Optional;

public class itemRowController {

    @FXML
    private ImageView fotoResep;
    @FXML
    private Label kategoriLabel, judulLabel, waktuLabel, porsiLabel, pedasLabel, deskripsiLabel;

    // Top-right controls
    @FXML
    private Label btnEkspor;
    @FXML
    private Label btnHapusFavorit;
    @FXML
    private Label iconHeart;
    @FXML
    private Label btnHapusResep;

    // Bottom controls
    @FXML
    private Label statusLabel;
    @FXML
    private Button btnPublish;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnApprove;
    @FXML
    private Button btnReject;

    private Resep resepAktif;
    private String viewMode;
    private Object parentController; // favoritController, myRecipesController, or moderasiAdminController

    public void setData(Resep resep, String viewMode, Object parent) {
        this.resepAktif = resep;
        this.viewMode = viewMode;
        this.parentController = parent;

        judulLabel.setText(resep.getJudul());
        if (resep.getJenisMakanan() != null) {
            kategoriLabel.setText(resep.getJenisMakanan().toUpperCase());
        } else {
            kategoriLabel.setText("UMUM");
        }
        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + " Menit");
        porsiLabel.setText("🍽 " + resep.getPorsiSajian() + " Porsi");
        pedasLabel.setText("🌶 Level " + resep.getTingkatKepedasan());

        String deskripsi = resep.getLangkahPembuatan();
        if (deskripsi != null && deskripsi.length() > 100) {
            deskripsi = deskripsi.substring(0, 100) + "...";
        }
        deskripsiLabel.setText(deskripsi);

        fotoResep.setImage(imageUtil.getImage(resep.getFoto()));

        // Reset visibility
        btnEkspor.setVisible(false);
        btnEkspor.setManaged(false);
        btnHapusFavorit.setVisible(false);
        btnHapusFavorit.setManaged(false);
        iconHeart.setVisible(false);
        iconHeart.setManaged(false);
        btnHapusResep.setVisible(false);
        btnHapusResep.setManaged(false);

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        btnPublish.setVisible(false);
        btnPublish.setManaged(false);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnApprove.setVisible(false);
        btnApprove.setManaged(false);
        btnReject.setVisible(false);
        btnReject.setManaged(false);

        if ("FAVORIT".equals(viewMode)) {
            btnEkspor.setVisible(true);
            btnEkspor.setManaged(true);
            btnHapusFavorit.setVisible(true);
            btnHapusFavorit.setManaged(true);
            iconHeart.setVisible(true);
            iconHeart.setManaged(true);
        } else if ("MY_RECIPES".equals(viewMode)) {
            btnHapusResep.setVisible(true);
            btnHapusResep.setManaged(true);
            statusLabel.setVisible(true);
            statusLabel.setManaged(true);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);

            String status = resep.getStatus();
            if ("DRAFT".equalsIgnoreCase(status)) {
                statusLabel.setText("Draft (Private)");
                btnPublish.setVisible(true);
                btnPublish.setManaged(true);
            } else if ("PENDING".equalsIgnoreCase(status)) {
                statusLabel.setText("Menunggu Persetujuan Admin");
            } else if ("PUBLISHED".equalsIgnoreCase(status)) {
                statusLabel.setText("Published (Public)");
            } else {
                statusLabel.setText(status);
            }
        } else if ("MODERASI".equals(viewMode)) {
            btnApprove.setVisible(true);
            btnApprove.setManaged(true);
            btnReject.setVisible(true);
            btnReject.setManaged(true);
        }
    }


    @FXML
    public void handleLihatResep(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/detail.fxml"));
            Parent root = loader.load();
            detailController controller = loader.getController();
            controller.setResepData(resepAktif);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHapusFavorit() {
        if (sessionManager.isLogin()) {
            RecipeService.getInstance().toggleFavorit(sessionManager.getUser().getId(), resepAktif.getIdResep(), true);
            if (parentController instanceof favoritController) {
                ((favoritController) parentController).loadDataFavorit();
            }
        }
    }

    @FXML
    public void handleEksporResep() {
        if (resepAktif == null) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Resep " + resepAktif.getJudul());
        String defaultFileName = resepAktif.getJudul().replaceAll(" ", "_") + ".txt";
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(judulLabel.getScene().getWindow());

        if (file != null) {
            RecipeService.getInstance().eksporKeTxt(Collections.singletonList(resepAktif), file);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ekspor Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Resep masakan berhasil diekspor");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleHapusResep() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Resep");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menghapus resep '" + resepAktif.getJudul() + "' secara permanen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean sukses = RecipeService.getInstance().hapusResepPermanen(resepAktif.getIdResep());
            if (sukses) {
                Alert suksesAlert = new Alert(Alert.AlertType.INFORMATION);
                suksesAlert.setTitle("Berhasil");
                suksesAlert.setHeaderText(null);
                suksesAlert.setContentText("Resep Anda berhasil dihapus.");
                suksesAlert.showAndWait();

                if (parentController instanceof myRecipesController) {
                    ((myRecipesController) parentController).loadDataMyRecipes();
                }
            } else {
                Alert gagalAlert = new Alert(Alert.AlertType.ERROR);
                gagalAlert.setTitle("Gagal");
                gagalAlert.setHeaderText(null);
                gagalAlert.setContentText("Gagal menghapus resep.");
                gagalAlert.showAndWait();
            }
        }
    }

    @FXML
    public void handlePublish() {
        boolean sukses = RecipeService.getInstance().updateResepStatus(resepAktif.getIdResep(), "PENDING");
        if (sukses && parentController instanceof myRecipesController) {
            ((myRecipesController) parentController).loadDataMyRecipes();
        }
    }

    @FXML
    public void handleEdit(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/add.fxml"));
            Parent root = loader.load();

            addResepController controller = loader.getController();
            controller.setResepUntukDiedit(resepAktif);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleApprove() {
        boolean sukses = RecipeService.getInstance().updateResepStatus(resepAktif.getIdResep(), "PUBLISHED");
        if (sukses && parentController instanceof moderasiAdminController) {
            ((moderasiAdminController) parentController).loadDataPendingRecipes();
        }
    }

    @FXML
    public void handleReject() {
        boolean sukses = RecipeService.getInstance().hapusResepPermanen(resepAktif.getIdResep());
        if (sukses && parentController instanceof moderasiAdminController) {
            ((moderasiAdminController) parentController).loadDataPendingRecipes();
        }
    }
}