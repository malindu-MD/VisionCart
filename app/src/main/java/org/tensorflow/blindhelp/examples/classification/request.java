package org.tensorflow.blindhelp.examples.classification;

import android.app.AlertDialog;
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

public class request extends AppCompatActivity  {

    private RecyclerView recyclerView;

    private TextToSpeech tts;

    private ImageView back1;

    private ArrayList<VolunteerRequest> volunteerRequests;

    private RequestList mAdapter;
    private DatabaseReference database;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        sessionManager = new SessionManager(this);

        back1=findViewById(R.id.back1);




        recyclerView = findViewById(R.id.requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        volunteerRequests = new ArrayList<>();
        getProductData();

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(request.this, VDashboard.class);
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
                            if(product.getStatus().equals("Not accept")){
                                volunteerRequests.add(product);


                            }
                        }
                    }
                     mAdapter = new RequestList(volunteerRequests);
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new RequestList.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {




                                showAcceptDialog(position);







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

    private void showAcceptDialog(int  position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);

        TextView requestTitle = dialogView.findViewById(R.id.request_title);
        Button acceptButton = dialogView.findViewById(R.id.accept_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        requestTitle.setText("Are you sure you want to accept this request?");

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VolunteerRequest request = volunteerRequests.get(position);

                if (request != null) {
                    String requestId = request.getID(); // Replace with your unique identifier
                    database.child(requestId).updateChildren(new HashMap<String, Object>() {{
                        put("volunteer", sessionManager.getUserId());
                        put("status", "Accepted");
                        put("volunteerName",sessionManager.getUsername());
                    }});


                    volunteerRequests.remove(position);
                     mAdapter.updateDataSet(volunteerRequests);
                      dialog.dismiss();




                }




            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }








}