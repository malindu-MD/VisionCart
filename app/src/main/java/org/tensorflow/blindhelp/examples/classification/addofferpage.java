package org.tensorflow.blindhelp.examples.classification;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.tensorflow.blindhelp.examples.classification.Fragments.DatePickerFragment;
import org.tensorflow.blindhelp.examples.classification.models.Offers;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class addofferpage extends AppCompatActivity    {

    private EditText pname;
    private EditText offerDetails;
    private TextView AdateStart;
    private TextView AdateEnd;
    private Button submit;
    private Button home;

    private Dialog successDialog;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addofferpage);


        pname = findViewById(R.id.pname);
        offerDetails = findViewById(R.id.offerDet);
        //AdateStart = findViewById(R.id.AdateStart);
        AdateEnd = findViewById(R.id.AdateEnd);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//        ArrayAdapter<CharSequence> daysadapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
//        daysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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


        ImageView dateStart =(ImageView) findViewById(R.id.dateStart);
        ImageView dateEnd = (ImageView) findViewById(R.id.dateEnd);
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker =  new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });




    }


//    @Override
//    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//
//        Calendar c =Calendar.getInstance();
//        c.set(Calendar.YEAR,year);
//        c.set(Calendar.MONTH,month);
//        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//        String CurrentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//
//        TextView AdateStart = (TextView)  findViewById(R.id.AdateStart);
//        AdateStart.setText(CurrentDateString);
//
//    }

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
        String startDate = AdateStart.getText().toString();
        String endDate =  AdateEnd.getText().toString();




        String id = databaseRef.push().getKey();

        Offers offerDet = new Offers(prname,getofferDetails,startDate,endDate, id);

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
