package com.example.kicklounge;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ViewFlipper offerBanner;
    private EditText searchBar;
    private ImageView fieldImage1, fieldImage2, fieldImage3, fieldImage4, fieldImage5, fieldImage6;
    private Button btnAdd1, btnAdd2, btnAdd3, btnAdd4, btnAdd5, btnAdd6;
    private final ArrayList<Field> fields = new ArrayList<>();
    private static class Field {
        String name;
        int imageRes;
        String info;
        
        Field(String name, int imageRes, String info) {
            this.name = name;
            this.imageRes = imageRes;
            this.info = info;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        offerBanner = view.findViewById(R.id.offerBanner);
        offerBanner.setAutoStart(true);
        offerBanner.setFlipInterval(3000);
        offerBanner.startFlipping();
        searchBar = view.findViewById(R.id.searchBar);
        initFields();
        setupSearchBar();

        fieldImage1 = view.findViewById(R.id.fieldImage1);
        fieldImage2 = view.findViewById(R.id.fieldImage2);
        fieldImage3 = view.findViewById(R.id.fieldImage3);
        fieldImage4 = view.findViewById(R.id.fieldImage4);
        fieldImage5 = view.findViewById(R.id.fieldImage5);
        fieldImage6 = view.findViewById(R.id.fieldImage6);

        btnAdd1 = view.findViewById(R.id.btnAdd1);
        btnAdd2 = view.findViewById(R.id.btnAdd2);
        btnAdd3 = view.findViewById(R.id.btnAdd3);
        btnAdd4 = view.findViewById(R.id.btnAdd4);
        btnAdd5 = view.findViewById(R.id.btnAdd5);
        btnAdd6 = view.findViewById(R.id.btnAdd6);
        setupFieldClickListeners();
        setupAddToCartListeners();
        return view;
    }


    private void initFields() {
        fields.add(new Field("Sports Heaven", R.drawable.field1,
                "<b>Sports Heaven, Sylhet</b> is a popular indoor sports complex located on Technical Road.<br><br>" +
                        "<b>Located in:</b> Sylhet Technical School & College<br>" +
                        "<b>Address:</b> Technical Rd, Sylhet 3100<br>" +
                        "<b>Hours:</b> Open 10 AM – Closes 12 AM<br>" +
                        "<b>Fee:</b> Day Shift: 600 Taka, Night Shift: 800 Taka"));

        fields.add(new Field("Soccer Zone", R.drawable.field2,
                "<b>Soccer Zone, Sylhet</b> is a highly regarded indoor sports arena.<br><br>" +
                        "<b>Located in:</b> Kazitula Road, Sylhet<br>" +
                        "<b>Address:</b> 31/E (beside Shahjalal Servicing Center, Sylhet 3100)<br>" +
                        "<b>Hours:</b> Open 10 AM – Closes 12 AM<br>" +
                        "<b>Fee:</b> Day Shift: 500 Taka, Night Shift: 800 Taka"));

        fields.add(new Field("Avalon", R.drawable.field3,
                "<b>Avalon, Sylhet</b> blends a sports club with a restaurant, creating a popular community destination.<br><br>" +
                        "<b>Located in:</b> Sreerampur, Sylhet<br>" +
                        "<b>Address:</b> Shahporan Bridge Bypass, Sreerampur 3111<br>" +
                        "<b>Hours:</b> Sun–Sat varies 10 AM–12 AM<br>" +
                        "<b>Fee:</b> Day Shift: 700 Taka, Night Shift: 900 Taka"));

        fields.add(new Field("Crossbar", R.drawable.field4,
                "<b>Crossbar Indoor</b> is a dedicated indoor turf facility.<br><br>" +
                        "<b>Address:</b> Sheikh Akram Ullah Rd, Sylhet<br>" +
                        "<b>Hours:</b> Open 9 AM – Closes 12 AM<br>" +
                        "<b>Fee:</b> Day Shift: 600 Taka, Night Shift: 900 Taka"));

        fields.add(new Field("Khan Sports Centre", R.drawable.field5,
                "<b>Khan Sports Centre</b> offers a dedicated space for recreational sports.<br><br>" +
                        "<b>Address:</b> Bonkolapara, Sylhet<br>" +
                        "<b>Hours:</b> Open 10 AM – Closes 12 AM<br>" +
                        "<b>Fee:</b> Day Shift: 800 Taka, Night Shift: 900 Taka"));

        fields.add(new Field("GOAL", R.drawable.field6,
                "<b>GOAL</b> is an established indoor sports facility popular for football enthusiasts.<br><br>" +
                        "<b>Address:</b> Ad Bangla Media, Bhatipara House, 35/2 Kumarpara Rd, Sylhet 3100<br>" +
                        "<b>Hours:</b> Open 9 AM – Closes 12 AM<br>" +
                        "<b>Fee:</b> Day Shift: 600 Taka, Night Shift: 700 Taka"));
    }

    private void setupSearchBar() {
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                String query = searchBar.getText().toString().trim().toLowerCase();
                boolean found = false;

                for (Field f : fields) {
                    if (f.name.toLowerCase().contains(query)) {
                        openFieldDetails(f);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(getContext(), "No result found", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
            return false;
        });
    }

    private void setupFieldClickListeners() {
        fieldImage1.setOnClickListener(v -> openFieldDetails(fields.get(0)));
        fieldImage2.setOnClickListener(v -> openFieldDetails(fields.get(1)));
        fieldImage3.setOnClickListener(v -> openFieldDetails(fields.get(2)));
        fieldImage4.setOnClickListener(v -> openFieldDetails(fields.get(3)));
        fieldImage5.setOnClickListener(v -> openFieldDetails(fields.get(4)));
        fieldImage6.setOnClickListener(v -> openFieldDetails(fields.get(5)));
    }

    private void setupAddToCartListeners() {
        btnAdd1.setOnClickListener(v -> addToCart(fields.get(0).name));
        btnAdd2.setOnClickListener(v -> addToCart(fields.get(1).name));
        btnAdd3.setOnClickListener(v -> addToCart(fields.get(2).name));
        btnAdd4.setOnClickListener(v -> addToCart(fields.get(3).name));
        btnAdd5.setOnClickListener(v -> addToCart(fields.get(4).name));
        btnAdd6.setOnClickListener(v -> addToCart(fields.get(5).name));
    }

    private void openFieldDetails(Field f) {
        Intent intent = new Intent(getActivity(), FieldDetailsActivity.class);
        intent.putExtra("fieldName", f.name);
        intent.putExtra("fieldImage", f.imageRes);
        intent.putExtra("fieldInfo", f.info);
        startActivity(intent);
    }

    private void addToCart(String itemName) {
        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).addToCart(itemName);
        }
    }
}
