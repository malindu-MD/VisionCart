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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.blindhelp.examples.classification.models.Offers;
import org.tensorflow.lite.examples.classification.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        submit = findViewById(R.id.submit);
        databaseRef = FirebaseDatabase.getInstance().getReference("VisionCart");
        AdateStart = findViewById(R.id.AdateStart);
        AdateEnd = findViewById(R.id.AdateEnd);

        // Retrieve values from the intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String offerDetailsText = intent.getStringExtra("offerDetails");
        String startingDate = intent.getStringExtra("dateStart");
        String endingDate = intent.getStringExtra("dateEnd");

        // Set the values in the EditText fields
        pname.setText(productName);
        offerDetails.setText(offerDetailsText);
        AdateStart.setText(startingDate);
        AdateEnd.setText(endingDate);





        submit.setOnClickListener(new View.OnClickListener() {
          @Override
         public void onClick(View view) {
              String prname = pname.getText().toString();
              String OfferDetails = offerDetails.getText().toString();
              String ending = AdateEnd.getText().toString();
              String starting = AdateStart.getText().toString();
              boolean check = validation(prname, OfferDetails, starting, ending);
                if(check==true){

                    insertData();

                    pname.setText("");
                    offerDetails.setText("");
                    AdateStart.setText("");
                    AdateEnd.setText("");

                }else {
                    Toast.makeText(getApplicationContext(),"Please check fileds",Toast.LENGTH_SHORT).show();
                }

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
        AdateStart = findViewById(R.id.AdateStart);
        AdateEnd = findViewById(R.id.AdateEnd);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

    dateStart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog dialog = new DatePickerDialog(addofferpage.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = year+"/"+month+"/"+dayOfMonth;
                    AdateStart.setText(date);

                }
            },year,month,day);

            dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            dialog.show();
        }
    });


        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(addofferpage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year+"/"+month+"/"+dayOfMonth;
                        AdateEnd.setText(date);

                    }
                },year,month,day);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog.show();
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
        String startDate = AdateStart.getText().toString();
        String endDate =  AdateEnd.getText().toString();
        String id = databaseRef.push().getKey();

        Offers offerDet = new Offers(prname,id,startDate,endDate,getofferDetails);

        if (id != null) {
            databaseRef.child(id).setValue(offerDet);
            showSuccessDialog();
        }
    }

    //validation


    private boolean validation(String pName, String getofferDetails, String startDate, String endDate) {
        if (pName.length() == 0) {
            pname.requestFocus();
            pname.setError("Field Cannot be Empty");
            return false;
        } else if (getofferDetails.length() == 0) {
            offerDetails.requestFocus();
            offerDetails.setError("Field Cannot be Empty");
            return false;
        } else if (startDate.length() == 0) {
            // Check if the start date is empty
            return false;
        } else if (endDate.length() == 0) {
            // Check if the end date is empty
            return false;
        } else {
            try {
                // Parse the start and end dates as Date objects
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                Date startDateObj = dateFormat.parse(startDate);
                Date endDateObj = dateFormat.parse(endDate);

                // Check if the start date is before the end date
                if (startDateObj.before(endDateObj)) {
                    return true; // Start date is earlier than end date, validation passes
                } else {
                    // Start date is not earlier than end date
                    Toast.makeText(getApplicationContext(), "Start date must be earlier than end date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Handle any parsing exceptions (invalid date formats)
            }
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
