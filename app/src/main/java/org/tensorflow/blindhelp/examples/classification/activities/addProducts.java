package org.tensorflow.blindhelp.examples.classification.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.tensorflow.blindhelp.examples.classification.adminPanel;
import org.tensorflow.lite.examples.classification.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addProducts extends AppCompatActivity {

    EditText pID,pName,pMfgDate,pExpDate,pPrice,pDetails;
    ImageView mfg_ID, Exp_ID;
    Button submit,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product_item);

        pID=(EditText) findViewById(R.id.addIDTxt);
        pName = (EditText) findViewById(R.id.addProductTxt);
        pMfgDate = (EditText) findViewById(R.id.addManuDateTxt);
        pExpDate = (EditText) findViewById(R.id.addExpTxt);
        pPrice = (EditText) findViewById(R.id.addPriceTxt);
        pDetails = (EditText) findViewById(R.id.addDetailsTxt);
        mfg_ID= (ImageView) findViewById(R.id.mfg_ID);
        Exp_ID = (ImageView) findViewById(R.id.Exp_ID);
        back = (Button) findViewById(R.id.addBackBtn);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        Exp_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(addProducts.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String date =day+"/"+month+"/"+year;
                        pExpDate.setText(date);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog.show();
            }
        });

        mfg_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog expdialog = new DatePickerDialog(addProducts.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String date =day+"/"+month+"/"+year;
                        pMfgDate.setText(date);
                    }
                },year,month,day);
                expdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                expdialog.show();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), adminPanel.class));
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
        String id = pID.getText().toString().trim();
        String name = pName.getText().toString().trim();
        String mfgDate = pMfgDate.getText().toString().trim();
        String expDate = pExpDate.getText().toString().trim();
        String price = pPrice.getText().toString().trim();
        String details = pDetails.getText().toString().trim();

        if (id.isEmpty() || name.isEmpty() || mfgDate.isEmpty() || expDate.isEmpty() || price.isEmpty() || details.isEmpty()) {
            StringBuilder emptyFields = new StringBuilder("Please fill in the following fields:");
            if (id.isEmpty()) {
                emptyFields.append("\n- ID");
            }
            if (name.isEmpty()) {
                emptyFields.append("\n- Name");
            }
            if (mfgDate.isEmpty()) {
                emptyFields.append("\n- Manufacturing Date");
            }
            if (expDate.isEmpty()) {
                emptyFields.append("\n- Expiry Date");
            }
            if (price.isEmpty()) {
                emptyFields.append("\n- Price");
            }
            if (details.isEmpty()) {
                emptyFields.append("\n- Details");
            }
            Toast.makeText(getApplicationContext(), emptyFields.toString(), Toast.LENGTH_LONG).show();

        } else if (!id.matches("^[A-Z]{2}\\d+$")) {
            Toast.makeText(getApplicationContext(), "Product ID must start with two capital English letters followed by numbers.", Toast.LENGTH_LONG).show();
        }  else {



            Map<String, Object> map = new HashMap<>();

            map.put("pID", pID.getText().toString());
            map.put("pName", pName.getText().toString());
            map.put("pMfgDate", pMfgDate.getText().toString());
            map.put("pExpDate", pExpDate.getText().toString());
            map.put("pPrice", pPrice.getText().toString());
            map.put("pDetails", pDetails.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("products").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pID.setText("");
                            pName.setText("");
                            pMfgDate.setText("");
                            pExpDate.setText("");
                            pPrice.setText("");
                            pDetails.setText("");
                            Toast.makeText(getApplicationContext(), "Product Inserted Successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }



}
