package service;

import dao.ResepDao;
import database.resepDB;
import model.Bahan;
import model.Resep;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class RecipeService {

    private static RecipeService instance;
    private final ResepDao resepDao;

    private RecipeService() {
        this.resepDao = resepDB.getInstance();
    }

    public static RecipeService getInstance() {
        if (instance == null) {
            instance = new RecipeService();
        }
        return instance;
    }

    public List<Resep> getAllResep() {
        return resepDao.getAllResep();
    }

    public Resep getResepById(int idResep) {
        return resepDao.getResepById(idResep);
    }

    public List<Bahan> getBahanByResep(int idResep) {
        return resepDao.getBahanByResep(idResep);
    }

    public List<Resep> getFavoritByUser(int idUser) {
        return resepDao.getFavoritByUser(idUser);
    }

    public List<Resep> getResepByPembuat(int idUser) {
        return resepDao.getResepByPembuat(idUser);
    }

    public boolean hapusResepPermanen(int idResep) {
        return resepDao.hapusResepPermanen(idResep);
    }

    public void simpanResep(int idUser, String judul, String kategori, int tingkatKepedasan,
                            String waktuStr, String porsiStr, List<String> listBahan,
                            List<String> listLangkah, File fotoTerpilih) {

        if (judul == null || judul.trim().isEmpty()) {
            throw new IllegalArgumentException("Judul resep tidak boleh kosong!");
        }
        if (kategori == null) {
            throw new IllegalArgumentException("Silakan pilih kategori resep!");
        }

        if (waktuStr == null || waktuStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Estimasi Waktu tidak boleh kosong!");
        }
        if (porsiStr == null || porsiStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Porsi sajian tidak boleh kosong!");
        }

        int waktu = 0;
        int porsi = 0;

        try {
            waktu = waktuStr == null || waktuStr.isEmpty() ? 0 : Integer.parseInt(waktuStr);
            porsi = porsiStr == null || porsiStr.isEmpty() ? 0 : Integer.parseInt(porsiStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Estimasi Waktu dan Porsi harus berupa angka bulat!");
        }


        if (listBahan == null || listBahan.isEmpty()) {
            throw new IllegalArgumentException("Minimal harus ada 1 bahan yang diisi!");
        }

        // Map kategori ke idKategori
        int idKategori = 2; // Default Makanan
        if ("Dessert".equals(kategori)) {
            idKategori = 4;
        } else if ("Minuman".equals(kategori)) {
            idKategori = 5;
        }

        // Susun langkah gabungan
        StringBuilder langkahGabungan = new StringBuilder();
        int step = 1;
        for (String langkah : listLangkah) {
            if (langkah != null && !langkah.trim().isEmpty()) {
                langkahGabungan.append(step).append(". ").append(langkah.trim()).append("\n\n");
                step++;
            }
        }

        // Proses penyalinan foto resep
        String namaFileFoto = null;
        if (fotoTerpilih != null) {
            try {
                namaFileFoto = System.currentTimeMillis() + "_" + fotoTerpilih.getName().replaceAll("\\s+", "_");

                File folderImages = new File("src/main/resources/images/");
                if (!folderImages.exists()) {
                    folderImages.mkdirs();
                }

                File tujuan = new File(folderImages, namaFileFoto);
                Files.copy(fotoTerpilih.toPath(), tujuan.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Terjadi kesalahan saat mengunggah foto: " + e.getMessage());
            }
        }

        // Simpan ke database
        boolean sukses = resepDao.tambahResepLengkap(idUser, judul, idKategori, tingkatKepedasan,
                waktu, porsi, langkahGabungan.toString(),
                listBahan, namaFileFoto);
        if (!sukses) {
            throw new RuntimeException("Gagal menyimpan resep ke database. Silakan coba lagi.");
        }
    }


    public List<Resep> getPendingResep() {
        return resepDao.getPendingResep();
    }

    public boolean updateResepStatus(int idResep, String status) {
        return resepDao.updateResepStatus(idResep, status);
    }

    public void perbaruiResep(int idResep, String judul, String kategori, int tingkatKepedasan,
                              String waktuStr, String porsiStr, List<String> listBahan,
                              List<String> listLangkah, File fotoTerpilih) {

        if (judul == null || judul.trim().isEmpty()) {
            throw new IllegalArgumentException("Judul resep tidak boleh kosong!");
        }
        if (kategori == null) {
            throw new IllegalArgumentException("Silakan pilih kategori resep!");
        }

        int waktu = 0;
        int porsi = 0;
        try {
            waktu = waktuStr == null || waktuStr.isEmpty() ? 0 : Integer.parseInt(waktuStr);
            porsi = porsiStr == null || porsiStr.isEmpty() ? 0 : Integer.parseInt(porsiStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Estimasi Waktu dan Porsi harus berupa angka bulat!");
        }

        if (listBahan == null || listBahan.isEmpty()) {
            throw new IllegalArgumentException("Minimal harus ada 1 bahan yang diisi!");
        }

        int idKategori = 2; // Default Makanan
        if ("Dessert".equals(kategori)) {
            idKategori = 4;
        } else if ("Minuman".equals(kategori)) {
            idKategori = 5;
        }

        StringBuilder langkahGabungan = new StringBuilder();
        int step = 1;
        for (String langkah : listLangkah) {
            if (langkah != null && !langkah.trim().isEmpty()) {
                langkahGabungan.append(step).append(". ").append(langkah.trim()).append("\n\n");
                step++;
            }
        }

        String namaFileFoto = null;
        if (fotoTerpilih != null) {
            try {
                namaFileFoto = System.currentTimeMillis() + "_" + fotoTerpilih.getName().replaceAll("\\s+", "_");

                File folderImages = new File("src/main/resources/images/");
                if (!folderImages.exists()) {
                    folderImages.mkdirs();
                }

                File tujuan = new File(folderImages, namaFileFoto);
                java.nio.file.Files.copy(fotoTerpilih.toPath(), tujuan.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Terjadi kesalahan saat mengunggah foto: " + e.getMessage());
            }
        }

        boolean sukses = resepDao.editResepLengkap(idResep, idKategori, judul, tingkatKepedasan,
                waktu, porsi, langkahGabungan.toString(),
                listBahan, namaFileFoto);
        if (!sukses) {
            throw new RuntimeException("Gagal mengupdate resep di database. Silakan coba lagi.");
        }
    }

    public boolean cekFavorit(int idUser, int idResep) {
        return resepDao.cekFavorit(idUser, idResep);
    }

    public void toggleFavorit(int idUser, int idResep, boolean isFavoritNow) {
        if (isFavoritNow) {
            resepDao.hapusFavorit(idUser, idResep);
        } else {
            resepDao.tambahKeFavorit(idUser, idResep);
        }
    }

    
}
