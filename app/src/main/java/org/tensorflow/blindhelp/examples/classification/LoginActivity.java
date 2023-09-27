package org.tensorflow.blindhelp.examples.classification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
import org.tensorflow.lite.examples.classification.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1;
    Button btn2;

    Button admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        btn1=findViewById(R.id.volunteer);
        btn2=findViewById(R.id.blind);
        admin=findViewById(R.id.admin);

        btn1.setOnClickListener((View.OnClickListener) this);
        btn2.setOnClickListener((View.OnClickListener) this);
        admin.setOnClickListener(this);



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
        else if (view.getId() == R.id.admin) {
            intent = new Intent(this, adminPanel.class);
            startActivity(intent);
        }
        else {
            throw new IllegalArgumentException("Undefined Clicked");
        }

    }
}