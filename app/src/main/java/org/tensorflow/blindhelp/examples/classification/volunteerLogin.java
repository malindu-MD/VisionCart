package org.tensorflow.blindhelp.examples.classification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.blindhelp.examples.classification.models.Volunteer;
import org.tensorflow.lite.examples.classification.R;

public class volunteerLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private DatabaseReference usersRef;

    private ProgressBar pb;

    private SessionManager sessionManager;

    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login2);



        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.vlogin);
        register=findViewById(R.id.register);
        pb=findViewById(R.id.progressBar7);
        pb.setVisibility(View.INVISIBLE);


        usersRef = FirebaseDatabase.getInstance().getReference("volunteer");
        sessionManager = new SessionManager(this);

//        if(sessionManager.isLoggedIn()){
//
//            Intent intent=new Intent(volunteerLogin.this, VDashboard.class);
//            startActivity(intent);
//
//        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(volunteerLogin.this,volunteerregister.class);
                startActivity(intent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if the username and password match
                loginUser(email, password);
            }
        });

    }


    private void loginUser(final String email, final String password) {
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Volunteer volunteer = userSnapshot.getValue(Volunteer.class);
                        if (volunteer != null && volunteer.getPassword().equals(password)) {
                            Toast.makeText(volunteerLogin.this, "Login successful.", Toast.LENGTH_SHORT).show();
                            sessionManager.createSession(volunteer.getEmail(), volunteer.getUsername());

                            Intent intent=new Intent(volunteerLogin.this, VDashboard.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(volunteerLogin.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(volunteerLogin.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
            }
        });
    }
}