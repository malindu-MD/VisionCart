package org.tensorflow.blindhelp.examples.classification;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
import org.tensorflow.blindhelp.examples.classification.activities.helpDesk;
import org.tensorflow.blindhelp.examples.classification.models.WishList;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class Add_WishList extends AppCompatActivity implements View.OnLongClickListener,View.OnClickListener{

    private TextToSpeech tts;
    private TextView QuantityID,detaillsID;
    private boolean IsInitialVoiceFinshed;
    private DatabaseReference wishListDBRef;
    private int numberOfClicks;
    CardView WishHomeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish_list);

        WishHomeID = findViewById(R.id.WishHomeID);
        WishHomeID.setOnClickListener(this);
        WishHomeID.setOnLongClickListener(this);

        IsInitialVoiceFinshed=false;

        wishListDBRef = FirebaseDatabase.getInstance().getReference("WishList");

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if((status==TextToSpeech.SUCCESS)){

                    int result=tts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","This Language is not supported");
                    }
                    speak("You Can Add products In Here.......  Give Product Details");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IsInitialVoiceFinshed=true;
                        }
                    },6000);



                }
                else{
                    Log.e("TTS","Initilization Failed");
                }

            }
        });
        detaillsID= (TextView) findViewById(R.id.detaillsID);
        QuantityID  =(TextView)findViewById(R.id.QuantityID);

        numberOfClicks = 0;


    }
    private void speak(String text){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else{
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void layoutClicked(View view)
    {
        if(IsInitialVoiceFinshed) {
            numberOfClicks++;
            listen();
        }
    }

    private void listen(){
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i,100);
        }catch (ActivityNotFoundException a){

            Toast.makeText(Add_WishList.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();


        }

    }

    private void exitFromApp(){
        this.finishAffinity();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100&& IsInitialVoiceFinshed){
            IsInitialVoiceFinshed = false;
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(result.get(0).equals("cancel"))
                {
                    speak("Cancelled!");
                    exitFromApp();
                }
                else {

                    //commewnt6 bhhbhad absbsj h sbnsbnsbsn

                    switch (numberOfClicks) {
                        case 1:
                            String to;
                            to= result.get(0).replaceAll("underscore","_");
                            to = to.replaceAll("\\s+","");
                            detaillsID.setText(to);
                            speak("please tell Quantity ");

                            break;
                        case 2:

                            QuantityID.setText(result.get(0));
                            speak("Please Confirm the Details\n Product Details : " + detaillsID.getText().toString() + "\nQuantity is : " + QuantityID.getText().toString() + "\n Say okay to confirm\n Say No To cancel ");
                            break;


                        default:
                            if(result.get(0).toLowerCase().equals("okay"))
                            {
                                insertDetails(detaillsID.getText().toString(),QuantityID.getText().toString());

                            }
                            else if(result.get(0).toLowerCase().equals("okay okay")){

                                insertDetails(detaillsID.getText().toString(),QuantityID.getText().toString());

                            }

                            else

                            {
                                numberOfClicks--;
                            }
                    }

                }
            }
            else {
                switch (numberOfClicks) {
                    case 1:
                        speak("Please tell Product Details");
                        break;
                    case 2:
                        speak("Please tell Quantity ");
                        break;
                    default:
                        speak("say okay or no");
                        break;
                }
                numberOfClicks--;
            }
        }
        IsInitialVoiceFinshed=true;
    }


    private void insertDetails(String detaillsID,String QuantityID) {

        Toast.makeText(Add_WishList.this, "Added to Wish List", Toast.LENGTH_SHORT).show();


        String id = wishListDBRef.push().getKey();

        String name = "Manoj";

        String status = "Not accept";

        WishList request = new WishList(detaillsID, QuantityID);

        assert id != null;

        wishListDBRef.child(id).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Data insertion was successful
                speak("Product Details Added to the Wish List");
            } else {
                // Data insertion failed
                speak("Product Details Added Fail");
            }
        });

    }
    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.WishHomeID) {
            text = "Please Long Press to go home Page";
        }else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.WishHomeID) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        }else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

    }
}