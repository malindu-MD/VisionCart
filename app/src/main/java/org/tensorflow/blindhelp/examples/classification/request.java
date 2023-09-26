package org.tensorflow.blindhelp.examples.classification;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.adapters.RequestList;
import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class request extends AppCompatActivity {

    private RecyclerView recyclerView;

    private TextToSpeech tts;

    private ArrayList<VolunteerRequest> volunteerRequests;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        recyclerView = findViewById(R.id.requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        volunteerRequests = new ArrayList<>();
        getProductData();















    }

    private void getProductData() {
        database = FirebaseDatabase.getInstance().getReference("VolunteerRequest");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        VolunteerRequest product = productSnapshot.getValue(VolunteerRequest.class);
                        if (product != null) {
                            if(product.getStatus().equals("Not accept")){
                                volunteerRequests.add(product);


                            }
                        }
                    }
                    RequestList mAdapter = new RequestList(volunteerRequests);
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new RequestList.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {












                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

}