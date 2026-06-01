package controller;

import service.RecipeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Resep;
import util.sessionManager;
import java.io.File;
import java.util.List;

public class favoritController {

    @FXML private VBox favoritContainer;
    @FXML private Button btnEksporSemua;

    private List<Resep> listFavorit;

    @FXML
    public void initialize() {
        loadDataFavorit();
    }

    public void loadDataFavorit() {
        favoritContainer.getChildren().clear();

        if (!sessionManager.isLogin()) {
            if (btnEksporSemua != null) {
                btnEksporSemua.setDisable(true);
            }
            return;
        }

        int idUser = sessionManager.getUser().getId();
        listFavorit = RecipeService.getInstance().getFavoritByUser(idUser);

        if (listFavorit.isEmpty()) {
            if (btnEksporSemua != null) {
                btnEksporSemua.setDisable(true);
            }
            Label kosong = new Label("Belum ada resep yang difavoritkan.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 15px;");
            favoritContainer.getChildren().add(kosong);
            return;
        }

        if (btnEksporSemua != null) {
            btnEksporSemua.setDisable(false);
        }

        for (Resep resep : listFavorit) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/itemRow.fxml"));
                HBox card = loader.load();
                itemRowController controller = loader.getController();

                // Kirim data resep, mode "FAVORIT", dan controller ini
                controller.setData(resep, "FAVORIT", this);
                favoritContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleEksporSemua() {
        if (listFavorit == null || listFavorit.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Tidak ada resep favorit untuk diekspor!");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ekspor Semua Resep Favorit");
        fileChooser.setInitialFileName("Semua_Resep_Favorit.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(favoritContainer.getScene().getWindow());

        if (file != null) {
            RecipeService.getInstance().eksporKeTxt(listFavorit, file);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ekspor Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Semua resep favorit berhasil diekspor ke dalam satu file!");
            alert.showAndWait();
        }
    }
}