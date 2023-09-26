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

import org.tensorflow.blindhelp.examples.classification.sendRequest;
import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class helpDesk extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech tts;


    private CardView  sendRequest;
    private CardView  myRequest;

    private CardView home;








    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);

        tts = new TextToSpeech(this, this);

        sendRequest=findViewById(R.id.sendRequest);
        myRequest=findViewById(R.id.myRequest);
        home=findViewById(R.id.home);


        sendRequest.setOnClickListener(this);
        myRequest.setOnClickListener(this);
        home.setOnClickListener(this);


        sendRequest.setOnLongClickListener(this);
        myRequest.setOnLongClickListener(this);
        home.setOnLongClickListener(this);





    }

    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.sendRequest) {
            text = "You clicked send Request";
        } else if (view.getId() == R.id.myRequest) {
            text = "You clicked My Request ";
        }
        else if (view.getId() == R.id.home) {
            text = "You clicked Home ";
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
        if (view.getId() == R.id.sendRequest) {
            intent = new Intent(this, org.tensorflow.blindhelp.examples.classification.sendRequest.class);
            startActivity(intent);
            return true;
        } else if (view.getId() == R.id.myRequest) {
            intent = new Intent(this, org.tensorflow.blindhelp.examples.classification.viewRequest.class);
            startActivity(intent);
            return true;
        }
        else if (view.getId() == R.id.home) {
            intent = new Intent(this, org.tensorflow.blindhelp.examples.classification.activities.HomePage.class);
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
            tts.speak("Welcome to FIND VOLUNTEER page. Single tap for details and long press to open an activity.", TextToSpeech.QUEUE_FLUSH, null, null);
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
