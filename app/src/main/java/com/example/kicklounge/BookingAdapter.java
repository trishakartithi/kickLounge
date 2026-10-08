package com.example.kicklounge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class BookingAdapter extends BaseAdapter {

    Context context;
    ArrayList<Booking> bookings;

    public BookingAdapter(Context context, ArrayList<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @Override
    public int getCount() { return bookings.size(); }

    @Override
    public Object getItem(int position) { return bookings.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);

        TextView tvField = convertView.findViewById(R.id.tvFieldName);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDateTime = convertView.findViewById(R.id.tvDateTime);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvPayment = convertView.findViewById(R.id.tvPayment);

        Booking b = bookings.get(position);

        tvField.setText(b.fieldName);
        tvName.setText("Name: " + b.firstName + " " + b.lastName);
        tvDateTime.setText("Date: " + b.date + " | Time: " + b.time);
        tvAmount.setText("Amount: " + b.amount);
        tvPayment.setText("Payment: " + b.payment);

        return convertView;
    }
}