package org.tensorflow.blindhelp.examples.classification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;

public class acceptedRequest extends AppCompatActivity  {

    private RecyclerView recyclerView;

    private TextToSpeech tts;

    private ImageView back2;
    private ArrayList<VolunteerRequest> volunteerRequests;

    private RequestList mAdapter;
    private DatabaseReference database;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_request);

        sessionManager = new SessionManager(this);
        back2=findViewById(R.id.back2);


        recyclerView = findViewById(R.id.aaceptedrequestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        volunteerRequests = new ArrayList<>();
        getProductData();

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(acceptedRequest.this, VDashboard.class);
                startActivity(intent);


            }
        });















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
                            if((product.getVolunteer().equals(sessionManager.getUserId())) && product.getStatus().equals("Accepted")){
                                volunteerRequests.add(product);


                            }
                        }
                    }
                    mAdapter = new RequestList(volunteerRequests);
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new RequestList.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {




                            showDetailsDialog(position);







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

    private void showDetailsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        VolunteerRequest request = volunteerRequests.get(position);

        builder.setTitle("Message Of The Person"); // Set the dialog title
        builder.setMessage("Message:"+request.getMessage()); // Set the dialog message

        // Add a button to close the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog when the "OK" button is clicked
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }





}