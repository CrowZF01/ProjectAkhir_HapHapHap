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
}