package com.example.visioncart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.example.visioncart.adapters.MyAdapter;
import com.example.visioncart.models.Offers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class offersPage extends AppCompatActivity implements View.OnClickListener,TextToSpeech.OnInitListener {

    RecyclerView recyclerView;
    private TextToSpeech tts;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Offers> list;

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
                        String fromMonth=list.get(position).getFromMonth();
                        String fromDay=list.get(position).getFromday();
                        String toMonth=list.get(position).getToMonth();
                        String toDay=list.get(position).getToDay();



                        speak(offerDetails+"for"+pname+"from"+fromMonth+fromDay+"To"+toMonth+toDay+"\n");






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

    }


}