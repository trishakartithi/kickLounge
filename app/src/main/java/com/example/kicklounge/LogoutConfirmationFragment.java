package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutConfirmationFragment extends Fragment {

    private TextView tvMessage;
    private Button btnConfirmLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_logout_confirmation, container, false);


        tvMessage = view.findViewById(R.id.tvMessage);
        btnConfirmLogout = view.findViewById(R.id.btnConfirmLogout);


        tvMessage.setText("You are trying to log out of KickLounge.\n\nDo you really want to log out?");


        btnConfirmLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();


                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
