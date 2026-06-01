package util;

import javafx.scene.image.Image;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class imageUtil {

    private static final Map<String, Image> CACHE = new HashMap<>();
    private static final String DEFAULT_IMAGE = "default.png";

    // Path yang benar menuju folder images milikmu
    private static final String RESOURCE_PATH = "/images/";

    public static Image getImage(String namaFile) {
        String pathFoto = null;

        if (namaFile != null && !namaFile.trim().isEmpty()) {
            // Bersihkan nama file dari slash kalau user tidak sengaja memasukkannya
            pathFoto = namaFile.replace("/", "");
        }

        if (pathFoto == null) {
            return getDefaultImage();
        }

        if (CACHE.containsKey(pathFoto)) {
            return CACHE.get(pathFoto);
        }

        try {
            Image img = null;

            // 1. Coba ambil pakai ClassLoader (Jalur Resmi JavaFX)
            String fullPath = RESOURCE_PATH + pathFoto;
            InputStream is = imageUtil.class.getResourceAsStream(fullPath);

            // Set resolusi aman untuk thumbnail list (misal 400x400)
            if (is != null) {
                img = new Image(is, 400, 400, true, true);
            }
            else {
                File fileTarget = new File("HapHapHap/target/classes" + fullPath);
                if(fileTarget.exists()){
                    img = new Image(fileTarget.toURI().toString(), 400, 400, true, true);
                }
            }

            if (img != null) {
                CACHE.put(pathFoto, img);
                return img;
            } else {
                System.out.println("⚠️ Gambar tidak ditemukan di folder: " + fullPath);
                return getDefaultImage();
            }

        } catch (Exception e) {
            System.out.println("❌ Gagal load gambar: " + e.getMessage());
            return getDefaultImage();
        }
    }

    private static Image getDefaultImage() {
        if (CACHE.containsKey(DEFAULT_IMAGE)) {
            return CACHE.get(DEFAULT_IMAGE);
        }

        try {
            Image img = null;
            String fullPath = RESOURCE_PATH + DEFAULT_IMAGE;
            InputStream is = imageUtil.class.getResourceAsStream(fullPath);

            if (is != null) {
                img = new Image(is);
                CACHE.put(DEFAULT_IMAGE, img);
                return img;
            } else {
                System.out.println("❌ CATATAN: File default.png tidak ada di folder " + RESOURCE_PATH);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}