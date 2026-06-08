package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.sessionManager;

import javafx.scene.control.Label;

public class homeController {

    private Node exploreView;
    private exploreController exploreCtrl;

    private Node favoritView;
    private favoritController favoritCtrl;

    private Node myRecipesView;
    private myRecipesController myRecipesCtrl;
    
    @FXML private StackPane contentArea;
    @FXML private Label menuSemua;
    @FXML private Label menuMakanan;
    @FXML private Label menuDessert;
    @FXML private Label menuMinuman;
    @FXML private Label menuFavorit;
    @FXML private Label menuMyRecipes;
    @FXML private Label menuModerasi;
    @FXML private javafx.scene.control.Button btnAddRecipe;

    @FXML
    public void initialize() {
        if (sessionManager.isLogin()) {
            model.User user = sessionManager.getUser();
            String role = user.getRole();

            if ("GUEST".equalsIgnoreCase(role)) {
                menuFavorit.setVisible(false);
                menuFavorit.setManaged(false);
                menuMyRecipes.setVisible(false);
                menuMyRecipes.setManaged(false);
                btnAddRecipe.setVisible(false);
                btnAddRecipe.setManaged(false);
                menuModerasi.setVisible(false);
                menuModerasi.setManaged(false);
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                menuModerasi.setVisible(true);
                menuModerasi.setManaged(true);
                menuFavorit.setVisible(true);
                menuFavorit.setManaged(true);
                menuMyRecipes.setVisible(false);
                menuMyRecipes.setManaged(false);
            } else {
                menuModerasi.setVisible(false);
                menuModerasi.setManaged(false);
            }
        } else {
            menuFavorit.setVisible(false);
            menuFavorit.setManaged(false);
            menuMyRecipes.setVisible(false);
            menuMyRecipes.setManaged(false);
            btnAddRecipe.setVisible(false);
            btnAddRecipe.setManaged(false);
            menuModerasi.setVisible(false);
            menuModerasi.setManaged(false);
        }

        bukaExplore("Semua");
        setAktif(menuSemua);
    }

    private void setAktif(Label activeLabel) {
        Label[] menus = {menuSemua, menuMakanan, menuDessert, menuMinuman, menuFavorit, menuMyRecipes, menuModerasi};
        for (Label menu : menus) {
            if (menu != null) {
                if (menu == activeLabel) {
                    menu.setStyle("-fx-cursor: hand; -fx-font-size: 13px; -fx-text-fill: #555555; -fx-font-weight: bold;");
                } else {
                    menu.setStyle("-fx-cursor: hand; -fx-font-size: 13px; -fx-text-fill: #555555;");
                }
            }
        }
    }

    // ====== FUNGSI SIDEBAR NAVIGASI ======
    @FXML
    public void kategoriSemua() {
        bukaExplore("Semua");
        setAktif(menuSemua);
    }

    @FXML
    public void kategoriMakanan() {
        bukaExplore("Makanan");
        setAktif(menuMakanan);
    }

    @FXML
    public void kategoriDessert() {
        bukaExplore("Dessert");
        setAktif(menuDessert);
    }

    @FXML
    public void kategoriMinuman() {
        bukaExplore("Minuman");
        setAktif(menuMinuman);
    }

    // Fungsi canggih untuk menyuntikkan (inject) halaman tanpa reload frame
    private void bukaExplore(String kategori) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/explore.fxml"));
            Node view = loader.load();

            // Ambil controllernya untuk menyetel filter secara langsung
            exploreController controller = loader.getController();
            controller.setKategori(kategori);

            // Ganti isi contentArea
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            System.out.println("Gagal memuat halaman Explore");
            e.printStackTrace();
        }
    }

    @FXML
    public void pindahFavorit() {
        try {
            // Jika view belum pernah diload, maka load dari FXML dan isi datanya
            if (favoritView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/favorit.fxml"));
                favoritView = loader.load();
                favoritCtrl = loader.getController();

                // Panggil loadData HANYA SEKALI di sini saat inisialisasi awal
                favoritCtrl.loadDataFavorit();
            }

            // Tampilkan view yang sudah di-cache tanpa me-load ulang data dari nol
            contentArea.getChildren().setAll(favoritView);
            setAktif(menuFavorit);

        } catch (Exception e) {
            System.out.println("Gagal memuat halaman Favorit");
            e.printStackTrace();
        }
    }

    // ====== NAVIGASI KELUAR FRAME ======
    @FXML
    public void handleAddRecipe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/add.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            sessionManager.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tambahkan method ini di bawah method pindahFavorit()
    @FXML
    public void pindahMyRecipes() {
        try {
            // Jika view belum pernah diload, maka load dari FXML dan isi datanya
            if (myRecipesView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/myRecipes.fxml"));
                myRecipesView = loader.load();
                myRecipesCtrl = loader.getController();

                // Panggil loadData HANYA SEKALI di sini untuk mencegah freeze saat klik bolak-balik
                myRecipesCtrl.loadDataMyRecipes();
            }

            // Tampilkan view yang sudah di-cache secara instan
            contentArea.getChildren().setAll(myRecipesView);
            setAktif(menuMyRecipes);

        } catch (Exception e) {
            System.out.println("Gagal memuat halaman My Recipes");
            e.printStackTrace();
        }
    }

    @FXML
    public void pindahModerasi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix_71241153/app/ProjectAkhir_HapHapHap/moderasiAdmin.fxml"));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
            setAktif(menuModerasi);
        } catch (Exception e) {
            System.out.println("Gagal memuat halaman Moderasi Admin");
            e.printStackTrace();
        }
    }
}