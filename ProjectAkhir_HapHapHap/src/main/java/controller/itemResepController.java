package controller;

import service.RecipeService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Resep;
import util.imageUtil;
import util.sessionManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class itemResepController {

    @FXML
    private VBox cardUtama;

    @FXML
    private Label judulLabel, kategoriLabel, bahanLabel, waktuLabel, porsiLabel;

    @FXML
    private ImageView fotoResep;

    @FXML
    private Label ikonHati;

    @FXML
    private javafx.scene.control.Button btnEditAdmin;

    @FXML
    private javafx.scene.control.Button btnHapusAdmin;

    private Resep resepAktif;
    private exploreController parentController;


    public void setData(Resep resep) {
        this.resepAktif = resep;

        judulLabel.setText(resep.getJudul());

        String kategori = resep.getJenisMakanan();
        if (kategori != null) {
            kategoriLabel.setText(kategori.toUpperCase());
        } else {
            kategoriLabel.setText("UMUM");
        }

        if (resep.getBahan() != null) {
            bahanLabel.setText(resep.getBahan());
        } else {
            bahanLabel.setText("");
        }

        waktuLabel.setText("⏱ " + resep.getEstimasiWaktu() + "m");
        porsiLabel.setText("🍽 " + resep.getPorsiSajian() + " Porsi");

        Image img = imageUtil.getImage(resep.getFoto());
        if (img != null) {
            fotoResep.setImage(img);
        } else {
            fotoResep.setImage(null);
        }

        // Mengatur kursor dan event klik untuk card resep
        cardUtama.setCursor(Cursor.HAND);

        cardUtama.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleLihatDetail(event);
            }
        });

        if (sessionManager.isLogin() && ikonHati != null) {
            int idUser = sessionManager.getUser().getId();
            boolean isFavorit = RecipeService.getInstance().cekFavorit(idUser, resep.getIdResep());

            if (isFavorit) {
                ikonHati.setText("♥");
                ikonHati.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 18px;");
            } else {
                ikonHati.setText("♡");
                ikonHati.setStyle("-fx-text-fill: #333333; -fx-font-size: 18px;");
            }
        }

        // Tampilkan tombol edit & hapus hanya jika user aktif adalah ADMIN
        if (sessionManager.isLogin() && "ADMIN".equalsIgnoreCase(sessionManager.getUser().getRole())) {
            btnEditAdmin.setVisible(true);
            btnEditAdmin.setManaged(true);
            if (btnHapusAdmin != null) {
                btnHapusAdmin.setVisible(true);
                btnHapusAdmin.setManaged(true);
            }
        } else {
            btnEditAdmin.setVisible(false);
            btnEditAdmin.setManaged(false);
            if (btnHapusAdmin != null) {
                btnHapusAdmin.setVisible(false);
                btnHapusAdmin.setManaged(false);
            }
        }
    }

    public void handleLihatDetail(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/detail.fxml")
            );

            Parent root = loader.load();

            detailController controller = loader.getController();
            controller.setResepData(resepAktif);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("Gagal membuka halaman detail!");
            e.printStackTrace();
        }
    }

}