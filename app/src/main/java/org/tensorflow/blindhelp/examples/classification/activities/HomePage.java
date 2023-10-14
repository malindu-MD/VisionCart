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

import org.tensorflow.blindhelp.examples.classification.CameraTest;
import org.tensorflow.blindhelp.examples.classification.ClassifierActivity;

import org.tensorflow.blindhelp.examples.classification.ShoppingList;
import org.tensorflow.blindhelp.examples.classification.offersPage;
import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class HomePage extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech tts;


    private CardView  qrCode;
    private CardView  help;
    private CardView  cash;
    private CardView  toDo;
    private CardView  offers;
    private CardView  cart;









    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        tts = new TextToSpeech(this, this);

        qrCode=findViewById(R.id.qrCode);
        help=findViewById(R.id.help);
        cash=findViewById(R.id.cash);
        toDo=findViewById(R.id.toDo);
        offers=findViewById(R.id.offers);
        cart=findViewById(R.id.cart);



        qrCode.setOnClickListener(this);
        help.setOnClickListener(this);
        cash.setOnClickListener(this);
        toDo.setOnClickListener(this);
        offers.setOnClickListener(this);
        cart.setOnClickListener(this);

        qrCode.setOnLongClickListener(this);
        help.setOnLongClickListener(this);
        cash.setOnLongClickListener(this);
        toDo.setOnLongClickListener(this);
        offers.setOnLongClickListener(this);
        cart.setOnLongClickListener(this);


    }

    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.qrCode) {
            text = "You clicked QR Code Scanner!";
        } else if (view.getId() == R.id.help) {
            text = "You clicked volunteer help";
        } else if (view.getId() == R.id.cash) {
            text = "You clicked Currency Detector";
        } else if (view.getId() == R.id.toDo) {
            text = "You clicked Wish List!";
        }else if (view.getId() == R.id.offers) {
            text = "You clicked Discount Item List";
        }else if (view.getId() == R.id.cart) {
            text = "You clicked My Shopping Cart";
        }
        else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.help) {
            intent = new Intent(this, helpDesk.class);
            startActivity(intent);
            return true;
        }  else if (view.getId() == R.id.offers) {
            intent = new Intent(this, offersPage.class);
            startActivity(intent);
            return true;
        }
        else if (view.getId() == R.id.qrCode) {
            intent = new Intent(this, Camera.class);
            startActivity(intent);
            return true;
        }
        else if (view.getId() == R.id.toDo) {
            intent = new Intent(this, ShoppingList_page.class);
            startActivity(intent);
            return true;
        }
        else if (view.getId() == R.id.cash) {
            intent = new Intent(this, CameraTest.class);
            startActivity(intent);
            return true;
        }
        else if (view.getId() == R.id.cart) {
            intent = new Intent(this, ShoppingList.class);
            startActivity(intent);
            return true;
        }

        else {
            throw new IllegalArgumentException("Undefined Clicked");
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            tts.speak("Welcome to Vision Cart. Single tap for details and long press to open an activity.", TextToSpeech.QUEUE_FLUSH, null, null);
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
