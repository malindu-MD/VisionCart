package org.tensorflow.blindhelp.examples.classification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class volunteerregister extends AppCompatActivity {


   private EditText usernameEditText, emailEditText, passwordEditText;

  private Button registerButton;

    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteerregister);

      usernameEditText = findViewById(R.id.userName);
      emailEditText = findViewById(R.id.email);
      passwordEditText = findViewById(R.id.password);
      registerButton = findViewById(R.id.register);

        usersRef = FirebaseDatabase.getInstance().getReference("volunteer");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if the username is unique
                checkUsernameAvailability(username, email, password);
            }
        });



    }

    private void checkUsernameAvailability(final String username, final String email, final String password) {
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(volunteerregister.this, "Email is already Registered.", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is available, proceed with registration
                    registerUser(username, email, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(volunteerregister.this, "Database read error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void registerUser(String username, String email, String password) {
        String userId = usersRef.push().getKey();
        Volunteer volunteer = new Volunteer(username, email, password);
        usersRef.child(userId).setValue(volunteer);

        Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show();
    }


}