package org.tensorflow.blindhelp.examples.classification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.blindhelp.examples.classification.adapters.Completed;
import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;
import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class completedRequest extends AppCompatActivity {

    private ImageView back3;
    private TextView text;

    private RecyclerView recyclerView;

    private TextToSpeech tts;

    private ImageView back2;
    private ArrayList<VolunteerRequest> volunteerRequests;

    private Completed mAdapter;
    private DatabaseReference database;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_request);


        back3=findViewById(R.id.back2);
        text=findViewById(R.id.accept);
        sessionManager = new SessionManager(this);


        recyclerView = findViewById(R.id.completeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        volunteerRequests = new ArrayList<>();

        getProductData();





        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(completedRequest.this, VDashboard.class);
                startActivity(intent);


            }
        });

        text.setText("Completed Request");

    }


    private void getProductData() {
        database = FirebaseDatabase.getInstance().getReference("VolunteerRequest");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                volunteerRequests.clear();

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        VolunteerRequest product = productSnapshot.getValue(VolunteerRequest.class);

                            if((product.getVolunteer().equals(sessionManager.getUserId())) && (product.getStatus().equals("Completed"))){
                                volunteerRequests.add(product);


                            }

                    }
                    mAdapter = new Completed(volunteerRequests, new Completed.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                        }

                        @Override
                        public void onDeleteButtonClick(int position) {



                            AlertDialog.Builder builder = new AlertDialog.Builder(completedRequest.this);


                            builder.setTitle("Are You Sure You Want to delete It?"); // Set the dialog title

                            // Add a button to close the dialog
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    VolunteerRequest request = volunteerRequests.get(position);

                                    String requestId = request.getID();

                                    database.child(requestId).removeValue();




                                }
                            });


                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            // Create and show the AlertDialog
                            AlertDialog dialog = builder.create();
                            dialog.show();



                        }
                    });





                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }


    private void showDetailsDialog(int position) {

    }

}