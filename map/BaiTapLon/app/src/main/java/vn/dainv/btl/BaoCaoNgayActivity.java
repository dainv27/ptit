package vn.dainv.btl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import vn.dainv.btl.database.NewsDbHelper;
import vn.dainv.btl.model.TinTuc;
import vn.dainv.btl.util.DateTextUtil;

public class BaoCaoNgayActivity extends AppCompatActivity {

    private NewsDbHelper db;
    private List<TinTuc> tinList = new ArrayList<>();
    private TinAdapter adapter;
    private TextInputEditText etTu;
    private TextInputEditText etDen;
    private View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_cao_ngay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_bao_cao_ngay), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        db = new NewsDbHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_bao_cao_ngay);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        etTu = findViewById(R.id.et_tu_ngay);
        etDen = findViewById(R.id.et_den_ngay);
        etTu.setText(DateTextUtil.todayIso());
        etDen.setText(DateTextUtil.todayIso());

        empty = findViewById(R.id.empty_ngay);
        ListView listView = findViewById(R.id.list_bao_cao_ngay);
        adapter = new TinAdapter();
        listView.setAdapter(adapter);

        MaterialButton btn = findViewById(R.id.btn_truy_van_ngay);
        btn.setOnClickListener(v -> truyVan());
    }

    private void truyVan() {
        String tu = etTu.getText() != null ? etTu.getText().toString().trim() : "";
        String den = etDen.getText() != null ? etDen.getText().toString().trim() : "";
        if (!DateTextUtil.isValidYyyyMmDd(tu)) {
            etTu.setError(getString(R.string.dinh_dang_ngay));
            return;
        }
        if (!DateTextUtil.isValidYyyyMmDd(den)) {
            etDen.setError(getString(R.string.dinh_dang_ngay));
            return;
        }
        if (tu.compareTo(den) > 0) {
            Toast.makeText(this, R.string.tu_ngay_lon_hon, Toast.LENGTH_SHORT).show();
            return;
        }

        tinList = db.getTinTucByNgayTrongKhoang(tu, den);
        adapter.notifyDataSetChanged();
        empty.setVisibility(tinList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private class TinAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return tinList.size();
        }

        @Override
        public TinTuc getItem(int position) {
            return tinList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getMaTin();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tin_tuc_bao_cao, parent, false);
            }
            TinTuc n = getItem(position);
            ((TextView) row.findViewById(R.id.bc_tieu_de)).setText(n.getTieuDe());
            ((TextView) row.findViewById(R.id.bc_ngay)).setText(n.getNgayDang());
            ((TextView) row.findViewById(R.id.bc_loai)).setText(db.getTenLoai(n.getMaLoai()));
            row.findViewById(R.id.bc_loai).setVisibility(View.VISIBLE);
            TextView detail = row.findViewById(R.id.bc_chi_tiet_short);
            String ct = n.getChiTiet();
            if (ct.length() > 120) {
                detail.setText(ct.substring(0, 120) + "…");
            } else {
                detail.setText(ct);
            }
            return row;
        }
    }
}
