package org.tensorflow.blindhelp.examples.classification;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.tensorflow.blindhelp.examples.classification.adapters.AdminOfferAdapter;
import org.tensorflow.blindhelp.examples.classification.models.Offers;
import org.tensorflow.lite.examples.classification.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;


public class viewOfferAdmin extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    AdminOfferAdapter adminOfferAdapter;
    ArrayList<Offers> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_admin);
        TextView downloadpdf= findViewById(R.id.downloadpdf);
        Button button = findViewById(R.id.offerhome);

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePdf();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the OffersActivity when the button is clicked
                Intent intent = new Intent(viewOfferAdmin.this, adminPanel.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.adminViewOffer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        getProductData();
    }

    private void getProductData() {
        database = FirebaseDatabase.getInstance().getReference("VisionCart");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list to avoid duplicate entries
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Offers offers = dataSnapshot.getValue(Offers.class);
                    list.add(offers);
                }

                adminOfferAdapter = new AdminOfferAdapter(list, new AdminOfferAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Handle item click here if needed
                    }

                    @Override
                    public void onUpdateButtonClick(int position) {
                        // Handle update button click here
                        Offers selectedOffer = list.get(position);
                        String pname = selectedOffer.getPname();
                        String offerDetails = selectedOffer.getOfferDetails();
                        String dateStart = selectedOffer.getDateStart();
                        String dateEnd = selectedOffer.getDateEnd();

                        Intent intent = new Intent(viewOfferAdmin.this, addofferpage.class);
                        intent.putExtra("productName", pname);
                        intent.putExtra("dateStart", dateStart);
                        intent.putExtra("dateEnd", dateEnd);
                        intent.putExtra("offerDetails", offerDetails);
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteButtonClick(int position) {
                        // Handle delete button click here
                        // Get the selected offer
                        Offers selectedOffer = list.get(position);
                        String offerId = selectedOffer.getId();

                        // Create a confirmation dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(viewOfferAdmin.this);
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to delete this item?");

                        // Add a positive button (Yes)
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked Yes, proceed with deletion
                                database.child(offerId).removeValue(); // Delete from the database
                                dialog.dismiss();
                            }
                        });

                        // Add a negative button (No)
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked No, do nothing
                                dialog.dismiss();
                            }
                        });

                        // Create and show the dialog
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

                recyclerView.setAdapter(adminOfferAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void generatePdf() {
        // Create a new Document
        Document document = new Document();

        try {
            // Set the file path where the PDF will be saved
            File pdfFile = new File(getExternalFilesDir(null), "recyclerview_data.pdf");
            String filePath = pdfFile.getAbsolutePath();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document for writing
            document.open();

            // Extract data from your RecyclerView and add it to the PDF
            for (int i = 0; i < list.size(); i++) {
                Offers offer = list.get(i);
                String productName = offer.getPname();
                String offerDetails = offer.getOfferDetails();
                String startDate = offer.getDateStart();
                String endDate = offer.getDateEnd();

                // Add the data to the PDF
                document.add(new Paragraph("Product Name: " + productName));
                document.add(new Paragraph("Offer Details: " + offerDetails));
                document.add(new Paragraph("Start Date: " + startDate));
                document.add(new Paragraph("End Date: " + endDate));
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
