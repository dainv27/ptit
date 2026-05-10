package vn.dainv.btl.model;

public class TinTuc {
    private long maTin;
    private String tieuDe;
    private String chiTiet;
    private String linkHinh;
    private long maLoai;
    private String ngayDang;

    public TinTuc(long maTin, String tieuDe, String chiTiet, String linkHinh, long maLoai, String ngayDang) {
        this.maTin = maTin;
        this.tieuDe = tieuDe != null ? tieuDe : "";
        this.chiTiet = chiTiet != null ? chiTiet : "";
        this.linkHinh = linkHinh != null ? linkHinh : "";
        this.maLoai = maLoai;
        this.ngayDang = ngayDang != null ? ngayDang : "";
    }

    public long getMaTin() {
        return maTin;
    }

    public void setMaTin(long maTin) {
        this.maTin = maTin;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe != null ? tieuDe : "";
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet != null ? chiTiet : "";
    }

    public String getLinkHinh() {
        return linkHinh;
    }

    public void setLinkHinh(String linkHinh) {
        this.linkHinh = linkHinh != null ? linkHinh : "";
    }

    public long getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(long maLoai) {
        this.maLoai = maLoai;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang != null ? ngayDang : "";
    }
}
