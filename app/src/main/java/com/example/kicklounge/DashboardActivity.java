package com.example.kicklounge;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final ArrayList<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showFragment(new HomeFragment(), "HOME_FRAGMENT");
            } else if (id == R.id.nav_cart) {
                showCartDialog();
            } else if (id == R.id.nav_bookings) {
                showFragment(new BookingHistoryFragment(), "BOOKINGS_FRAGMENT");
            } else if (id == R.id.nav_profile) {
                showFragment(new ProfileFragment(), "PROFILE_FRAGMENT");
            } else if (id == R.id.nav_signout) {
                showFragment(new LogoutConfirmationFragment(), "SIGNOUT_FRAGMENT");
            }
            return true;
        });

        if (savedInstanceState == null) {
            showFragment(new HomeFragment(), "HOME_FRAGMENT");
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        loadCart();
    }
    private void showFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }
    private void loadCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("Carts")
                .child(user.getUid());

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String key = itemSnapshot.getKey();
                    CartItem item = itemSnapshot.getValue(CartItem.class);
                    if (item != null) {
                        item.key = key;
                        cartItems.add(item);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showCartDialog() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Cart");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cart, null);
        ListView cartListView = dialogView.findViewById(R.id.cartListView);
        Button closeButton = dialogView.findViewById(R.id.btnClose);

        CartAdapter adapter = new CartAdapter(this, cartItems);
        cartListView.setAdapter(adapter);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        closeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void addToCart(String itemName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference cartRef = FirebaseDatabase.getInstance()
                .getReference("Carts")
                .child(user.getUid());

        String key = cartRef.push().getKey();
        if (key == null) return;

        CartItem item = new CartItem(itemName, key);

        cartRef.child(key).setValue(item)
                .addOnSuccessListener(aVoid -> {
                    cartItems.add(item);
                    Toast.makeText(this, itemName + " added to cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show());
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }
}
