package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FieldDetailsActivity extends AppCompatActivity {

    ImageView ivFieldImage;
    TextView tvFieldName, tvFieldInfo;
    Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_details);

        ivFieldImage = findViewById(R.id.ivFieldImage);
        tvFieldName = findViewById(R.id.tvFieldName);
        tvFieldInfo = findViewById(R.id.tvFieldInfo);
        btnBookNow = findViewById(R.id.btnBookNow);

        String fieldName = getIntent().getStringExtra("fieldName");
        int imageRes = getIntent().getIntExtra("fieldImage", 0);
        String fieldInfo = getIntent().getStringExtra("fieldInfo");

        tvFieldName.setText(fieldName);
        ivFieldImage.setImageResource(imageRes);
        tvFieldInfo.setText(Html.fromHtml(fieldInfo));

        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(FieldDetailsActivity.this, BookingFormActivity.class);
            intent.putExtra("fieldName", fieldName);
            startActivity(intent);
        });
    }
}
