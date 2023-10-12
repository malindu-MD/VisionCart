package org.tensorflow.blindhelp.examples.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.tensorflow.lite.examples.classification.R;

public class start extends AppCompatActivity {

    private Button blind,volunteer,admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        blind=findViewById(R.id.blind);
        volunteer=findViewById(R.id.volunteer);
        admin=findViewById(R.id.admin);

        blind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(start.this,blindLogin.class);
                startActivity(intent);




            }
        });

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(start.this,volunteerLogin.class);
                startActivity(intent);



            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(start.this, adminPanel.class);
                startActivity(intent);



            }
        });



    }
}