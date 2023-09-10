package com.example.visioncart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.visioncart.activities.HomePage;
import com.example.visioncart.activities.helpDesk;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        btn1=findViewById(R.id.volunteer);
        btn2=findViewById(R.id.blind);


        btn1.setOnClickListener((View.OnClickListener) this);
        btn2.setOnClickListener((View.OnClickListener) this);




    }

    @Override
    public void onClick(View view) {

        Intent intent;
        if (view.getId() == R.id.volunteer) {
            intent = new Intent(this, VDashboard.class);
            startActivity(intent);
        }
       else if (view.getId() == R.id.blind) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
        else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

    }
}