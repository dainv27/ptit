package com.dainv.mab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.ComponentActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ComponentActivity {
    private Spinner categorySpinner;
    private RadioGroup categoryRadioGroup;
    private ListView itemsListView;

    private final List<Item> schoolSupplies = Arrays.asList(
            new Item("Sách", "Sách dùng để học tập và tra cứu kiến thức.", android.R.drawable.ic_menu_edit),
            new Item("Thước kẻ", "Thước kẻ dùng để đo độ dài và kẻ đường thẳng.", android.R.drawable.ic_menu_crop),
            new Item("Bút chì", "Bút chì dùng để viết hoặc phác thảo.", android.R.drawable.ic_menu_edit),
            new Item("Tẩy", "Tẩy dùng để xóa nét bút chì.", android.R.drawable.ic_menu_delete)
    );

    private final List<Item> electronics = Arrays.asList(
            new Item("Tai nghe", "Tai nghe dùng để nghe âm thanh cá nhân.", android.R.drawable.ic_media_play),
            new Item("Ốp lưng", "Ốp lưng bảo vệ điện thoại khỏi va đập.", android.R.drawable.ic_menu_gallery),
            new Item("Chuột", "Chuột hỗ trợ thao tác trên máy tính.", android.R.drawable.ic_menu_manage),
            new Item("USB", "USB dùng để lưu trữ và sao chép dữ liệu.", android.R.drawable.ic_menu_save),
            new Item("Bàn phím không dây", "Bàn phím kết nối Bluetooth hoặc receiver.", android.R.drawable.ic_menu_send)
    );

    private List<Item> currentItems = schoolSupplies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categorySpinner = findViewById(R.id.categorySpinner);
        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        itemsListView = findViewById(R.id.itemsListView);

        setupSpinner();
        setupRadioGroup();
        setupListClick();
        updateListForCategory(getSelectedCategory());
    }

    private void setupSpinner() {
        List<String> categoryOptions = Arrays.asList(
                getString(R.string.category_school_supplies),
                getString(R.string.category_electronics)
        );

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryOptions
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    categoryRadioGroup.check(R.id.rbSchoolSupplies);
                } else {
                    categoryRadioGroup.check(R.id.rbElectronics);
                }
                updateListForCategory(getSelectedCategory());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No-op.
            }
        });
    }

    private void setupRadioGroup() {
        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSchoolSupplies && categorySpinner.getSelectedItemPosition() != 0) {
                categorySpinner.setSelection(0);
            } else if (checkedId == R.id.rbElectronics && categorySpinner.getSelectedItemPosition() != 1) {
                categorySpinner.setSelection(1);
            }
            updateListForCategory(getSelectedCategory());
        });
    }

    private void setupListClick() {
        itemsListView.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = currentItems.get(position);
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
            detailIntent.putExtra(DetailActivity.EXTRA_NAME, selectedItem.getName());
            detailIntent.putExtra(DetailActivity.EXTRA_DESCRIPTION, selectedItem.getDescription());
            detailIntent.putExtra(DetailActivity.EXTRA_IMAGE_RES, selectedItem.getImageResId());
            startActivity(detailIntent);
        });
    }

    private String getSelectedCategory() {
        if (categoryRadioGroup.getCheckedRadioButtonId() == R.id.rbElectronics) {
            return getString(R.string.category_electronics);
        }
        return getString(R.string.category_school_supplies);
    }

    private void updateListForCategory(String category) {
        if (category.equals(getString(R.string.category_electronics))) {
            currentItems = electronics;
        } else {
            currentItems = schoolSupplies;
        }

        String[] names = new String[currentItems.size()];
        for (int i = 0; i < currentItems.size(); i++) {
            names[i] = currentItems.get(i).getName();
        }

        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        );
        itemsListView.setAdapter(namesAdapter);
    }
}
