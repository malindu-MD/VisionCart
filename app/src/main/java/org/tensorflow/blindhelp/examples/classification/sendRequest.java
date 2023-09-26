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

import org.tensorflow.blindhelp.examples.classification.activities.helpDesk;
import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class sendRequest extends AppCompatActivity {
    private TextToSpeech tts;

    private TextView phoneNumber,Date,Time,Message;
    private int numberOfClicks;
    private boolean IsInitialVoiceFinshed;

    private DatabaseReference requestDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        IsInitialVoiceFinshed=false;

        requestDbRef = FirebaseDatabase.getInstance().getReference("VolunteerRequest");

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if((status==TextToSpeech.SUCCESS)){

                    int result=tts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","This Language is not supported");
                    }
                    speak("Welcome to send request form. Tell me your phone number");
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


        phoneNumber= (TextView) findViewById(R.id.tos);
        Date  =(TextView)findViewById(R.id.date);
        Message = (TextView) findViewById(R.id.message);
        Time=(TextView) findViewById(R.id.time);
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

            Toast.makeText(sendRequest.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();


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
                            phoneNumber.setText(to);
                            speak("tell your date ");

                            break;
                        case 2:

                            Date.setText(result.get(0));
                            speak("tell your time");
                            break;
                        case 3:
                            Time.setText(result.get(0));
                            speak("tell your message");
                            break;
                        case 4 :
                            Message.setText(result.get(0));
                            speak("Please Confirm the Request\n phone number : " + phoneNumber.getText().toString() + "\nDate is : " + Date.getText().toString() + "\nTime  is : " + Date.getText().toString() + "\nMessage is : " + Message.getText().toString()  + "\nSpeak Yes to confirm");
                            break;

                        default:
                            if(result.get(0).equals("yes"))
                            {
                                insertRequest(phoneNumber.getText().toString(),Date.getText().toString(),Time.getText().toString(),Message.getText().toString());

                            }else
                            {
                                speak("Please Restart the app to reset");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        exitFromApp();
                                    }
                                }, 4000);
                            }
                    }

                }
            }
            else {
                switch (numberOfClicks) {
                    case 1:
                        speak("tell  your phone number");
                        break;
                    case 2:
                        speak("tell your date  ");
                        break;
                    case 3:
                        speak("tell your time ");
                        break;
                    case 4:
                        speak("tell your message ");
                        break;
                    default:
                        speak("say yes or no");
                        break;
                }
                numberOfClicks--;
            }
        }
        IsInitialVoiceFinshed=true;
    }

    private void insertRequest(String phoneNumber,String date,String time,String message){

        Toast.makeText(sendRequest.this, "hello world", Toast.LENGTH_SHORT).show();


        String id = requestDbRef.push().getKey();

        String name="Manoj";

        String status="Not accept";

        VolunteerRequest request= new VolunteerRequest(id,name,phoneNumber,date,time,message,null,status);

        assert id != null;

        requestDbRef.child(id).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Data insertion was successful
                speak("Sending the Request");
                Intent intent;

                intent = new Intent(this, helpDesk.class);
                startActivity(intent);

            } else {
                // Data insertion failed
                speak("Request Sending is Failed");
            }
        });














    }





}