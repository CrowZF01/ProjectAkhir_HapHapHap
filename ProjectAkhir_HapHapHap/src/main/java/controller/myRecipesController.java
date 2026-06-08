package controller;

import service.RecipeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Resep;
import util.sessionManager;

import java.util.List;

public class myRecipesController {

    @FXML private VBox myRecipesContainer;

    @FXML
    public void initialize() {
        loadDataMyRecipes();
    }

    public void loadDataMyRecipes() {
        myRecipesContainer.getChildren().clear();

        if (!sessionManager.isLogin()) return;

        int idUser = sessionManager.getUser().getId();
        List<Resep> listResep = RecipeService.getInstance().getResepByPembuat(idUser);

        if (listResep.isEmpty()) {
            Label kosong = new Label("Anda belum membuat resep apapun.");
            kosong.setStyle("-fx-text-fill: #888888; -fx-font-size: 15px;");
            myRecipesContainer.getChildren().add(kosong);
            return;
        }

        for (Resep resep : listResep) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/copy_Teletubies_haphaphap/itemRow.fxml"));
                HBox card = loader.load();
                itemRowController controller = loader.getController();

                // Kirim data resep, mode "MY_RECIPES", dan "this" (controller ini) agar bisa refresh saat diremove
                controller.setData(resep, "MY_RECIPES", this);

                myRecipesContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}