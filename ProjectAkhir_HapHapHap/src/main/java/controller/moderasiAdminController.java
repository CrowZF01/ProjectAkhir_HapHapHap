package controller;

import service.RecipeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Resep;

import java.util.List;

public class moderasiAdminController {

    @FXML private VBox pendingRecipesContainer;

    @FXML
    public void initialize() {
        loadDataPendingRecipes();
    }

    public void loadDataPendingRecipes() {
        pendingRecipesContainer.getChildren().clear();

        List<Resep> listResep = RecipeService.getInstance().getPendingResep();

        if (listResep.isEmpty()) {
            Label kosong = new Label("Tidak ada resep yang menunggu persetujuan.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 15px;");
            pendingRecipesContainer.getChildren().add(kosong);
            return;
        }

        for (Resep resep : listResep) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/itemRow.fxml"));
                HBox card = loader.load();
                itemRowController controller = loader.getController();

                // Kirim data resep, mode "MODERASI", dan parent controller agar bisa refresh saat disetujui/ditolak
                controller.setData(resep, "MODERASI", this);

                pendingRecipesContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
