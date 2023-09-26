package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.tensorflow.lite.examples.classification.R;

public class VDashboard extends AppCompatActivity  implements View.OnClickListener{


    CardView viewRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vdashboard);

        viewRequest=findViewById(R.id.valRequest);
        viewRequest.setOnClickListener((View.OnClickListener) this);


    }


    @Override
    public void onClick(View view) {
        Intent  intent = new Intent(this, request.class);
        startActivity(intent);
    }
}