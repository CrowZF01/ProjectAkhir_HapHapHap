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
                    getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/detail.fxml"));

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

    @FXML
    public void handleEditAdmin(javafx.event.ActionEvent event) {
        // Mencegah klik menyebar ke card utama (memicu detail view)
        event.consume();
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/add.fxml"));
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

    public void setData(Resep resep, exploreController parent) {
        this.parentController = parent;
        setData(resep);
    }

    @FXML
    public void handleHapusAdmin(javafx.event.ActionEvent event) {
        event.consume(); // Mencegah klik menyebar ke card utama (memicu detail view)

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Resep");
        alert.setHeaderText(null);
        alert.setContentText(
                "Apakah Anda benar-benar ingin menghapus resep '" + resepAktif.getJudul() + "' secara permanen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean sukses = RecipeService.getInstance().hapusResepPermanen(resepAktif.getIdResep());
            if (sukses) {
                Alert suksesAlert = new Alert(Alert.AlertType.INFORMATION);
                suksesAlert.setTitle("Berhasil");
                suksesAlert.setHeaderText(null);
                suksesAlert.setContentText("Resep masakan berhasil dihapus.");
                suksesAlert.showAndWait();

                if (parentController != null) {
                    parentController.refreshData(); // Halaman memuat ulang data
                }
            } else {
                Alert gagalAlert = new Alert(Alert.AlertType.ERROR);
                gagalAlert.setTitle("Gagal");
                gagalAlert.setHeaderText(null);
                gagalAlert.setContentText("Gagal menghapus resep masakan.");
                gagalAlert.showAndWait();
            }
        }
    }
}