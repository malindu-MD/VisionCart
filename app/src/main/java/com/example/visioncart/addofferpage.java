package com.example.visioncart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visioncart.R;
import com.example.visioncart.models.Offers;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addofferpage extends AppCompatActivity {

    private EditText pname;
    private EditText offerDetails;
    private Spinner fromMonth;
    private Spinner fromday;
    private Spinner toMonth;
    private Spinner toDay;
    private Button submit;
    private Button home;

    private Dialog successDialog;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addofferpage);

        fromMonth = findViewById(R.id.monthfrom);
        fromday = findViewById(R.id.fromday);
        toMonth = findViewById(R.id.tomonmth);
        toDay = findViewById(R.id.today);
        pname = findViewById(R.id.pname);
        offerDetails = findViewById(R.id.offerDet);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromMonth.setAdapter(adapter);
        toMonth.setAdapter(adapter);

        ArrayAdapter<CharSequence> daysadapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        daysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromday.setAdapter(daysadapter);
        toDay.setAdapter(daysadapter);

        submit = findViewById(R.id.submit);
        databaseRef = FirebaseDatabase.getInstance().getReference("VisionCart");

        // Retrieve values from the intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String offerDetailsText = intent.getStringExtra("offerDetails");
        String fromMonthText = intent.getStringExtra("fromMonth");
        String fromDayText = intent.getStringExtra("fromDay");
        String toMonthText = intent.getStringExtra("toMonth");
        String toDayText = intent.getStringExtra("toDay");

        // Set the values in the EditText fields
        pname.setText(productName);
        offerDetails.setText(offerDetailsText);

        // Set the values in the spinners
        setSpinnerValue(fromMonth, fromMonthText);
        setSpinnerValue(toMonth, toMonthText);
        setSpinnerValue(fromday, fromDayText);
        setSpinnerValue(toDay, toDayText);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        home = findViewById(R.id.bbbb);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addofferpage.this, adminPanel.class);
                startActivity(intent);
            }
        });
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
            int position = adapter.getPosition(value);
            spinner.setSelection(position);
        }
    }

    private void insertData() {
        String prname = pname.getText().toString();
        String getofferDetails = offerDetails.getText().toString();
        String getfromMonth = fromMonth.getSelectedItem().toString();
        String getfromDay = fromday.getSelectedItem().toString();
        String getToDay = toDay.getSelectedItem().toString();
        String getToMonth = toMonth.getSelectedItem().toString();

        String id = databaseRef.push().getKey();

        Offers offerDet = new Offers(prname, getfromMonth, getfromDay, getToMonth, getToDay, getofferDetails, id);

        if (id != null) {
            databaseRef.child(id).setValue(offerDet);
            showSuccessDialog();
        }
    }
    // Create a method to show the custom success dialog
    private void showSuccessDialog() {
        successDialog = new Dialog(this);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.success_dialog);

        TextView dialogTitle = successDialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = successDialog.findViewById(R.id.dialogMessage);
        Button dialogButtonOK = successDialog.findViewById(R.id.dialogButtonOK);

        dialogTitle.setText("Success");
        dialogMessage.setText("Data inserted successfully!");

        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
            }
        });

        successDialog.show();
    }
}
