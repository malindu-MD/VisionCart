package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
import org.tensorflow.blindhelp.examples.classification.adapters.MyAdapter;
import org.tensorflow.blindhelp.examples.classification.models.Offers;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class offersPage extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener,TextToSpeech.OnInitListener {

    RecyclerView recyclerView;
    private TextToSpeech tts;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Offers> list;
    private CardView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        tts = new TextToSpeech(this, this);

        recyclerView = findViewById(R.id.offerView);
//
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        getProductData();

        home = (CardView)findViewById(R.id.homeCArdOffer);

        home.setOnClickListener(this);
        home.setOnLongClickListener(this);
//        home.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(offersPage.this, HomePage.class);
//                startActivity(intent);
//            }
//        });

    }
    private void getProductData(){
        database = FirebaseDatabase.getInstance().getReference("VisionCart");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Offers offers = dataSnapshot.getValue(Offers.class);
                    list.add(offers);


                }

                MyAdapter mAdapter = new MyAdapter(list);
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        String pname=list.get(position).getPname();
                        String offerDetails=list.get(position).getOfferDetails();
                        String dateStart=list.get(position).getDateStart();
                        String dateEnd=list.get(position).getDateEnd();




                        speak(offerDetails+"for"+pname+"from"+dateStart+"To"+dateEnd+"\n");






                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void onInit(int status) { // page ekata enakot ena eka
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            tts.speak("Welcome to View Offer Page. Single tap for hear details", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        String text = "";
        if (v.getId() == R.id.homeCArdOffer){
            text = "You clicked Home!";
        }
        speak(text);
    }


    @Override
    public boolean onLongClick(View v) {
        Intent intent;
        if (v.getId() == R.id.msgBox) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }
    }
}