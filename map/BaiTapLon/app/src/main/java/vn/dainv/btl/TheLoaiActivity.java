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
import vn.dainv.btl.model.TheLoai;

public class TheLoaiActivity extends AppCompatActivity {

    public static final String EXTRA_MA_LOAI = "ma_loai";

    private NewsDbHelper db;
    private List<TheLoai> items = new ArrayList<>();
    private TheLoaiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_the_loai_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_the_loai), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        db = new NewsDbHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_the_loai);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ListView listView = findViewById(R.id.list_the_loai);
        adapter = new TheLoaiAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TheLoai t = items.get(position);
            startActivity(new Intent(this, TheLoaiEditActivity.class)
                    .putExtra(EXTRA_MA_LOAI, t.getMaLoai()));
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_the_loai);
        fab.setOnClickListener(v -> startActivity(new Intent(this, TheLoaiEditActivity.class)
                .putExtra(EXTRA_MA_LOAI, -1L)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        items = db.getAllTheLoai();
        adapter.notifyDataSetChanged();
    }

    private void confirmDelete(TheLoai t) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.xoa_the_loai)
                .setMessage(getString(R.string.xac_nhan_xoa_the_loai, t.getTenLoai()))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.xoa, (d, w) -> {
                    if (db.deleteTheLoai(t.getMaLoai())) {
                        Toast.makeText(this, R.string.da_xoa, Toast.LENGTH_SHORT).show();
                        reload();
                    } else {
                        Toast.makeText(this, R.string.khong_xoa_duoc_con_tin, Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private class TheLoaiAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public TheLoai getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getMaLoai();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_the_loai, parent, false);
            }
            TheLoai t = getItem(position);
            ((TextView) row.findViewById(R.id.item_ten_loai)).setText(t.getTenLoai());
            TextView moTa = row.findViewById(R.id.item_mo_ta_loai);
            moTa.setText(t.getMoTa());
            moTa.setVisibility(t.getMoTa().isEmpty() ? View.GONE : View.VISIBLE);
            row.findViewById(R.id.btn_delete_the_loai).setOnClickListener(v -> confirmDelete(t));
            return row;
        }
    }
}
