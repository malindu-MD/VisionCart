package org.tensorflow.blindhelp.examples.classification;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.tensorflow.blindhelp.examples.classification.adapters.RequestList;
import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;
import org.tensorflow.lite.examples.classification.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VDashboard extends AppCompatActivity  {


    private CardView viewRequest,acceptedRequest,completed;
    private TextView  name,totalPoint;

    private SessionManager sessionManager;

    private ImageView logout,downloadPdf;

    private ArrayList<VolunteerRequest> volunteerRequests;

    private DatabaseReference database;

    private  int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vdashboard);
        sessionManager = new SessionManager(this);


        viewRequest=findViewById(R.id.valRequest);
        acceptedRequest=findViewById(R.id.acceptedRequest);
        completed=findViewById(R.id.completed);
        logout=findViewById(R.id.logout);
        name=findViewById(R.id.name1);
        downloadPdf=findViewById(R.id.downloadpdf);
        totalPoint=findViewById(R.id.totalPoint);

        volunteerRequests = new ArrayList<>();

       name.setText("Hi "+sessionManager.getUsername());

        viewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(VDashboard.this, request.class);
                startActivity(intent);


            }
        });

        acceptedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(VDashboard.this, acceptedRequest.class);
                startActivity(intent);


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              showAcceptDialog();


            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(VDashboard.this, completedRequest.class);
                startActivity(intent);


            }
        });

        downloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPDF("Volo");


            }
        });




        database = FirebaseDatabase.getInstance().getReference("VolunteerRequest");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    volunteerRequests.clear(); // Clear the list to avoid duplicates

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        VolunteerRequest product = productSnapshot.getValue(VolunteerRequest.class);
                        if (product != null) {
                            if((product.getStatus().equals("Completed")) && product.getVolunteer().equals(sessionManager.getUserId())){
                                volunteerRequests.add(product);
                                total += product.getPoint();


                            }
                        }
                        String ptotal = String.valueOf(total);
                        totalPoint.setText(ptotal);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });







    }

    @Override
    public void onBackPressed() {
        // To disable the back button, simply do nothing
        // You can also show a message or perform a different action if needed
        // super.onBackPressed(); // Remove this line
    }


    private void showAcceptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog2, null);

        TextView requestTitle = dialogView.findViewById(R.id.title);
        Button acceptButton = dialogView.findViewById(R.id.yes);
        Button cancelButton = dialogView.findViewById(R.id.no);

        requestTitle.setText("Are you sure you want to LogOut?");

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sessionManager.logout();
                Intent intent=new Intent(VDashboard.this, start.class);
                startActivity(intent);
                dialog.dismiss();




            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void createPDF (String pdfFilename){

        try {

            Document document = new Document();

            File pdfFile = new File(getExternalFilesDir(null), "recyclerview_data.pdf");
            String filePath = pdfFile.getAbsolutePath();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));


            //Inserting Image in PDFc
            Date currentDate = new Date();

            // Define the desired date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Format the current date as a string
            String formattedDate = dateFormat.format(currentDate);




            PdfPTable irdTable = new PdfPTable(2);
            irdTable.addCell(getIRDCell("Report No"));
            irdTable.addCell(getIRDCell("Date"));
            irdTable.addCell(getIRDCell("XE1234")); // pass invoice number
            irdTable.addCell(getIRDCell(formattedDate)); // pass invoice date

            PdfPTable irhTable = new PdfPTable(3);
            irhTable.setWidthPercentage(100);

            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("Report Of Completed Request", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            PdfPCell invoiceTable = new PdfPCell (irdTable);
            invoiceTable.setBorder(0);
            irhTable.addCell(invoiceTable);

            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            fs.addFont(font);
            Phrase bill = fs.process("To"); // customer information
            Paragraph name = new Paragraph("Mr."+sessionManager.getUsername());
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph(sessionManager.getUserId());
            contact.setIndentationLeft(20);
            Paragraph address = new Paragraph("");
            address.setIndentationLeft(20);

            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 1, 2,3,2,4,1 });
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("Request Number"));
            billTable.addCell(getBillHeaderCell("Date"));
            billTable.addCell(getBillHeaderCell("Name"));
            billTable.addCell(getBillHeaderCell("Phone Number"));
            billTable.addCell(getBillHeaderCell("Email"));
            billTable.addCell(getBillHeaderCell("Points"));


            for(int i=0;i<volunteerRequests.size();i++){

                billTable.addCell(getBillRowCell(volunteerRequests.get(i).getRequestId()));
                billTable.addCell(getBillRowCell(volunteerRequests.get(i).getDate()));
                billTable.addCell(getBillRowCell(volunteerRequests.get(i).getName()));
                billTable.addCell(getBillRowCell(volunteerRequests.get(i).getPhoneNumber()));
                billTable.addCell(getBillRowCell(volunteerRequests.get(i).getEmail()));
                billTable.addCell(getBillRowCell(String.valueOf(volunteerRequests.get(i).getPoint())));



            }






            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell("Conditions"));
            validity.addCell(getValidityCell(" This points are valid for 6 months"));
            validity.addCell(getValidityCell(" You can get discount using your email"));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("Total Points"));

            accounts.addCell(getAccountsCellR(String.valueOf(total)));
            accounts.addCell(getAccountsCell(""));
            accounts.addCell(getAccountsCellR(""));
            accounts.addCell(getAccountsCell(""));
            accounts.addCell(getAccountsCellR(""));
            accounts.addCell(getAccountsCell(""));
            accounts.addCell(getAccountsCellR(""));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);
            billTable.addCell(summaryR);

            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell(" "));

            document.open();//PDF document opened........

            document.add(irhTable);
            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(address);
            document.add(billTable);
            document.add(describer);

            document.close();




            System.out.println("Pdf created successfully..");

            openPdfWithFileProvider(pdfFile);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static void setHeader() {

    }


    public static PdfPCell getIRHCell(String text, int alignment) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        /*	font.setColor(BaseColor.GRAY);*/
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getIRDCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getBillFooterCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
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