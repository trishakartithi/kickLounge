package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {

    TextView tvBookingConfirm;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        tvBookingConfirm = findViewById(R.id.tvBookingConfirm);
        btnConfirm = findViewById(R.id.btnConfirm);


        tvBookingConfirm.setText("Your booking has been placed successfully.");

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
