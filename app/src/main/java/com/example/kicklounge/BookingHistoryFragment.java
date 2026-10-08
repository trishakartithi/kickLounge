package com.example.kicklounge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class BookingHistoryFragment extends Fragment {
    ListView listViewBookings;
    ArrayList<Booking> bookingList;
    BookingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking_history, container, false);
        listViewBookings = view.findViewById(R.id.listViewBookings);
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(getContext(), bookingList);
        listViewBookings.setAdapter(adapter);
        loadUserBookings();
        return view;
    }

    private void loadUserBookings() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Bookings")
                .child(user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Booking booking = snap.getValue(Booking.class);
                    if (booking != null) bookingList.add(booking);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
