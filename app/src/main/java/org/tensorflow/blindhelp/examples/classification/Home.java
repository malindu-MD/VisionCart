package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class Home extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech tts;
   private RelativeLayout msgBox;
    private RelativeLayout phoneMngr ;
    private RelativeLayout timeDate;
    private RelativeLayout cameraCard;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new TextToSpeech(this, this);

         msgBox = findViewById(R.id.msgBox);
         phoneMngr = findViewById(R.id.phoneMngr);
         timeDate = findViewById(R.id.timeDate);
         cameraCard = findViewById(R.id.cameraCard);

        msgBox.setOnClickListener(this);
        phoneMngr.setOnClickListener(this);
        timeDate.setOnClickListener(this);
        cameraCard.setOnClickListener(this);

        msgBox.setOnLongClickListener(this);
        phoneMngr.setOnLongClickListener(this);
        timeDate.setOnLongClickListener(this);
        cameraCard.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.msgBox) {
            text = "You clicked messaging!";
        } else if (view.getId() == R.id.phoneMngr) {
            text = "You clicked phone manager!";
        } else if (view.getId() == R.id.timeDate) {
            text = "You clicked Time/Date and Battery status!";
        } else if (view.getId() == R.id.cameraCard) {
            text = "You clicked phone camera!";
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.msgBox) {
            intent = new Intent(this, MainActivity.class);
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
