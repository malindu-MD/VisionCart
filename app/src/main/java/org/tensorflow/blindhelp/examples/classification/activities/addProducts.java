package org.tensorflow.blindhelp.examples.classification.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.tensorflow.lite.examples.classification.R;

import java.util.HashMap;
import java.util.Map;

public class addProducts extends AppCompatActivity {

    EditText pName,pMfgDate,pExpDate,pPrice,pDetails;
    Button submit,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product_item);

        pName = (EditText) findViewById(R.id.addProductTxt);
        pMfgDate = (EditText) findViewById(R.id.addManuDateTxt);
        pExpDate = (EditText) findViewById(R.id.addExpTxt);
        pPrice = (EditText) findViewById(R.id.addPriceTxt);
        pDetails = (EditText) findViewById(R.id.addDetailsTxt);

        back = (Button) findViewById(R.id.addBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });
        submit = (Button) findViewById(R.id.addStoreBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });
    }
        private void processinsert()
        {
            Map<String,Object> map=new HashMap<>();
            map.put("pName",pName.getText().toString());
            map.put("pMfgDate",pMfgDate.getText().toString());
            map.put("pExpDate",pExpDate.getText().toString());
            map.put("pPrice",pPrice.getText().toString());
            map.put("pDetails",pDetails.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("products").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pName.setText("");
                            pMfgDate.setText("");
                            pExpDate.setText("");
                            pPrice.setText("");
                            pDetails.setText("");
                            Toast.makeText(getApplicationContext(),"Product Inserted Successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"Could not insert",Toast.LENGTH_LONG).show();
                        }
                    });

        }


    }
