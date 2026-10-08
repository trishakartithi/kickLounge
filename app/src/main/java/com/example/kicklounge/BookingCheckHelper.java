package com.example.kicklounge;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingCheckHelper {

    public interface BookingCheckListener {
        void onResult(boolean isAvailable);
    }

    public static void isSlotAvailable(String fieldName, String date, BookingCheckListener listener) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookings");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean available = true;

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot bookingSnap : userSnap.getChildren()) {
                        Booking booking = bookingSnap.getValue(Booking.class);
                        if (booking == null) continue;


                        if (booking.fieldName.equals(fieldName) && booking.date.equals(date)) {
                            available = false;
                            break;
                        }
                    }
                    if (!available) break;
                }


                listener.onResult(available);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onResult(false);
            }
        });
    }
}
