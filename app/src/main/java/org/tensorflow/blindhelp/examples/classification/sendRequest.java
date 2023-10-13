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
import java.util.Random;

public class sendRequest extends AppCompatActivity {
    private TextToSpeech tts;

    private TextView phoneNumber,Date,Time,Message;
    private int numberOfClicks;
    private boolean IsInitialVoiceFinshed;

    private DatabaseReference requestDbRef;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        sessionManager = new SessionManager(this);

        IsInitialVoiceFinshed=false;

        requestDbRef = FirebaseDatabase.getInstance().getReference("VolunteerRequest");

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if((status==TextToSpeech.SUCCESS)){

                    int result=tts.setLanguage(Locale.UK);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","This Language is not supported");
                    }
                    speak("Welcome to send request form. Tell your phone number. It should be 10 digits");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IsInitialVoiceFinshed=true;
                        }
                    },1000);



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


                    switch (numberOfClicks) {
                        case 1:
                            String phoneNumberText = result.get(0).replaceAll("underscore", "_");
                            phoneNumberText = phoneNumberText.replaceAll("\\s+", "");


                            if (isValidPhone(phoneNumberText)) {
                                phoneNumber.setText(phoneNumberText);
                                speak("Tell your month and date that you will go to the shop. It should tell  as an example January 7");
                            } else {
                                speak("Invalid Phone number format. Please re-speak the phone number.");
                                phoneNumber.setText("");
                                numberOfClicks--;
                            }
                            break;
                        case 2:
                            String dateText = result.get(0);
                            // Perform date validation here, e.g., check if it's a valid date format
                            if (isValidMonth(dateText)) {
                                Date.setText(dateText);
                                speak("tell your time that you will go to the shop given date");
                            } else {
                                speak("Invalid date format. Please re-speak the date.");
                                Date.setText("");
                                numberOfClicks--;
                            }
                            break;
                        case 3:
                            String timeText = result.get(0);
                            // Perform time validation here, e.g., check if it's a valid time format
                            if (isValidTime(timeText)) {
                                Time.setText(timeText);
                                speak("tell your message including all information");
                            } else {
                                speak("Invalid time format. Please re-speak the time.");
                                Time.setText("");
                                numberOfClicks--;
                            }
                            break;
                        case 4:
                            String messageText = result.get(0);
                            // Perform message validation here if needed
                            // For example, check if the message is not empty or too long
                            if (isValidMessage(messageText)) {
                                Message.setText(messageText);
                                speak("Please Confirm the Request\n phone number : " + phoneNumber.getText().toString() + "\nDate is : " + Date.getText().toString() + "\nTime  is : " + Time.getText().toString() + "\nMessage is : " + Message.getText().toString() + "\nSpeak Okay to confirm or \n Speak no to Cancel");
                            } else {
                                speak("Invalid message. Please re-speak the message.");
                                Message.setText("");
                                numberOfClicks--;
                            }
                            break;

                        default:
                            if(result.get(0).toLowerCase().equals("okay"))
                            {
                                insertRequest(phoneNumber.getText().toString(),Date.getText().toString(),Time.getText().toString(),Message.getText().toString());

                            }else if(result.get(0).toLowerCase().equals("okay okay")){

                                insertRequest(phoneNumber.getText().toString(),Date.getText().toString(),Time.getText().toString(),Message.getText().toString());

                            }
                            else if(result.get(0).toLowerCase().equals("ok")){

                                insertRequest(phoneNumber.getText().toString(),Date.getText().toString(),Time.getText().toString(),Message.getText().toString());

                            }
                            else if(result.get(0).toLowerCase().equals("ok ok")){

                                insertRequest(phoneNumber.getText().toString(),Date.getText().toString(),Time.getText().toString(),Message.getText().toString());

                            }
                            else if(result.get(0).toLowerCase().equals("no")){

                                Intent intent=new Intent(sendRequest.this, helpDesk.class);
                                startActivity(intent);

                            }
                            else if(result.get(0).toLowerCase().equals("no no")){

                                Intent intent=new Intent(sendRequest.this, helpDesk.class);
                                startActivity(intent);

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
                        speak("say Okay or no");
                        break;
                }
                numberOfClicks--;
            }
        }
        IsInitialVoiceFinshed=true;
    }

    private void insertRequest(String phoneNumber,String date,String time,String message){

        Toast.makeText(sendRequest.this, "Sent request", Toast.LENGTH_SHORT).show();


        String id = requestDbRef.push().getKey();

        Random rand = new Random();
        int min = 100; // Minimum 3-digit number
        int max = 999; // Maximum 3-digit number
        int randomNumber = rand.nextInt((max - min) + 1) + min;

// Convert the random number to a string
        String requestId = String.valueOf(randomNumber);



        String status="Not accept";

        //VolunteerRequest request= new VolunteerRequest(id,requestId,sessionManager.getUserId(),sessionManager.getUsername(),phoneNumber,date,time,message,"",status,0);
        VolunteerRequest request= new VolunteerRequest(id,requestId,sessionManager.getUserId(),sessionManager.getUsername(),phoneNumber,date,time,message,"","",status,0);

        assert id != null;

        requestDbRef.child(id).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Data insertion was successful

                speak("Sending the Request\n"+"Your Request Number is "+requestId+"\nAgain \nYour Request Number is "+requestId+"please remember it");
                Intent intent;

                intent = new Intent(this, helpDesk.class);
                startActivity(intent);

            } else {
                // Data insertion failed
                speak("Request Sending is Failed");
            }
        });














    }

    private boolean isValidMonth(String input) {
        // Split the input string into parts (e.g., "January 2" -> ["January", "2"])
        String[] parts = input.split(" ");

        if (parts.length != 2) {
            return false; // Return false if the input does not have two parts
        }

        String month = parts[0]; // Extract the month part

        // Define an array of valid month names
        String[] validMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        // Check if the provided month is in the list of valid months
        for (String validMonthName : validMonths) {
            if (validMonthName.equalsIgnoreCase(month)) {
                return true; // Return true if the month is valid
            }
        }

        return false; // Return false if the provided month is not in the list of valid months
    }

    private boolean isValidTime(String time) {


        return true;
    }


    private boolean isValidMessage(String message) {
        // Implement your message validation logic here if needed
        // For example, check if the message is not empty or too long
        return !message.isEmpty(); // Adjust the maximum length as needed
    }
    private boolean isValidPhone(String phone){

        return phone.matches("\\d{10}");


    }











}