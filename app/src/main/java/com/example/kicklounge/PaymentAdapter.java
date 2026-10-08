package com.example.kicklounge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] paymentNames;
    private int[] paymentLogos;

    public PaymentAdapter(Context context, String[] paymentNames, int[] paymentLogos) {
        super(context, R.layout.spinner_item_payment, paymentNames);
        this.context = context;
        this.paymentNames = paymentNames;
        this.paymentLogos = paymentLogos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_payment, parent, false);

        ImageView imgLogo = convertView.findViewById(R.id.imgPaymentLogo);
        TextView tvName = convertView.findViewById(R.id.tvPaymentName);

        imgLogo.setImageResource(paymentLogos[position]);
        tvName.setText(paymentNames[position]);

        return convertView;
    }
}
