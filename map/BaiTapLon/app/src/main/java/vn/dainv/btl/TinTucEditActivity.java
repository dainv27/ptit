package vn.dainv.btl;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import vn.dainv.btl.database.NewsDbHelper;
import vn.dainv.btl.model.TheLoai;
import vn.dainv.btl.model.TinTuc;
import vn.dainv.btl.util.DateTextUtil;

public class TinTucEditActivity extends AppCompatActivity {

    private NewsDbHelper db;
    private long maTin;
    private TextInputEditText etMa;
    private TextInputEditText etTieuDe;
    private TextInputEditText etChiTiet;
    private TextInputEditText etLinkHinh;
    private TextInputEditText etNgay;
    private Spinner spTheLoai;
    private List<TheLoai> theLoaiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tin_tuc_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_tin_edit), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        db = new NewsDbHelper(this);
        maTin = getIntent().getLongExtra(TinTucActivity.EXTRA_MA_TIN, -1L);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_tin_edit);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        etMa = findViewById(R.id.et_ma_tin);
        etTieuDe = findViewById(R.id.et_tieu_de);
        etChiTiet = findViewById(R.id.et_chi_tiet);
        etLinkHinh = findViewById(R.id.et_link_hinh);
        etNgay = findViewById(R.id.et_ngay_dang);
        spTheLoai = findViewById(R.id.sp_the_loai);

        theLoaiList = db.getAllTheLoai();
        if (theLoaiList.isEmpty()) {
            Toast.makeText(this, R.string.can_tao_the_loai_truoc, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String[] labels = new String[theLoaiList.size()];
        for (int i = 0; i < theLoaiList.size(); i++) {
            TheLoai t = theLoaiList.get(i);
            labels[i] = t.getTenLoai() + " (#" + t.getMaLoai() + ")";
        }
        ArrayAdapter<String> spAd = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, labels);
        spTheLoai.setAdapter(spAd);

        if (maTin < 0) {
            toolbar.setTitle(R.string.them_tin);
            etMa.setText(R.string.tu_dong_sau_luu);
            etMa.setEnabled(false);
            etNgay.setText(DateTextUtil.todayIso());
        } else {
            toolbar.setTitle(R.string.sua_tin);
            TinTuc n = db.getTinTuc(maTin);
            if (n == null) {
                Toast.makeText(this, R.string.khong_tim_thay, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            etMa.setText(String.valueOf(n.getMaTin()));
            etMa.setEnabled(false);
            etTieuDe.setText(n.getTieuDe());
            etChiTiet.setText(n.getChiTiet());
            etLinkHinh.setText(n.getLinkHinh());
            etNgay.setText(n.getNgayDang());
            for (int i = 0; i < theLoaiList.size(); i++) {
                if (theLoaiList.get(i).getMaLoai() == n.getMaLoai()) {
                    spTheLoai.setSelection(i);
                    break;
                }
            }
        }

        MaterialButton btnSave = findViewById(R.id.btn_save_tin);
        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String tieuDe = etTieuDe.getText() != null ? etTieuDe.getText().toString().trim() : "";
        if (tieuDe.isEmpty()) {
            etTieuDe.setError(getString(R.string.bat_buoc));
            return;
        }
        String chiTiet = etChiTiet.getText() != null ? etChiTiet.getText().toString().trim() : "";
        String link = etLinkHinh.getText() != null ? etLinkHinh.getText().toString().trim() : "";
        String ngay = etNgay.getText() != null ? etNgay.getText().toString().trim() : "";
        if (!DateTextUtil.isValidYyyyMmDd(ngay)) {
            etNgay.setError(getString(R.string.dinh_dang_ngay));
            return;
        }

        int idx = spTheLoai.getSelectedItemPosition();
        if (idx < 0 || idx >= theLoaiList.size()) {
            Toast.makeText(this, R.string.chon_the_loai, Toast.LENGTH_SHORT).show();
            return;
        }
        long maLoai = theLoaiList.get(idx).getMaLoai();

        if (maTin < 0) {
            long id = db.insertTinTuc(new TinTuc(0, tieuDe, chiTiet, link, maLoai, ngay));
            if (id > 0) {
                Toast.makeText(this, R.string.da_luu, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.loi_luu, Toast.LENGTH_SHORT).show();
            }
        } else {
            TinTuc n = new TinTuc(maTin, tieuDe, chiTiet, link, maLoai, ngay);
            if (db.updateTinTuc(n) > 0) {
                Toast.makeText(this, R.string.da_luu, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.loi_luu, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
