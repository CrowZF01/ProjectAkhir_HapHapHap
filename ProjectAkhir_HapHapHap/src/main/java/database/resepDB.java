package database;

import dao.ResepDao;
import model.Bahan;
import model.Resep;
import util.databaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class resepDB implements ResepDao {

    private static resepDB instance;

    private resepDB() {
    }

    public static resepDB getInstance() {
        if (instance == null) {
            instance = new resepDB();
        }
        return instance;
    }

    // Query sakti huruf kecil sesuai DB
    private final String BASE_QUERY = """
            SELECT resep.id_resep, resep.nama_resep, kategori.nama_kategori, resep.tingkat_kepedasan, resep.foto,
                    GROUP_CONCAT(bahan.nama_bahan SEPARATOR ', ') AS daftar_bahan,
                    resep.langkah_pembuatan, resep.waktu_estimasi, resep.porsi_sajian, resep.status
                    FROM resep
                    LEFT JOIN kategori ON resep.id_kategori = kategori.id_kategori
                    LEFT JOIN resep_bahan ON resep.id_resep = resep_bahan.id_resep
                    LEFT JOIN bahan ON resep_bahan.id_bahan = bahan.id_bahan """;

    public List<Resep> getAllResep() {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY + " WHERE resep.status = 'PUBLISHED' GROUP BY resep.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Resep getResepById(int idResep) {
        String sql = BASE_QUERY + " WHERE resep.id_resep = ? GROUP BY resep.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idResep);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToResep(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Resep> cariBerdasarkanNama(String nama) {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY
                + " WHERE resep.status = 'PUBLISHED' AND resep.nama_resep LIKE ? GROUP BY resep.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nama + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Resep> filterBerdasarkanBahan(List<String> bahanList) {
        List<Resep> list = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
        sqlBuilder.append(" WHERE resep.status = 'PUBLISHED' GROUP BY resep.id_resep HAVING ");

        for (int i = 0; i < bahanList.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append("daftar_bahan LIKE ?");
        }

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < bahanList.size(); i++) {
                stmt.setString(i + 1, "%" + bahanList.get(i) + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Resep mapToResep(ResultSet rs) throws SQLException {
        String bahan = rs.getString("daftar_bahan");
        if (bahan == null)
            bahan = "";

        return new Resep(
                rs.getInt("id_resep"),
                rs.getString("nama_resep"),
                rs.getString("nama_kategori"),
                rs.getInt("tingkat_kepedasan"),
                bahan,
                rs.getString("langkah_pembuatan"),
                rs.getInt("waktu_estimasi"),
                rs.getInt("porsi_sajian"),
                rs.getString("foto"),
                rs.getString("status"));
    }

    public List<Resep> filterBerdasarkanKategori(String kategori) {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY
                + " WHERE resep.status = 'PUBLISHED' AND kategori.nama_kategori = ? GROUP BY resep.id_resep";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kategori);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Bahan> getBahanByResep(int idResep) {
        List<Bahan> list = new ArrayList<>();
        String sql = """
                SELECT bahan.id_bahan, resep_bahan.id_resep, bahan.nama_bahan FROM bahan
                JOIN resep_bahan ON bahan.id_bahan = resep_bahan.id_bahan
                WHERE resep_bahan.id_resep = ?""";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idResep);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bahan bahan = new Bahan();
                bahan.setIdBahan(rs.getInt("id_bahan"));
                bahan.setIdResep(rs.getInt("id_resep"));
                bahan.setNamaBahan(rs.getString("nama_bahan"));
                list.add(bahan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // Method Transaction Super Aman untuk Insert 3 Tabel Sekaligus + Foto!
    public boolean tambahResepLengkap(int idUser, String judul, int idKategori, int kepedasan, int waktu, int porsi,
                                      String langkah, List<String> bahanList, String foto, String status) {
        try (Connection conn = databaseUtil.getConnection()) {

            // 1. Simpan data resep
            String sqlResep = "INSERT INTO resep (id_user, id_kategori, nama_resep, langkah_pembuatan, waktu_estimasi, porsi_sajian, tingkat_kepedasan, foto, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtResep = conn.prepareStatement(sqlResep);

            stmtResep.setInt(1, idUser);
            stmtResep.setInt(2, idKategori);
            stmtResep.setString(3, judul);
            stmtResep.setString(4, langkah);
            stmtResep.setInt(5, waktu);
            stmtResep.setInt(6, porsi);
            stmtResep.setInt(7, kepedasan);
            stmtResep.setString(8, foto);
            stmtResep.setString(9, status);

            int hasilResep = stmtResep.executeUpdate();

            if (hasilResep <= 0) {
                return false;
            }

            // 2. Ambil id resep terakhir
            int idResepBaru = -1;
            Statement stmtId = conn.createStatement();
            ResultSet rsId = stmtId.executeQuery("SELECT LAST_INSERT_ID()");

            if (rsId.next()) {
                idResepBaru = rsId.getInt(1);
            }

            if (idResepBaru == -1) {
                return false;
            }

            // 3. Simpan bahan dan relasi resep_bahan
            String sqlBahan = "INSERT INTO bahan (nama_bahan) VALUES (?)";
            String sqlAmbilIdBahan = "SELECT LAST_INSERT_ID()";
            String sqlRelasi = "INSERT INTO resep_bahan (id_resep, id_bahan) VALUES (?, ?)";

            PreparedStatement stmtBahan = conn.prepareStatement(sqlBahan);
            PreparedStatement stmtRelasi = conn.prepareStatement(sqlRelasi);
            Statement stmtIdBahan = conn.createStatement();

            for (String namaBahan : bahanList) {
                if (namaBahan.trim().isEmpty()) {
                    continue;
                }

                stmtBahan.setString(1, namaBahan);
                int hasilBahan = stmtBahan.executeUpdate();

                if (hasilBahan > 0) {
                    ResultSet rsBahan = stmtIdBahan.executeQuery(sqlAmbilIdBahan);

                    if (rsBahan.next()) {
                        int idBahanBaru = rsBahan.getInt(1);

                        stmtRelasi.setInt(1, idResepBaru);
                        stmtRelasi.setInt(2, idBahanBaru);
                        stmtRelasi.executeUpdate();
                    }
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Mengambil daftar resep favorit milik user tertentu
    public List<Resep> getFavoritByUser(int idUser) {
        List<Resep> list = new ArrayList<>();
        // INNER JOIN dengan tabel favorit_user
        String sql = BASE_QUERY
                + " INNER JOIN favorit_user ON resep.id_resep = favorit_user.id_resep WHERE favorit_user.id_user = ? GROUP BY resep.id_resep";

        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // === TAMBAHKAN 2 METHOD INI DI DALAM CLASS resepDB ===

    // Mengambil resep yang dibuat oleh user (My Recipes)
    public List<Resep> getResepByPembuat(int idUser) {
        List<Resep> list = new ArrayList<>();
        String sql = BASE_QUERY + " WHERE resep.id_user = ? GROUP BY resep.id_resep";

        try (Connection conn = util.databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToResep(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Menghapus resep secara permanen (Cascade Manual)
    public boolean hapusResepPermanen(int idResep) {
        String sqlBahan = "DELETE FROM resep_bahan WHERE id_resep = ?";
        String sqlFav = "DELETE FROM favorit_user WHERE id_resep = ?";
        String sqlResep = "DELETE FROM resep WHERE id_resep = ?";

        try (Connection conn = util.databaseUtil.getConnection()) {
            conn.setAutoCommit(false); // Mulai transaksi

            try (PreparedStatement stmt1 = conn.prepareStatement(sqlBahan);
                 PreparedStatement stmt2 = conn.prepareStatement(sqlFav);
                 PreparedStatement stmt3 = conn.prepareStatement(sqlResep)) {

                // Hapus relasi bahan
                stmt1.setInt(1, idResep);
                stmt1.executeUpdate();

                // Hapus relasi dari favorit (jika ada yang memfavoritkan)
                stmt2.setInt(1, idResep);
                stmt2.executeUpdate();

                // Hapus resep utamanya
                stmt3.setInt(1, idResep);
                int hasil = stmt3.executeUpdate();

                conn.commit(); // Simpan perubahan
                return hasil > 0;
            } catch (SQLException e) {
                conn.rollback(); // Batalkan jika ada error
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean cekFavorit(int idUser, int idResep) {
        String sql = "SELECT * FROM favorit_user WHERE id_user = ? AND id_resep = ?";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idResep);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tambahKeFavorit(int idUser, int idResep) {
        String sql = "INSERT INTO favorit_user (id_user, id_resep) VALUES (?, ?)";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idResep);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hapusFavorit(int idUser, int idResep) {
        String sql = "DELETE FROM favorit_user WHERE id_user = ? AND id_resep = ?";
        try (Connection conn = databaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idResep);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}