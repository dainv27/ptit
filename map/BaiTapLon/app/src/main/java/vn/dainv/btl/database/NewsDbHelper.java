package vn.dainv.btl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.dainv.btl.model.TheLoai;
import vn.dainv.btl.model.TinTuc;

public class NewsDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "quan_ly_tin_tuc.db";
    private static final int DB_VERSION = 2;

    public static final String T_THE_LOAI = "the_loai";
    public static final String C_MA_LOAI = "ma_loai";
    public static final String C_TEN_LOAI = "ten_loai";
    public static final String C_MO_TA_LOAI = "mo_ta";

    public static final String T_TIN_TUC = "tin_tuc";
    public static final String C_MA_TIN = "ma_tin";
    public static final String C_TIEU_DE = "tieu_de";
    public static final String C_CHI_TIET = "chi_tiet";
    public static final String C_LINK_HINH = "link_hinh";
    public static final String C_MA_LOAI_FK = "ma_loai";
    public static final String C_NGAY_DANG = "ngay_dang";

    public NewsDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_THE_LOAI + " ("
                + C_MA_LOAI + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + C_TEN_LOAI + " TEXT NOT NULL,"
                + C_MO_TA_LOAI + " TEXT"
                + ")");

        db.execSQL("CREATE TABLE " + T_TIN_TUC + " ("
                + C_MA_TIN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + C_TIEU_DE + " TEXT NOT NULL,"
                + C_CHI_TIET + " TEXT,"
                + C_LINK_HINH + " TEXT,"
                + C_MA_LOAI_FK + " INTEGER NOT NULL,"
                + C_NGAY_DANG + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + C_MA_LOAI_FK + ") REFERENCES " + T_THE_LOAI + "(" + C_MA_LOAI + ")"
                + ")");
        db.execSQL("CREATE INDEX idx_tin_tuc_ma_loai ON " + T_TIN_TUC + "(" + C_MA_LOAI_FK + ")");
        db.execSQL("CREATE INDEX idx_tin_tuc_ngay ON " + T_TIN_TUC + "(" + C_NGAY_DANG + ")");
        seedSampleDataIfEmpty(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            seedSampleDataIfEmpty(db);
        }
    }

    private void seedSampleDataIfEmpty(SQLiteDatabase db) {
        long n = DatabaseUtils.queryNumEntries(db, T_THE_LOAI);
        if (n > 0) return;
        seedSampleData(db);
    }

    private void seedSampleData(SQLiteDatabase db) {
        ContentValues tl = new ContentValues();

        tl.put(C_TEN_LOAI, "Thời sự");
        tl.put(C_MO_TA_LOAI, "Tin trong nước và quốc tế");
        long maThoiSu = db.insert(T_THE_LOAI, null, tl);

        tl.clear();
        tl.put(C_TEN_LOAI, "Kinh tế");
        tl.put(C_MO_TA_LOAI, "Thị trường, doanh nghiệp, đầu tư");
        long maKinhTe = db.insert(T_THE_LOAI, null, tl);

        tl.clear();
        tl.put(C_TEN_LOAI, "Thể thao");
        tl.put(C_MO_TA_LOAI, "Bóng đá và các môn thể thao khác");
        long maTheThao = db.insert(T_THE_LOAI, null, tl);

        ContentValues tin = new ContentValues();

        tin.put(C_TIEU_DE, "Khai mạc giải giao hữu sinh viên 2026");
        tin.put(C_CHI_TIET, "Sáng nay, giải đấu giao hữu giữa các CLB bóng đá sinh viên đã khai mạc tại sân trường với sự tham gia của 8 đội. Ban tổ chức kỳ vọng giải sẽ tạo sân chơi lành mạnh và gắn kết sinh viên các khoa.");
        tin.put(C_LINK_HINH, "https://picsum.photos/id/1058/800/450");
        tin.put(C_MA_LOAI_FK, maTheThao);
        tin.put(C_NGAY_DANG, "2026-05-01");
        db.insert(T_TIN_TUC, null, tin);

        tin.clear();
        tin.put(C_TIEU_DE, "Đội tuyển chuẩn bị trận giao hữu cuối tuần");
        tin.put(C_CHI_TIET, "Huấn luyện viên cho biết đội hình sẽ được điều chỉnh nhẹ để thử nghiệm chiến thuật mới trước mùa giải chính thức.");
        tin.put(C_LINK_HINH, "https://picsum.photos/id/348/800/450");
        tin.put(C_MA_LOAI_FK, maTheThao);
        tin.put(C_NGAY_DANG, "2026-04-28");
        db.insert(T_TIN_TUC, null, tin);

        tin.clear();
        tin.put(C_TIEU_DE, "Thị trường chứng khoán biến động nhẹ phiên đầu tuần");
        tin.put(C_CHI_TIET, "Các chỉ số chính dao động trong biên hẹp; nhà đầu tư theo dõi sát diễn biến lãi suất và kết quả kinh doanh quý.");
        tin.put(C_LINK_HINH, "https://picsum.photos/id/180/800/450");
        tin.put(C_MA_LOAI_FK, maKinhTe);
        tin.put(C_NGAY_DANG, "2026-05-03");
        db.insert(T_TIN_TUC, null, tin);

        tin.clear();
        tin.put(C_TIEU_DE, "Doanh nghiệp nhỏ được khuyến khích chuyển đổi số");
        tin.put(C_CHI_TIET, "Chương trình hỗ trợ ưu đãi vay và đào tạo nội bộ nhằm giúp hộ kinh doanh, SME áp dụng hóa đơn điện tử và quản lý bán hàng trực tuyến.");
        tin.put(C_LINK_HINH, "https://picsum.photos/id/60/800/450");
        tin.put(C_MA_LOAI_FK, maKinhTe);
        tin.put(C_NGAY_DANG, "2026-04-15");
        db.insert(T_TIN_TUC, null, tin);

        tin.clear();
        tin.put(C_TIEU_DE, "Hội nghị khí hậu: cam kết giảm phát thải giai đoạn mới");
        tin.put(C_CHI_TIET, "Các bên thảo luận lộ trình giảm phát thải và hỗ trợ tài chính cho nước đang phát triển chuyển dần sang năng lượng tái tạo.");
        tin.put(C_LINK_HINH, "https://picsum.photos/id/1022/800/450");
        tin.put(C_MA_LOAI_FK, maThoiSu);
        tin.put(C_NGAY_DANG, "2026-04-10");
        db.insert(T_TIN_TUC, null, tin);

        tin.clear();
        tin.put(C_TIEU_DE, "Lễ kỷ niệm thành lập trường diễn ra vào cuối tháng");
        tin.put(C_CHI_TIET, "Ban giám hiệu thông báo chương trình văn nghệ và triển lãm thành tích đào tạo, mời cựu sinh viên tham dự.");
        tin.put(C_LINK_HINH, "https://picsum.photos/id/318/800/450");
        tin.put(C_MA_LOAI_FK, maThoiSu);
        tin.put(C_NGAY_DANG, "2026-03-20");
        db.insert(T_TIN_TUC, null, tin);
    }

    public long insertTheLoai(TheLoai t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(C_TEN_LOAI, t.getTenLoai());
        v.put(C_MO_TA_LOAI, t.getMoTa());
        return db.insert(T_THE_LOAI, null, v);
    }

    public int updateTheLoai(TheLoai t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(C_TEN_LOAI, t.getTenLoai());
        v.put(C_MO_TA_LOAI, t.getMoTa());
        return db.update(T_THE_LOAI, v, C_MA_LOAI + "=?", new String[]{String.valueOf(t.getMaLoai())});
    }

    public int countTinByMaLoai(long maLoai) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + T_TIN_TUC + " WHERE " + C_MA_LOAI_FK + "=?",
                new String[]{String.valueOf(maLoai)})) {
            if (c.moveToFirst()) return c.getInt(0);
        }
        return 0;
    }

    public boolean deleteTheLoai(long maLoai) {
        if (countTinByMaLoai(maLoai) > 0) return false;
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_THE_LOAI, C_MA_LOAI + "=?", new String[]{String.valueOf(maLoai)}) > 0;
    }

    public List<TheLoai> getAllTheLoai() {
        List<TheLoai> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_THE_LOAI, null, null, null, null, null, C_TEN_LOAI + " COLLATE NOCASE")) {
            while (c.moveToNext()) {
                list.add(theLoaiFromCursor(c));
            }
        }
        return list;
    }

    @Nullable
    public TheLoai getTheLoai(long maLoai) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_THE_LOAI, null, C_MA_LOAI + "=?",
                new String[]{String.valueOf(maLoai)}, null, null, null)) {
            if (c.moveToFirst()) return theLoaiFromCursor(c);
        }
        return null;
    }

    private static TheLoai theLoaiFromCursor(Cursor c) {
        return new TheLoai(
                c.getLong(c.getColumnIndexOrThrow(C_MA_LOAI)),
                c.getString(c.getColumnIndexOrThrow(C_TEN_LOAI)),
                c.getString(c.getColumnIndexOrThrow(C_MO_TA_LOAI))
        );
    }

    public long insertTinTuc(TinTuc n) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(C_TIEU_DE, n.getTieuDe());
        v.put(C_CHI_TIET, n.getChiTiet());
        v.put(C_LINK_HINH, n.getLinkHinh());
        v.put(C_MA_LOAI_FK, n.getMaLoai());
        v.put(C_NGAY_DANG, n.getNgayDang());
        return db.insert(T_TIN_TUC, null, v);
    }

    public int updateTinTuc(TinTuc n) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(C_TIEU_DE, n.getTieuDe());
        v.put(C_CHI_TIET, n.getChiTiet());
        v.put(C_LINK_HINH, n.getLinkHinh());
        v.put(C_MA_LOAI_FK, n.getMaLoai());
        v.put(C_NGAY_DANG, n.getNgayDang());
        return db.update(T_TIN_TUC, v, C_MA_TIN + "=?", new String[]{String.valueOf(n.getMaTin())});
    }

    public boolean deleteTinTuc(long maTin) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_TIN_TUC, C_MA_TIN + "=?", new String[]{String.valueOf(maTin)}) > 0;
    }

    public List<TinTuc> getAllTinTuc() {
        List<TinTuc> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_TIN_TUC, null, null, null, null, null, C_NGAY_DANG + " DESC," + C_TIEU_DE)) {
            while (c.moveToNext()) {
                list.add(tinTucFromCursor(c));
            }
        }
        return list;
    }

    @Nullable
    public TinTuc getTinTuc(long maTin) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_TIN_TUC, null, C_MA_TIN + "=?",
                new String[]{String.valueOf(maTin)}, null, null, null)) {
            if (c.moveToFirst()) return tinTucFromCursor(c);
        }
        return null;
    }

    public List<TinTuc> getTinTucByTheLoai(long maLoai) {
        List<TinTuc> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_TIN_TUC, null, C_MA_LOAI_FK + "=?",
                new String[]{String.valueOf(maLoai)}, null, null, C_NGAY_DANG + " DESC," + C_TIEU_DE)) {
            while (c.moveToNext()) {
                list.add(tinTucFromCursor(c));
            }
        }
        return list;
    }

    public List<TinTuc> getTinTucByNgayTrongKhoang(String tuNgay, String denNgay) {
        List<TinTuc> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sel = C_NGAY_DANG + " >= ? AND " + C_NGAY_DANG + " <= ?";
        String[] args = {tuNgay, denNgay};
        try (Cursor c = db.query(T_TIN_TUC, null, sel, args, null, null, C_NGAY_DANG + " DESC," + C_TIEU_DE)) {
            while (c.moveToNext()) {
                list.add(tinTucFromCursor(c));
            }
        }
        return list;
    }

    public String getTenLoai(long maLoai) {
        TheLoai t = getTheLoai(maLoai);
        return t != null ? t.getTenLoai() : "";
    }

    private static TinTuc tinTucFromCursor(Cursor c) {
        return new TinTuc(
                c.getLong(c.getColumnIndexOrThrow(C_MA_TIN)),
                c.getString(c.getColumnIndexOrThrow(C_TIEU_DE)),
                c.getString(c.getColumnIndexOrThrow(C_CHI_TIET)),
                c.getString(c.getColumnIndexOrThrow(C_LINK_HINH)),
                c.getLong(c.getColumnIndexOrThrow(C_MA_LOAI_FK)),
                c.getString(c.getColumnIndexOrThrow(C_NGAY_DANG))
        );
    }
}
