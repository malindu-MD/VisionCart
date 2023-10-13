package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.tensorflow.blindhelp.examples.classification.activities.addProducts;
import org.tensorflow.blindhelp.examples.classification.activities.admin_product_view;
import org.tensorflow.lite.examples.classification.R;

public class adminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        CardView viewOffers = (CardView) findViewById(R.id.viewOffers);
        CardView addoffer = (CardView) findViewById(R.id.addOffer);
        CardView addProductBtn = (CardView) findViewById(R.id.addProductBtn);
        CardView viewProductBtn = (CardView) findViewById(R.id.viewProductBtn);

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

        addProductBtn.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, addProducts.class);
            startActivity(intent);
        });

        viewProductBtn.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, admin_product_view.class);
            startActivity(intent);
        });
    }
}