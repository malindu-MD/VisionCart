package org.tensorflow.blindhelp.examples.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.tensorflow.lite.examples.classification.R;

public class test extends AppCompatActivity {


    private TextView text;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sessionManager = new SessionManager(this);
        text=findViewById(R.id.test);


        if (sessionManager.isLoggedIn()) {
            // If not logged in, redirect to the login activity and finish this activity
            text.setText(sessionManager.getUsername());
        }




    }
}