package org.tensorflow.blindhelp.examples.classification.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.tensorflow.blindhelp.examples.classification.models.CartList;
import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class Camera extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextToSpeech.OnInitListener {

    private static final long SCAN_TIMEOUT = 7000; // 4 seconds

    private TextView nameP;
    private TextView detailsP;
    private TextView priceP;
    private TextView expP;
    private TextView mfgP;
    private String scannedData;
    private TextToSpeech tts;
    private TextView resultID;
    private CardView homeID;
    private CardView scanID;
    private CardView cartID;

    DatabaseReference cartListDbRef;
    private Handler scanTimeoutHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_camera);

        //resultID = findViewById(R.id.resultID);
        homeID = findViewById(R.id.homeid);
        scanID = findViewById(R.id.scanID);

        homeID.setOnClickListener(this);
        scanID.setOnClickListener(this);

        scanID.setOnLongClickListener(this);
        homeID.setOnLongClickListener(this);

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.UK);

        scanCode();
        cartListDbRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        scanTimeoutHandler = new Handler(Looper.getMainLooper());
        startScanTimeout(); // Start the initial timeout

        // Schedule a recurring timeout task
        final Runnable scanTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                if (scannedData == null) {
                    speak("No scanning detected. Turning room to the other side.");
                    // Implement code to turn the room to the other side here
                    startScanTimeout(); // Restart the timeout
                }
            }
        };

        scanTimeoutHandler.postDelayed(scanTimeoutRunnable, SCAN_TIMEOUT); // Schedule the first timeout
    }

    private void startScanTimeout() {
        // This method is used to restart the timeout handler
        scanTimeoutHandler.removeCallbacksAndMessages(null);
        scanTimeoutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scannedData == null) {
                    speak("No scanning detected. Point the camera in another direction");
                    // Implement code to turn the room to the other side here
                    startScanTimeout(); // Restart the timeout
                }
            }
        }, SCAN_TIMEOUT);
    }

    private void cancelScanTimeout() {
        scanTimeoutHandler.removeCallbacksAndMessages(null);
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(true);
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
                scannedData = result.getContents();




                nameP = findViewById(R.id.nameP); // Assuming 'aaa' is the ID of your TextView in the XML layout
                detailsP =findViewById(R.id.detailsP);
                priceP = findViewById(R.id.priceP);
                expP =findViewById(R.id.expP);
                mfgP=findViewById(R.id.mfgP);

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                            String pID = String.valueOf(dataSnapshot1.child("pID").getValue());
                            if (pID.equals(scannedData)) {

                                String pName = String.valueOf(dataSnapshot1.child("pName").getValue());
                                nameP.setText(pName);

                                String price = String.valueOf(dataSnapshot1.child("pPrice").getValue());
                                priceP.setText(price);
                                String mfg = String.valueOf(dataSnapshot1.child("pMfgDate").getValue());
                                mfgP.setText(mfg);
                                String exp = String.valueOf(dataSnapshot1.child("pExpDate").getValue());
                                expP.setText(exp);
                                String details1 = String.valueOf(dataSnapshot1.child("pDetails").getValue());
                                detailsP.setText(details1);

                                // Toast.makeText(Camera.this, "CartList value equals the given value.", Toast.LENGTH_SHORT).show();
                                speak("Product is scanned\n" + "Product Name\n"+pName+"\nPrice\n"+price+"Rupees"+"\nManufacture Date\n"+mfg+"\nExpire Date\n"+exp+"\nProduct Details\n"+details1);

                                break;
                            }
                            else{
                                Toast.makeText(Camera.this, "not found.", Toast.LENGTH_SHORT).show();
                                speak("No Product Data here");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Camera.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                });

                cancelScanTimeout(); // Stop the "No scanning detected" message
//                speak("Product is scanned\n" + "Product Name\n"+nameP+"Product Details\n"+detailsP);
                //  resultID.setText(scannedData);
                cancelScanTimeout();
            } else {
                cancelScanTimeout();
                Toast.makeText(this, "No Result", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
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
            tts.speak("Please Scan product\n", TextToSpeech.QUEUE_FLUSH, null, null);
            speak("Please Scan product\n" + "Move your camera");
        }
    }

    @Override
    public void onClick(View view) {
        String text = "";
        if (view.getId() == R.id.homeid) {
            text = "Please Long Press to Go Home page";
        } else if (view.getId() == R.id.scanID) {
            text = "Please Long Press to Scan Again ";
        }  else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        speak(text);
    }

    @Override
    public boolean onLongClick(View view) {
        Intent intent;
        if (view.getId() == R.id.scanID) {
            recreate();
            //  speak("Scanning ");
            return true;
        } else if (view.getId() == R.id.homeid) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        }else {
            throw new IllegalArgumentException("Undefined Clicked");
        }
    }

    private void insertCartListData() {
        String name = nameP.getText().toString();
        String price = priceP.getText().toString();
        String mfg = mfgP.getText().toString();
        String exp = expP.getText().toString();
        String details = detailsP.getText().toString();

        if (!name.isEmpty() && !details.isEmpty()) {
            CartList cartList = new CartList(name,price,mfg,exp,details); // Create a CartList object with name and details

            DatabaseReference newCartItemRef = cartListDbRef.push(); // Create a unique key for the new item
            newCartItemRef.setValue(cartList);

            Toast.makeText(Camera.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
            speak("Product Added to Shopping Cart List");
        } else {
            Toast.makeText(Camera.this, "Name and details must not be empty", Toast.LENGTH_SHORT).show();
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