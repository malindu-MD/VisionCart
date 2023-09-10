package com.example.visioncart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.visioncart.activities.helpDesk;

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