package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnAdminSignIn, btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Updated layout with admin section


        btnAdminSignIn = findViewById(R.id.btnAdminSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);


        btnAdminSignIn.setOnClickListener(v -> {
            Toast.makeText(this, "Admin Sign In Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this, AdminSignInActivity.class));
        });

        btnSignIn.setOnClickListener(v -> {
            Toast.makeText(this, "User Sign In Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this, SignInActivity.class));
        });


        btnSignUp.setOnClickListener(v -> {
            Toast.makeText(this, "User Sign Up Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
        });
    }
}
