package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.adapters.AdminOfferAdapter;
import org.tensorflow.blindhelp.examples.classification.models.Offers;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewOfferAdmin extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    AdminOfferAdapter adminOfferAdapter;
    ArrayList<Offers> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_admin);

        Button button = findViewById(R.id.offerhome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the OffersActivity when the button is clicked
                Intent intent = new Intent(viewOfferAdmin.this, adminPanel.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.adminViewOffer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        getProductData();
    }

    private void getProductData() {
        database = FirebaseDatabase.getInstance().getReference("VisionCart");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list to avoid duplicate entries
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Offers offers = dataSnapshot.getValue(Offers.class);
                    list.add(offers);
                }

                adminOfferAdapter = new AdminOfferAdapter(list, new AdminOfferAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                    }

                    @Override
                    public void onUpdateButtonClick(int position) {
                        // Handle update button click here
                        // You can use the same data extraction logic as in onItemClick
                        // and pass the data to the UpdateOfferActivity for editing.
                        String pname = list.get(position).getPname();
                        String offerDetails = list.get(position).getOfferDetails();
                        String fromMonth = list.get(position).getFromMonth();
                        String fromDay = list.get(position).getFromday();
                        String toMonth = list.get(position).getToMonth();
                        String toDay = list.get(position).getToDay();

                        Intent intent = new Intent(viewOfferAdmin.this, addofferpage.class);
                        intent.putExtra("productName", pname);
                        intent.putExtra("fromMonth", fromMonth);
                        intent.putExtra("fromDay", fromDay);
                        intent.putExtra("toMonth", toMonth);
                        intent.putExtra("toDay", toDay);
                        intent.putExtra("offerDetails", offerDetails);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adminOfferAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}
