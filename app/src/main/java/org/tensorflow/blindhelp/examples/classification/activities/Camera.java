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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.tensorflow.blindhelp.examples.classification.models.CartList;
import org.tensorflow.lite.examples.classification.R;

import java.util.Locale;

public class Camera extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextToSpeech.OnInitListener {

    private static final long SCAN_TIMEOUT = 7000; // 4 seconds

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
                cancelScanTimeout(); // Stop the "No scanning detected" message
                speak("Product is scanned\n" + scannedData);
                resultID.setText(scannedData);
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
            recreate();
          //  speak("Scanning ");
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
        Toast.makeText(Camera.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
        speak("Product Added to Shopping Cart List");
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
