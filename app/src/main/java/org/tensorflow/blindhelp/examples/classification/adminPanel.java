package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.tensorflow.lite.examples.classification.R;

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