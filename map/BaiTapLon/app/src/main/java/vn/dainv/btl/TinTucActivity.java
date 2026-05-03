package vn.dainv.btl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import vn.dainv.btl.database.NewsDbHelper;
import vn.dainv.btl.model.TinTuc;

public class TinTucActivity extends AppCompatActivity {

    public static final String EXTRA_MA_TIN = "ma_tin";

    private NewsDbHelper db;
    private List<TinTuc> items = new ArrayList<>();
    private TinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tin_tuc_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_tin_tuc), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        db = new NewsDbHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_tin_tuc);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ListView listView = findViewById(R.id.list_tin_tuc);
        adapter = new TinAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TinTuc n = items.get(position);
            startActivity(new Intent(this, TinTucEditActivity.class)
                    .putExtra(EXTRA_MA_TIN, n.getMaTin()));
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_tin);
        fab.setOnClickListener(v -> startActivity(new Intent(this, TinTucEditActivity.class)
                .putExtra(EXTRA_MA_TIN, -1L)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        items = db.getAllTinTuc();
        adapter.notifyDataSetChanged();
    }

    private void confirmDelete(TinTuc n) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.xoa_tin)
                .setMessage(getString(R.string.xac_nhan_xoa_tin, n.getTieuDe()))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.xoa, (d, w) -> {
                    if (db.deleteTinTuc(n.getMaTin())) {
                        Toast.makeText(this, R.string.da_xoa, Toast.LENGTH_SHORT).show();
                        reload();
                    } else {
                        Toast.makeText(this, R.string.loi_xoa, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private class TinAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public TinTuc getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getMaTin();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tin_tuc, parent, false);
            }
            TinTuc n = getItem(position);
            ((TextView) row.findViewById(R.id.item_tieu_de)).setText(n.getTieuDe());
            ((TextView) row.findViewById(R.id.item_ten_loai_tin)).setText(db.getTenLoai(n.getMaLoai()));
            ((TextView) row.findViewById(R.id.item_ngay_dang)).setText(n.getNgayDang());
            row.findViewById(R.id.btn_delete_tin).setOnClickListener(v -> confirmDelete(n));
            return row;
        }
    }
}
