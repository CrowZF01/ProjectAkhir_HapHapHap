package model;

public class Resep {
    private int idResep;
    private String judul;
    private String jenisMakanan;
    private int tingkatKepedasan;
    private String bahan;
    private String langkahPembuatan;
    private int estimasiWaktu;
    private int porsiSajian;
    private String foto;
    private String status; // 'PENDING', 'PUBLISHED', 'REJECTED'

    public Resep() {
    }

    public Resep(int idResep, String judul, String jenisMakanan, int tingkatKepedasan,
                 String bahan, String langkahPembuatan, int estimasiWaktu, int porsiSajian, String foto, String status) {
        this.idResep = idResep;
        this.judul = judul;
        this.jenisMakanan = jenisMakanan;
        this.tingkatKepedasan = tingkatKepedasan;
        this.bahan = bahan;
        this.langkahPembuatan = langkahPembuatan;
        this.estimasiWaktu = estimasiWaktu;
        this.porsiSajian = porsiSajian;
        this.foto = foto;
        this.status = status;
    }

    public Resep(int idResep, String judul, String jenisMakanan, int tingkatKepedasan,
                 String bahan, String langkahPembuatan, int estimasiWaktu, int porsiSajian, String foto) {
        this.idResep = idResep;
        this.judul = judul;
        this.jenisMakanan = jenisMakanan;
        this.tingkatKepedasan = tingkatKepedasan;
        this.bahan = bahan;
        this.langkahPembuatan = langkahPembuatan;
        this.estimasiWaktu = estimasiWaktu;
        this.porsiSajian = porsiSajian;
        this.foto = foto;
        this.status = "PENDING";
    }

    public int getIdResep() {
        return idResep;
    }

    public void setIdResep(int idResep) {
        this.idResep = idResep;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    public void setJenisMakanan(String jenisMakanan) {
        this.jenisMakanan = jenisMakanan;
    }

    public int getTingkatKepedasan() {
        return tingkatKepedasan;
    }

    public void setTingkatKepedasan(int tingkatKepedasan) {
        this.tingkatKepedasan = tingkatKepedasan;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getLangkahPembuatan() {
        return langkahPembuatan;
    }

    public void setLangkahPembuatan(String langkahPembuatan) {
        this.langkahPembuatan = langkahPembuatan;
    }

    public int getEstimasiWaktu() {
        return estimasiWaktu;
    }

    public void setEstimasiWaktu(int estimasiWaktu) {
        this.estimasiWaktu = estimasiWaktu;
    }

    public int getPorsiSajian() {
        return porsiSajian;
    }

    public void setPorsiSajian(int porsiSajian) {
        this.porsiSajian = porsiSajian;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetailResep() {
        return judul + "\n\nBahan:\n" + bahan + "\n\nLangkah:\n" + langkahPembuatan;
    }
}