package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class HomePage extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech tts;


    private CardView  qrCode;
    private CardView  help;
    private CardView  cash;
    private CardView  toDo;
    private CardView  offers;
    private CardView  profile;







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
        profile=findViewById(R.id.profile);

        qrCode.setOnClickListener(this);
        help.setOnClickListener(this);
        cash.setOnClickListener(this);
        toDo.setOnClickListener(this);
        offers.setOnClickListener(this);
        profile.setOnClickListener(this);

        qrCode.setOnLongClickListener(this);
        help.setOnLongClickListener(this);
        cash.setOnLongClickListener(this);
        toDo.setOnLongClickListener(this);
        offers.setOnLongClickListener(this);
        profile.setOnLongClickListener(this);


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
            text = "You clicked Shopping List!";
        }else if (view.getId() == R.id.offers) {
            text = "You clicked Discount Item List";
        }else if (view.getId() == R.id.profile) {
            text = "You clicked My Profile";
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
        if (view.getId() == R.id.cash) {
            intent = new Intent(this, ClassifierActivity.class);
            startActivity(intent);
            return true;
        } else {
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