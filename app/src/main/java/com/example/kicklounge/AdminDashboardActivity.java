package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    private ListView bookingListView;
    private Button signOutButton;

    private ArrayList<Booking> bookingList;
    private AdminBookingAdapter adapter;
    private DatabaseReference bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        bookingListView = findViewById(R.id.lvAdminBookings);
        signOutButton = findViewById(R.id.btnAdminSignOut);

        bookingList = new ArrayList<>();
        adapter = new AdminBookingAdapter(this, bookingList);
        bookingListView.setAdapter(adapter);

        bookingRef = FirebaseDatabase.getInstance().getReference("Bookings");

        loadAllBookings();

        signOutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Admin signed out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminDashboardActivity.this, WelcomeActivity.class));
            finish();
        });
    }

    private void loadAllBookings() {
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                bookingList.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot singleBooking : userSnap.getChildren()) {
                        Booking booking = singleBooking.getValue(Booking.class);
                        if (booking != null) {
                            booking.firebaseKey = singleBooking.getKey();
                            booking.userUid = userSnap.getKey();
                            bookingList.add(booking);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelBooking(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Do you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (booking.userUid != null && booking.firebaseKey != null) {
                        bookingRef.child(booking.userUid)
                                .child(booking.firebaseKey)
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Booking removed", Toast.LENGTH_SHORT).show();
                                    bookingList.remove(booking);
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
