package vn.dainv.btl.model;

public class TheLoai {
    private long maLoai;
    private String tenLoai;
    private String moTa;

    public TheLoai(long maLoai, String tenLoai, String moTa) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai != null ? tenLoai : "";
        this.moTa = moTa != null ? moTa : "";
    }

    public long getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(long maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai != null ? tenLoai : "";
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa != null ? moTa : "";
    }
}
