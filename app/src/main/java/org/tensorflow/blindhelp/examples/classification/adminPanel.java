package org.tensorflow.blindhelp.examples.classification;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.tensorflow.blindhelp.examples.classification.models.Blind;
import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;

import org.tensorflow.blindhelp.examples.classification.activities.addProducts;
import org.tensorflow.blindhelp.examples.classification.activities.admin_product_view;

import org.tensorflow.lite.examples.classification.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class adminPanel extends AppCompatActivity {

    private ArrayList<Blind> blindList;
    private DatabaseReference database;
    private ImageView userreport;

    private  CardView addProductBtn,viewProductBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        userreport=findViewById(R.id.userreport);
        blindList = new ArrayList<>();
        CardView viewOffers = (CardView) findViewById(R.id.viewOffers);
        CardView addoffer = (CardView) findViewById(R.id.addOffer);
         addProductBtn = (CardView) findViewById(R.id.addProductBtn);
         viewProductBtn = (CardView) findViewById(R.id.viewProductBtn);

        addoffer.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, addofferpage.class);
            startActivity(intent);
        });

        viewOffers.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, viewOfferAdmin.class);
            startActivity(intent);
        });
        addProductBtn.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, addProducts.class);
            startActivity(intent);
        });

        viewProductBtn.setOnClickListener(view -> {
            // Open the OffersActivity when the CardView is clicked
            Intent intent = new Intent(adminPanel.this, admin_product_view.class);
            startActivity(intent);
        });



        userreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (blindList != null && !blindList.isEmpty()) {
                    generatePdf();
                } else {
                    Toast.makeText(adminPanel.this, "No volunteer requests found", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //pdf
        database = FirebaseDatabase.getInstance().getReference("blind");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    blindList.clear(); // Clear the list to avoid duplicates

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        VolunteerRequest product = productSnapshot.getValue(VolunteerRequest.class);
                        if (product != null) {
                            blindList.add(product);
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

    private void generatePdf() {
        // Create a new Document
        Document document = new Document();

        try {
            // Set the file path where the PDF will be saved
            File pdfFile = new File(getExternalFilesDir(null), "volunteer_requests.pdf");
            String filePath = pdfFile.getAbsolutePath();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document for writing
            document.open();

            // Add a title for the PDF
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("User List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // Add some spacing

            // Extract data from your blindList and add it to the PDF
            for (Blind request : blindList) {
                document.add(new Paragraph("Name: " + request.getUsername()));
                document.add(new Paragraph("Email: " + request.getEmail()));

                document.add(new Paragraph("\n")); // Add some spacing between items
            }

            // Close the document
            document.close();

            // Display a success message
            Toast.makeText(this, "PDF saved successfully", Toast.LENGTH_SHORT).show();

            // Open the generated PDF using an Intent with FileProvider
            openPdfWithFileProvider(pdfFile);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    private void openPdfWithFileProvider(File pdfFile) {
        Uri contentUri = FileProvider.getUriForFile(this, "org.tensorflow.blindhelp.examples.classification.fileprovider", pdfFile);

        // Create an Intent to open the PDF file with a PDF viewer app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(contentUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permissions

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where no PDF viewer app is installed
            Toast.makeText(this, "No PDF viewer app installed", Toast.LENGTH_SHORT).show();
        }



    }
}