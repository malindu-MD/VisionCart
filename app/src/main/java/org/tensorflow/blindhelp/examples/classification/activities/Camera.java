package org.tensorflow.blindhelp.examples.classification.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.tensorflow.blindhelp.examples.classification.models.CartList;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

public class Camera extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener,TextToSpeech.OnInitListener {

    // Declare a variable to store the scanned data
    private String scannedData;
    private TextToSpeech tts;
    private TextView resultID;
    private CardView homeID;
    private CardView scanID;
    private CardView cartID;

    DatabaseReference cartListDbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_camera);

        resultID = findViewById(R.id.resultID);
        homeID = findViewById(R.id.homeid);
        scanID = findViewById(R.id.scanID);
        cartID = findViewById(R.id.cartID);

        homeID.setOnClickListener(this);
        scanID.setOnClickListener(this);
        cartID.setOnClickListener(this);


        scanID.setOnLongClickListener(this);
        homeID.setOnLongClickListener(this);
        cartID.setOnLongClickListener(this);

        tts = new TextToSpeech(this, this);
        scanCode();

        cartListDbRef = FirebaseDatabase.getInstance().getReference().child("CartList");
    }


    private void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(CaptureActions.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {

                // Store the scanned data in the variable
                scannedData = result.getContents();
                speak(scannedData);
                // Set the scannedData as the text of the resultID TextView
                resultID.setText(scannedData);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(scannedData);

                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scanCode();
                        speak("Scanning");
                    }
                }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                // Set the dialog's gravity to appear at the top
                AlertDialog dialog = builder.create();
                dialog.getWindow().setGravity(Gravity.TOP);
//                dialog.show();


//                AlertDialog dialog = builder.create();
//                dialog.show();

            } else {
                Toast.makeText(this, "No Result", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
            tts.speak("Please Scan product", TextToSpeech.QUEUE_FLUSH, null, null);

        }
    }


    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.homeid) {
            text = "Please Long Press to Go Home page";
        } else if (view.getId() == R.id.scanID) {
            text = "Please Long Press to Scan Again ";

        } else if (view.getId() == R.id.cartID) {
            text = "Please Long Press to add shopping list";
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.scanID) {
            // Start scanning again
            scanCode();
            speak("Scanning");
            return true;
        } else if (view.getId() == R.id.homeid) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);

            return true;
        } else if (view.getId() == R.id.cartID) {
            insertCartListData();
            return true;
        } else {
            throw new IllegalArgumentException("Undefined Clicked");
        }
    }

    private void insertCartListData() {
        String cartList = resultID.getText().toString();

        CartList cartlist = new CartList(cartList);

        cartListDbRef.push().setValue(cartlist);
        Toast.makeText(Camera.this,"Data Inserted Sucssesfully",Toast.LENGTH_SHORT).show();
        speak("Product Added to Shopping Cart List");
    }
}
