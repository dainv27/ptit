package vn.dainv.btl;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import vn.dainv.btl.database.NewsDbHelper;
import vn.dainv.btl.model.TheLoai;

public class TheLoaiEditActivity extends AppCompatActivity {

    private NewsDbHelper db;
    private long maLoai;
    private TextInputEditText etTen;
    private TextInputEditText etMoTa;
    private TextInputEditText etMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_the_loai_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_the_loai_edit), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        db = new NewsDbHelper(this);
        maLoai = getIntent().getLongExtra(TheLoaiActivity.EXTRA_MA_LOAI, -1L);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_the_loai_edit);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        etMa = findViewById(R.id.et_ma_loai);
        etTen = findViewById(R.id.et_ten_loai);
        etMoTa = findViewById(R.id.et_mo_ta_loai);

        if (maLoai < 0) {
            toolbar.setTitle(R.string.them_the_loai);
            etMa.setText(R.string.tu_dong_sau_luu);
            etMa.setEnabled(false);
        } else {
            toolbar.setTitle(R.string.sua_the_loai);
            TheLoai t = db.getTheLoai(maLoai);
            if (t == null) {
                Toast.makeText(this, R.string.khong_tim_thay, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            etMa.setText(String.valueOf(t.getMaLoai()));
            etMa.setEnabled(false);
            etTen.setText(t.getTenLoai());
            etMoTa.setText(t.getMoTa());
        }

        MaterialButton btnSave = findViewById(R.id.btn_save_the_loai);
        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String ten = etTen.getText() != null ? etTen.getText().toString().trim() : "";
        if (ten.isEmpty()) {
            etTen.setError(getString(R.string.bat_buoc));
            return;
        }
        String moTa = etMoTa.getText() != null ? etMoTa.getText().toString().trim() : "";

        if (maLoai < 0) {
            long id = db.insertTheLoai(new TheLoai(0, ten, moTa));
            if (id > 0) {
                Toast.makeText(this, R.string.da_luu, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.loi_luu, Toast.LENGTH_SHORT).show();
            }
        } else {
            TheLoai t = new TheLoai(maLoai, ten, moTa);
            if (db.updateTheLoai(t) > 0) {
                Toast.makeText(this, R.string.da_luu, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.loi_luu, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
