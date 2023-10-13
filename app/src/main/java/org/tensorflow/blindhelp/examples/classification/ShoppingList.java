package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
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

public class ShoppingList extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener, View.OnLongClickListener {

    private RecyclerView recyclerList;
    private TextToSpeech tts;
    private ArrayList<CartList> cartList;
    ShoppingListAdapter shoppingListAdapter;
    private DatabaseReference database;
    private Button readItemBtn;
    private int currentItemIndex = 0; // Track the current item being read

    CardView cardHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        tts = new TextToSpeech(this, this);

        readItemBtn = findViewById(R.id.readItemBtn);

        cardHome = findViewById(R.id.cardHome);
        cardHome.setOnClickListener(this);
        cardHome.setOnLongClickListener(this);

        recyclerList = findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setHasFixedSize(true);

        cartList = new ArrayList<>();
//        shoppingListAdapter = new ShoppingListAdapter(this,cartList);
//        recyclerList.setAdapter(shoppingListAdapter);
        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("CartList"); // Make sure this is correctly pointing to your database reference

        getProductData();

        readItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if there are items to read
                if (currentItemIndex < cartList.size()) {
                    String namep = cartList.get(currentItemIndex).getName();
                    String pricepp = cartList.get(currentItemIndex).getPrice();
                    String mfgpp = cartList.get(currentItemIndex).getMfg();
                    String exppp = cartList.get(currentItemIndex).getExp();
                    String detailspp = cartList.get(currentItemIndex).getDetails();
                    speak("Item Number " + (currentItemIndex + 1) + "\n" + namep+pricepp+mfgpp+exppp+detailspp+"\nLong press the button to remove it");
                    currentItemIndex++; // Move to the next item
                } else {
                    speak("No more items to read.");
                }
            }
        });

        // Add a long-press listener to the readItemBtn
        readItemBtn.setOnLongClickListener(this);
    }

    private void getProductData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        CartList product = productSnapshot.getValue(CartList.class);
                        if (product != null) {
                            // Store the key associated with each item
                            product.setKey(productSnapshot.getKey());
                            cartList.add(product);
                        }
                    }
                    ShoppingListAdapter mAdapter = new ShoppingListAdapter(cartList);
                    recyclerList.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String pname = cartList.get(position).getName();
                            String pprice = cartList.get(position).getPrice();
                            String pmfg = cartList.get(position).getMfg();
                            String pexp = cartList.get(position).getExp();
                            String pdetails = cartList.get(position).getDetails();



                            speak("You selected " + pname+pprice+pmfg+pexp+pdetails);
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
            tts.speak("press button for the speak cart list", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.cardHome) {
            text = "Please Long Press to go to the Home page";
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.cardHome) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        } else if (view.getId() == R.id.readItemBtn) {
            // Check if there is an item to delete and it's not out of bounds
            if (currentItemIndex >= 0 && currentItemIndex < cartList.size()) {
                currentItemIndex--;
                // Get the key of the item to be deleted
                String itemKey = cartList.get(currentItemIndex).getKey();

                // Remove the item from the Firebase Realtime Database
                DatabaseReference itemRef = database.child(itemKey);
                itemRef.removeValue()
                        .addOnSuccessListener(aVoid -> {
                            // Item deleted successfully from the database
                            speak("Item deleted.");
                            // Delete the item from the local list
                            deleteCurrentItem();
                        })
                        .addOnFailureListener(e -> {
                            // Handle the failure to delete the item
                            speak("Failed to delete item.");
                        });
            } else {
                speak("No item to delete.");
            }
            return true;
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }
    }

    private void deleteCurrentItem() {
        if (currentItemIndex >= 0 && currentItemIndex < cartList.size()) {
            cartList.remove(currentItemIndex);
            speak("Item delete");
            recreate();
            recyclerList.getAdapter().notifyDataSetChanged();

            if (currentItemIndex < cartList.size()) {
                String cartDetails = cartList.get(currentItemIndex).getName();
                speak("Item Number " + (currentItemIndex + 1) + "\n" + cartDetails );
            } else if (cartList.isEmpty()) {
                speak("No more items to read.");
            } else {
                // Decrement the index to move to the previous item
                currentItemIndex--;
                String cartDetails = cartList.get(currentItemIndex).getName();
                speak("Item Number " + (currentItemIndex + 1) + "\n" + cartDetails);
            }
        } else {
            speak("No item to delete.");
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown TTS;
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}
