package vn.dainv.btl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import vn.dainv.btl.database.NewsDbHelper;
import vn.dainv.btl.model.TheLoai;
import vn.dainv.btl.model.TinTuc;

public class BaoCaoTheLoaiActivity extends AppCompatActivity {

    private NewsDbHelper db;
    private List<TheLoai> theLoaiList = new ArrayList<>();
    private List<TinTuc> tinList = new ArrayList<>();
    private TinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_cao_the_loai);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_bao_cao_tl), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        db = new NewsDbHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_bao_cao_tl);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        Spinner sp = findViewById(R.id.sp_bao_cao_the_loai);
        ListView listView = findViewById(R.id.list_bao_cao_tin);
        adapter = new TinAdapter();
        listView.setAdapter(adapter);

        theLoaiList = db.getAllTheLoai();
        TextView empty = findViewById(R.id.empty_bao_cao);
        if (theLoaiList.isEmpty()) {
            empty.setText(R.string.chua_co_the_loai);
            empty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            sp.setVisibility(View.GONE);
            return;
        }

        List<String> labels = new ArrayList<>();
        for (TheLoai t : theLoaiList) {
            labels.add(t.getTenLoai());
        }
        ArrayAdapter<String> spAd = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, labels);
        sp.setAdapter(spAd);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TheLoai tl = theLoaiList.get(position);
                tinList = db.getTinTucByTheLoai(tl.getMaLoai());
                adapter.notifyDataSetChanged();
                TextView ev = findViewById(R.id.empty_bao_cao);
                if (tinList.isEmpty()) {
                    ev.setText(R.string.chua_co_tin);
                    ev.setVisibility(View.VISIBLE);
                } else {
                    ev.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp.setSelection(0);
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
            row.findViewById(R.id.bc_loai).setVisibility(View.GONE);
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
