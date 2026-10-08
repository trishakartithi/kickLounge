package com.example.kicklounge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {

    private final Context context;
    private final ArrayList<CartItem> items;

    public CartAdapter(Context context, ArrayList<CartItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        TextView tvItemName = convertView.findViewById(R.id.tvItemName);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnBookNow = convertView.findViewById(R.id.btnBookNow);

        CartItem item = items.get(position);
        tvItemName.setText(item.name);


        btnDelete.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && item.key != null) {
                DatabaseReference cartRef = FirebaseDatabase.getInstance()
                        .getReference("Carts")
                        .child(user.getUid())
                        .child(item.key);

                cartRef.removeValue().addOnSuccessListener(aVoid -> {
                    items.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
                );
            }
        });

        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingFormActivity.class);
            intent.putExtra("fieldName", item.name);

            if (context instanceof DashboardActivity) {
                ((DashboardActivity) context).startActivityForResult(intent, 101);
            } else {
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
