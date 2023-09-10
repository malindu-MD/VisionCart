package com.example.visioncart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.visioncart.activities.HomePage;

public class adminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        CardView viewOffers = (CardView) findViewById(R.id.viewOffers);
        CardView addoffer = (CardView) findViewById(R.id.addOffer);

        addoffer.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, addofferpage.class);
            startActivity(intent);
        });

        viewOffers.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, viewOfferAdmin.class);
            startActivity(intent);
        });
    }
}