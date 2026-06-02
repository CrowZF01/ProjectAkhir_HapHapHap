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
        kategoriLabel.setText(resep.getJenisMakanan() != null ? resep.getJenisMakanan().toUpperCase() : "UMUM");
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
}