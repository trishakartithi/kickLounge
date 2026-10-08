package com.example.kicklounge;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvAddress, tvAge;
    private Button btnEdit;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvAge = view.findViewById(R.id.tvAge);
        btnEdit = view.findViewById(R.id.btnEdit);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);


            tvEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "N/A");


            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvName.setText(snapshot.child("name").getValue(String.class) != null ? snapshot.child("name").getValue(String.class) : "Not added");
                    tvPhone.setText(snapshot.child("phone").getValue(String.class) != null ? snapshot.child("phone").getValue(String.class) : "Not added");
                    tvAddress.setText(snapshot.child("address").getValue(String.class) != null ? snapshot.child("address").getValue(String.class) : "Not added");
                    tvAge.setText(snapshot.child("age").getValue(String.class) != null ? snapshot.child("age").getValue(String.class) : "Not added");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext(), "No user logged in!", Toast.LENGTH_SHORT).show();
        }


        btnEdit.setOnClickListener(v -> showEditDialog());

        return view;
    }

    private void showEditDialog() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }


        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etAge = dialogView.findViewById(R.id.etAge);

        etAge.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);


        etName.setText(tvName.getText().toString().equals("Not added") ? "" : tvName.getText().toString());
        etPhone.setText(tvPhone.getText().toString().equals("Not added") ? "" : tvPhone.getText().toString());
        etAddress.setText(tvAddress.getText().toString().equals("Not added") ? "" : tvAddress.getText().toString());
        etAge.setText(tvAge.getText().toString().equals("Not added") ? "" : tvAge.getText().toString());


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Profile Info");
        builder.setView(dialogView);
        builder.setPositiveButton("Save", null); // override later
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String age = etAge.getText().toString().trim();


            if (!name.matches("^[a-zA-Z ]+$")) { etName.setError("Invalid name"); return; }
            if (!phone.matches("^(?:\\+?88)?01[3-9]\\d{8}$")) { etPhone.setError("Invalid phone"); return; }
            if (address.isEmpty()) { etAddress.setError("Address required"); return; }
            if (!age.matches("^[0-9]{1,3}$")) { etAge.setError("Invalid age"); return; }


            userRef.child("name").setValue(name);
            userRef.child("phone").setValue(phone);
            userRef.child("address").setValue(address);
            userRef.child("age").setValue(age);


            tvName.setText(name);
            tvPhone.setText(phone);
            tvAddress.setText(address);
            tvAge.setText(age);

            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }
}
