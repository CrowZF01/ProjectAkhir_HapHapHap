package model;

public class Bahan {
    private int idBahan;
    private int idResep;
    private String namaBahan;

    public Bahan(){
    }

    public Bahan(int idBahan, int idResep, String namaBahan) {
        this.idBahan = idBahan;
        this.idResep = idResep;
        this.namaBahan = namaBahan;
    }

    public int getIdBahan() {
        return idBahan;
    }

    public void setIdBahan(int idBahan) {
        this.idBahan = idBahan;
    }

    public int getIdResep() {
        return idResep;
    }

    public void setIdResep(int idResep) {
        this.idResep = idResep;
    }

    public String getNamaBahan() {
        return namaBahan;
    }

    public void setNamaBahan(String namaBahan) {
        this.namaBahan = namaBahan;
    }

    @Override
    public String toString(){
        return "Bahan{" + "namaBahan='" + namaBahan + '\'' + '}';
    }
}
