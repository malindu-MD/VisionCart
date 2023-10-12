package org.tensorflow.blindhelp.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
import org.tensorflow.blindhelp.examples.classification.models.Blind;
import org.tensorflow.blindhelp.examples.classification.models.Volunteer;
import org.tensorflow.lite.examples.classification.R;

public class blindLogin extends AppCompatActivity {


    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private DatabaseReference usersRef;

    private ProgressBar pb;

    private SessionManager sessionManager;

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.vlogin);
        register=findViewById(R.id.register);
        pb=findViewById(R.id.progressBar7);
        pb.setVisibility(View.INVISIBLE);

        usersRef = FirebaseDatabase.getInstance().getReference("blind");
        sessionManager = new SessionManager(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(blindLogin.this,blindRegister.class);
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
                        Blind blind = userSnapshot.getValue(Blind.class);
                        if (blind != null && blind.getPassword().equals(password)) {
                            Toast.makeText(blindLogin.this, "Login successful.", Toast.LENGTH_SHORT).show();
                            sessionManager.createSession(blind.getEmail(), blind.getUsername());

                            Intent intent=new Intent(blindLogin.this, HomePage.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(blindLogin.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(blindLogin.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
            }
        });
    }
}