package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSignInActivity extends AppCompatActivity {

    private EditText etAdminEmail, etAdminPassword;
    private Button btnAdminSignIn;
    private final String ADMIN_EMAIL = "admin@kicklounge.com";
    private final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin);

        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminSignIn = findViewById(R.id.btnAdminSignIn);

        btnAdminSignIn.setOnClickListener(v -> {
            String email = etAdminEmail.getText().toString().trim();
            String password = etAdminPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid admin", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
