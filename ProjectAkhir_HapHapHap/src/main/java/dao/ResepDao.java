package dao;

import model.Bahan;
import model.Resep;

import java.util.List;

public interface ResepDao {
    List<Resep> getAllResep();

    Resep getResepById(int idResep);

    List<Resep> cariBerdasarkanNama(String nama);

    List<Resep> filterBerdasarkanBahan(List<String> bahanList);

    List<Resep> filterBerdasarkanKategori(String kategori);

    List<Bahan> getBahanByResep(int idResep);

    boolean tambahResepLengkap(int idUser, String judul, int idKategori, int kepedasan, int waktu, int porsi,
            String langkah, List<String> bahanList, String foto, String status);

    List<Resep> getFavoritByUser(int idUser);

    List<Resep> getResepByPembuat(int idUser);

    boolean hapusResepPermanen(int idResep);

    List<Resep> getPendingResep();

    boolean updateResepStatus(int idResep, String status);

    boolean editResepLengkap(int idResep, int idKategori, String judul, int kepedasan, int waktu, int porsi,
            String langkah, List<String> bahanList, String foto);

    boolean cekFavorit(int idUser, int idResep);

    boolean tambahKeFavorit(int idUser, int idResep);

    boolean hapusFavorit(int idUser, int idResep);
}
