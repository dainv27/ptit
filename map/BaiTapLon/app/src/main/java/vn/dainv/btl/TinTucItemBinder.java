package vn.dainv.btl;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import vn.dainv.btl.database.NewsDbHelper;
import vn.dainv.btl.model.TinTuc;

public final class TinTucItemBinder {

    private TinTucItemBinder() {
    }

    public static void bind(
            AppCompatActivity activity,
            View row,
            TinTuc n,
            NewsDbHelper db,
            boolean showDeleteButton,
            @Nullable View.OnClickListener deleteListener) {
        ((TextView) row.findViewById(R.id.item_ma_tin)).setText(
                activity.getString(R.string.ma_tin) + ": " + n.getMaTin());
        ((TextView) row.findViewById(R.id.item_tieu_de)).setText(
                activity.getString(R.string.tieu_de) + ": " + n.getTieuDe());
        ((TextView) row.findViewById(R.id.item_chi_tiet)).setText(
                activity.getString(R.string.chi_tiet) + ": " + n.getChiTiet());

        // Find item by id
        ImageView hinhAnh = row.findViewById(R.id.item_hinh_anh);
        String linkTrim = TextUtils.isEmpty(n.getLinkHinh()) ? "" : n.getLinkHinh().trim();
        if (linkTrim.isEmpty()) {
            Glide.with(activity).clear(hinhAnh);
            hinhAnh.setImageDrawable(null);
            hinhAnh.setVisibility(View.GONE);
        } else {
            hinhAnh.setVisibility(View.VISIBLE);
            hinhAnh.setContentDescription(
                    activity.getString(R.string.link_hinh) + " — " + n.getTieuDe());
            // Load image by GLine support caching resource
            Glide.with(activity)
                    .load(linkTrim)
                    .centerCrop()
                    .into(hinhAnh);
        }

        ((TextView) row.findViewById(R.id.item_ten_loai_tin)).setText(
                activity.getString(R.string.loai_tin) + ": " + db.getTenLoai(n.getMaLoai()));
        ((TextView) row.findViewById(R.id.item_ngay_dang)).setText(
                activity.getString(R.string.list_ngay_dang) + ": " + n.getNgayDang());

        View del = row.findViewById(R.id.btn_delete_tin);
        if (showDeleteButton) {
            del.setVisibility(View.VISIBLE);
            del.setOnClickListener(deleteListener);
        } else {
            del.setVisibility(View.GONE);
            del.setOnClickListener(null);
        }
    }
}
