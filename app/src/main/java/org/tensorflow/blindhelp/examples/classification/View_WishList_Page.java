package org.tensorflow.blindhelp.examples.classification;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
import org.tensorflow.blindhelp.examples.classification.adapters.wishListAdapter;
import org.tensorflow.blindhelp.examples.classification.models.WishList;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class View_WishList_Page extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener,View.OnLongClickListener {



    private RecyclerView recyclerWishList;
    private TextToSpeech tts;
    private ArrayList<WishList> WishList;
    private DatabaseReference database;
    CardView wishHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wish_list_page);

        tts = new TextToSpeech((Context) this, (TextToSpeech.OnInitListener) this);

        wishHome=findViewById(R.id.wishHome);
        wishHome.setOnClickListener(this);
        wishHome.setOnLongClickListener(this);

        recyclerWishList = findViewById(R.id.recyclerWishList);
        recyclerWishList.setLayoutManager(new LinearLayoutManager(this));
        recyclerWishList.setHasFixedSize(true);

        WishList = new ArrayList<>();
        getProductData();
    }

    private void getProductData() {
        database = FirebaseDatabase.getInstance().getReference("WishList");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        WishList product = productSnapshot.getValue(WishList.class);
                        if (product != null) {
                            WishList.add(product);
                        }
                    }
                    wishListAdapter mAdapter = new wishListAdapter(WishList);
                    recyclerWishList.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new wishListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            String wishDetails1=WishList.get(position).getDetaillsID();
                            String wishDetails2=WishList.get(position).getQuantityID();
                            System.out.println(wishDetails2);


                            speak("You select "+wishDetails1+"\n");

                        }

                        @Override
                        public void onDeleteButtonClick(int position) {

                                // Handle item deletion
                                if (position >= 0 && position < WishList.size()) {
                                    // Remove the item from the list
                                    WishList.remove(position);

                                    // Notify the adapter that an item has been removed
                                    mAdapter.notifyItemRemoved(position);
                                }



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
            tts.speak("These are the items You have added to wish list page", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.wishHome) {
            text = "Please Long Press to go to Home page";
        }else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.wishHome) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);


            return true;
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }
    }


    @Override
    protected void onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}