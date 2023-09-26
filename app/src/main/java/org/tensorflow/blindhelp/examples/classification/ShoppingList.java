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

import org.tensorflow.blindhelp.examples.classification.adapters.ShoppingListAdapter;
import org.tensorflow.blindhelp.examples.classification.models.CartList;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ShoppingList extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener,View.OnLongClickListener{

    private RecyclerView recyclerList;
    private TextToSpeech tts;
    private ArrayList<CartList> CartList;
    private DatabaseReference database;

    CardView cardHome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        tts = new TextToSpeech((Context) this, (TextToSpeech.OnInitListener) this);

        cardHome=findViewById(R.id.cardHome);
        cardHome.setOnClickListener(this);
        cardHome.setOnLongClickListener(this);

        recyclerList = findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setHasFixedSize(true);

        CartList = new ArrayList<>();
        getProductData();
    }

    private void getProductData() {
        database = FirebaseDatabase.getInstance().getReference("CartList");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        CartList product = productSnapshot.getValue(CartList.class);
                        if (product != null) {
                            CartList.add(product);
                        }
                    }
                    ShoppingListAdapter mAdapter = new ShoppingListAdapter(CartList);
                    recyclerList.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            String CartDetails=CartList.get(position).getCartList();




                            speak("You select "+CartDetails+"\n");






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
            tts.speak("These are the items You have scanned and added to the list", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.cardHome) {
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
        if (view.getId() == R.id.cardHome) {
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