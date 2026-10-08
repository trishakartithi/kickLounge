
package com.example.kicklounge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminBookingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Booking> bookingList;

    public AdminBookingAdapter(Context context, ArrayList<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @Override
    public int getCount() {
        return bookingList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_admin_booking, parent, false);
        }


        TextView fieldText = convertView.findViewById(R.id.tvFieldName);
        TextView emailText = convertView.findViewById(R.id.tvUserEmail);
        TextView nameText = convertView.findViewById(R.id.tvName);
        TextView dateTimeText = convertView.findViewById(R.id.tvDateTime);
        TextView amountText = convertView.findViewById(R.id.tvAmount);
        TextView paymentText = convertView.findViewById(R.id.tvPayment);
        Button cancelButton = convertView.findViewById(R.id.btnCancelBooking);

        Booking booking = bookingList.get(position);

        fieldText.setText(booking.fieldName);
        emailText.setText("Email: " + booking.email);
        nameText.setText("Name: " + booking.firstName + " " + booking.lastName);
        dateTimeText.setText("Date: " + booking.date + " | Time: " + booking.time);
        amountText.setText("Amount: " + booking.amount);
        paymentText.setText("Payment: " + booking.payment);


        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Booking cancelled: " + booking.fieldName, Toast.LENGTH_SHORT).show();

            bookingList.remove(position);
            notifyDataSetChanged();

            if (context instanceof AdminDashboardActivity) {
                ((AdminDashboardActivity) context).cancelBooking(booking);
            }
        });

        return convertView;
    }
}
