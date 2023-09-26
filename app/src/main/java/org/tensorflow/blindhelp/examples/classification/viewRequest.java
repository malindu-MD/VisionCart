package org.tensorflow.blindhelp.examples.classification;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.adapters.RequestAdapter;
import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class viewRequest extends AppCompatActivity  implements View.OnClickListener, TextToSpeech.OnInitListener{

    private RecyclerView recyclerView;

    private TextToSpeech tts;

    private ArrayList<VolunteerRequest> volunteerRequests;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);




        tts = new TextToSpeech((Context) this, (TextToSpeech.OnInitListener) this);

        recyclerView = findViewById(R.id.requestView);
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
                            volunteerRequests.add(product);
                        }
                    }
                    RequestAdapter mAdapter = new RequestAdapter(volunteerRequests);
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                         String status=volunteerRequests.get(position).getStatus();
                         String volunteer=volunteerRequests.get(position).getVolunteer();



                             speak("Your request is "+status+"\n");




                             speak("Your request is "+status+"\nYour volunteer is"+volunteer);









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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            tts.speak("Welcome to My request page. Single tap for details and long press to open an activity.", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


    @Override
    public void onClick(View view) {

    }
}