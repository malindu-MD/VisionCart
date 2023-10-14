package org.tensorflow.blindhelp.examples.classification.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.tensorflow.blindhelp.examples.classification.models.Products;
import org.tensorflow.lite.examples.classification.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductAdapter extends FirebaseRecyclerAdapter<Products,ProductAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,final int position, @NonNull Products model) {

        holder.pID.setText(model.getpID());
        holder.pName.setText(model.getpName());
        holder.pMfgDate.setText(model.getpMfgDate());
        holder.pExpDate.setText(model.getpExpDate());
        holder.pPrice.setText(model.getpPrice());
        holder.pDetails.setText(model.getpDetails());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog((holder.edit.getContext()))
                        .setContentHolder(new ViewHolder(R.layout.update_popup_products))
                        .setExpanded(true,1200)
                        .create();

                // dialogPlus.show();
                View myview=dialogPlus.getHolderView();
                final EditText name=myview.findViewById(R.id.txtName);
                final EditText mfgDate=myview.findViewById(R.id. txtMFG);
                final EditText expDate=myview.findViewById(R.id.txtEXP);
                final EditText price=myview.findViewById(R.id.txtPrice);
                final EditText details=myview.findViewById(R.id.txtDetails);

                Button btnUpdate =myview.findViewById(R.id.btnUpdate);


                name.setText(model.getpName());
                price.setText(model.getpPrice());
                mfgDate.setText(model.getpMfgDate());
                expDate.setText(model.getpExpDate());
                details.setText(model.getpDetails());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            String newName = name.getText().toString().trim();
                            String newMfgDate = mfgDate.getText().toString().trim();
                            String newExpDate = expDate.getText().toString().trim();
                            String newPrice = price.getText().toString().trim();
                            String newDetails = details.getText().toString().trim();

                            if (newName.isEmpty() || newMfgDate.isEmpty() || newExpDate.isEmpty() || newPrice.isEmpty() || newDetails.isEmpty()) {
                                StringBuilder emptyFields = new StringBuilder("Please fill in the following fields:");

                                if (newName.isEmpty()) {
                                    emptyFields.append("\n-Product Name");
                                }
                                if (newMfgDate.isEmpty()) {
                                    emptyFields.append("\n- Manufacturing Date");
                                }
                                if (newExpDate.isEmpty()) {
                                    emptyFields.append("\n- Expiry Date");
                                }
                                if (newPrice.isEmpty()) {
                                    emptyFields.append("\n- Price");
                                }
                                if (newDetails.isEmpty()) {
                                    emptyFields.append("\n- Details");
                                }
                                Toast.makeText(holder.pName.getContext(), emptyFields.toString(), Toast.LENGTH_LONG).show();

                            }  else {

                                Map<String, Object> map = new HashMap<>();

                                map.put("pName", name.getText().toString());
                                map.put("pMfgDate", mfgDate.getText().toString());
                                map.put("pExpDate", expDate.getText().toString());
                                map.put("pPrice", price.getText().toString());
                                map.put("pDetails", details.getText().toString());

                                FirebaseDatabase.getInstance().getReference().child("products")
                                        .child(getRef(position).getKey()).updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(holder.pName.getContext(), "Updated successfully..", Toast.LENGTH_SHORT).show();
                                                dialogPlus.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(holder.pName.getContext(), "Error while Updating..", Toast.LENGTH_SHORT).show();
                                                dialogPlus.dismiss();
                                            }
                                        });
                            }



                    }
                });

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.pName.getContext());
                builder.setTitle("Are you sure ?");
                builder.setMessage("Deleted data can't be undo..!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        FirebaseDatabase.getInstance().getReference().child("products")
                                .child(Objects.requireNonNull(getRef(position).getKey())).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(holder.pName.getContext(),"Cancelled..",Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();
            }
        });


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_single_view,parent,false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder{
        TextView pID,pName,pMfgDate,pExpDate,pPrice,pDetails;

        ImageView edit, delete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            pID=(TextView)itemView.findViewById(R.id.idTxt);
            pName=(TextView)itemView.findViewById(R.id.nameTxt);
            pMfgDate=(TextView)itemView.findViewById(R.id.mfgTxt);
            pExpDate=(TextView)itemView.findViewById(R.id.expTxt);
            pPrice=(TextView)itemView.findViewById(R.id.priceTxt);
            pDetails=(TextView)itemView.findViewById(R.id.detailsTxt);

            edit=(ImageView)itemView.findViewById(R.id.editIcon);
            delete=(ImageView)itemView.findViewById(R.id.deleteIcon);
        }
    }
}


