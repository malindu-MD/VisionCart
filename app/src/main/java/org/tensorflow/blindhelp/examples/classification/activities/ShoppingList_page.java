package org.tensorflow.blindhelp.examples.classification.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.tensorflow.blindhelp.examples.classification.Add_WishList;
import org.tensorflow.blindhelp.examples.classification.ShoppingList;
import org.tensorflow.blindhelp.examples.classification.View_WishList_Page;
import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class ShoppingList_page extends AppCompatActivity implements View.OnLongClickListener,View.OnClickListener,TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    //   CardView ViewShoppingListID;
    CardView addWishList;
    CardView homeIDDD;
    CardView ViewWishListID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_page);

        //   ViewShoppingListID = findViewById(R.id.ViewShoppingListID);
        homeIDDD = findViewById(R.id.homeIDDD);
        addWishList = findViewById(R.id.addWishList);
        ViewWishListID = findViewById(R.id.ViewWishListID);


        //     ViewShoppingListID.setOnClickListener(this);
        homeIDDD.setOnClickListener(this);
        addWishList.setOnClickListener(this);
        ViewWishListID.setOnClickListener(this);

        //      ViewShoppingListID.setOnLongClickListener(this);
        homeIDDD.setOnLongClickListener(this);
        addWishList.setOnLongClickListener(this);
        ViewWishListID.setOnLongClickListener(this);


        tts = new TextToSpeech(this, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            tts.speak("You are Now shopping list handle page", TextToSpeech.QUEUE_FLUSH, null, null);

        }
    }

    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.homeIDDD) {
            text = "Please Long Press to go home Page";
        }
//         else if (view.getId() == R.id.ViewShoppingListID) {
//            text = "Please Long Press to go View Shopping List";
//        }
        else if (view.getId() == R.id.ViewWishListID) {
            text = "Please Long Press to View wish list page";
        }
        else if (view.getId() == R.id.addWishList) {
            text = "Please Long Press to Add item to wish List";
        }else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }




    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.homeIDDD) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        }
//        else if (view.getId() == R.id.ViewShoppingListID) {
//            intent = new Intent(this, ShoppingList.class);
//            startActivity(intent);
//            return true;
//        }
        else if (view.getId() == R.id.addWishList) {
            intent = new Intent(this, Add_WishList.class);
            startActivity(intent);
            return true;
        }else if (view.getId() == R.id.ViewWishListID) {
            intent = new Intent(this, View_WishList_Page.class);
            startActivity(intent);
            return true;
        }else {
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