package vn.dainv.btl;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);

        findViewById(R.id.btn_the_loai).setOnClickListener(v ->
                startActivity(new Intent(this, TheLoaiActivity.class)));
        findViewById(R.id.btn_tin_tuc).setOnClickListener(v ->
                startActivity(new Intent(this, TinTucActivity.class)));
        findViewById(R.id.btn_bao_cao_tl).setOnClickListener(v ->
                startActivity(new Intent(this, BaoCaoTheLoaiActivity.class)));
        findViewById(R.id.btn_bao_cao_ngay).setOnClickListener(v ->
                startActivity(new Intent(this, BaoCaoNgayActivity.class)));
    }
}
