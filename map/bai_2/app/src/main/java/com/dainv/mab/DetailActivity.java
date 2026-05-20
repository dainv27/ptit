package com.dainv.mab;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;

public class DetailActivity extends ComponentActivity {
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_IMAGE_RES = "extra_image_res";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        ImageView itemImageView = findViewById(R.id.itemImageView);
        TextView itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);

        String name = getIntent().getStringExtra(EXTRA_NAME);
        String description = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        int imageRes = getIntent().getIntExtra(EXTRA_IMAGE_RES, android.R.drawable.ic_menu_report_image);

        itemNameTextView.setText(name == null ? "" : name);
        itemDescriptionTextView.setText(description == null ? "" : description);
        itemImageView.setImageResource(imageRes);
    }
}
