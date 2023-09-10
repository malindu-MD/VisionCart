package com.example.visioncart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.visioncart.R;
import com.example.visioncart.offersPage;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        CardView cardView = findViewById(R.id.offerbutton);

        // Set an OnClickListener on the CardView
        cardView.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(HomePage.this, offersPage.class);
            startActivity(intent);
        });
    }
}