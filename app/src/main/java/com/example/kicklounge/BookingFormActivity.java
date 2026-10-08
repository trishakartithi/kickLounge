package com.example.kicklounge;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.Calendar;

public class BookingFormActivity extends AppCompatActivity {
    TextView tvFieldName;
    EditText etFirstName, etLastName, etEmail, etPhone, etDate, etTime, etAmount;
    Spinner spPaymentMethod;
    Button btnSubmitBooking;
    private String fieldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);

        tvFieldName = findViewById(R.id.tvFieldName);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etAmount = findViewById(R.id.etAmount);
        spPaymentMethod = findViewById(R.id.spPaymentMethod);
        btnSubmitBooking = findViewById(R.id.btnPlaceBooking);
        fieldName = getIntent().getStringExtra("fieldName");
        tvFieldName.setText(fieldName != null ? fieldName : "Selected Field");


        String[] paymentNames = {"bKash", "Nagad", "Credit/Debit Card"};
        int[] paymentLogos = {R.drawable.bkash, R.drawable.nogod, R.drawable.card};
        PaymentAdapter adapter = new PaymentAdapter(this, paymentNames, paymentLogos);
        spPaymentMethod.setAdapter(adapter);

        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(this,
                    (view, y, m, d) -> etDate.setText(String.format("%02d/%02d/%d", d, m + 1, y)),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });


        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            TimePickerDialog tp = new TimePickerDialog(this,
                    (view, h, min) -> etTime.setText(String.format("%02d:%02d", h, min)),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
            tp.show();
        });
        btnSubmitBooking.setOnClickListener(v -> submitBooking());
    }

    private void submitBooking() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String payment = spPaymentMethod.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || date.isEmpty() || time.isEmpty() || amount.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showToast("Invalid email");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            showToast("Please sign in first");
            return;
        }

        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean conflictFound = false;

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot bookingSnap : userSnap.getChildren()) {
                        Booking existing = bookingSnap.getValue(Booking.class);
                        if (existing == null) continue;

                        if (existing.fieldName.equals(fieldName) && existing.date.equals(date)) {
                            try {
                                String[] oldT = existing.time.split(":");
                                String[] newT = time.split(":");

                                int oldH = Integer.parseInt(oldT[0]);
                                int oldM = Integer.parseInt(oldT[1]);
                                int newH = Integer.parseInt(newT[0]);
                                int newM = Integer.parseInt(newT[1]);
                                int oldTotal = oldH * 60 + oldM;
                                int newTotal = newH * 60 + newM;
                                if (Math.abs(newTotal - oldTotal) < 60) {
                                    conflictFound = true;
                                    break;
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    if (conflictFound) break;
                }

                if (conflictFound) {
                    showToast("This slot is already booked! Please choose another time.");
                } else {
                    saveBooking(user, firstName, lastName, email, phone, date, time, amount, payment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error checking slot availability");
            }
        });
    }

    private void saveBooking(FirebaseUser user, String fn, String ln, String email,
                             String phone, String date, String time, String amount, String payment) {

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Bookings")
                .child(user.getUid());
        String key = userRef.push().getKey();
        if (key == null) {
            showToast("Error generating booking key");
            return;
        }

        Booking booking = new Booking(fieldName, fn, ln, email, phone, date, time, amount, payment);
        booking.firebaseKey = key;
        booking.userUid = user.getUid();

        userRef.child(key).setValue(booking)
                .addOnSuccessListener(a -> {
                    showToast("Booking saved successfully!");
                    startActivity(new Intent(BookingFormActivity.this, BookingActivity.class)
                            .putExtra("bookingKey", key));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Failed to save booking"));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
