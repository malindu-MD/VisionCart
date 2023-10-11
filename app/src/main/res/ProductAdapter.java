package org.tensorflow.blindhelp.examples.classification.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;

import org.tensorflow.lite.examples.classification.R;

import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends FirebaseRecyclerAdapter<ProductItem, ProductAdapter.myviewholder> {
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<ProductItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final ProductItem model) {
        holder.pName.setText(model.getpName());
        holder.pMfgDate.setText(model.getpMfgDate());
        holder.pExpDate.setText(model.getpExpDate());
        holder.pPrice.setText(model.getpPrice());
        holder.pDetails.setText(model.getpDetails());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.edit.getContext())
                        .setContentHolder(new RecyclerView.ViewHolder(R.layout.update_product_details))
                        .setExpanded(true, 1100)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText name = myview.findViewById(R.id.updateName);
                final EditText mfgDate = myview.findViewById(R.id.updateMfgDate);
                final EditText expDate = myview.findViewById(R.id.updateExpDate);
                final EditText price = myview.findViewById(R.id.updatePrice);
                final EditText details = myview.findViewById(R.id.updateDetails);

                Button submit = myview.findViewById(R.id.updateBtn);

                name.setText(model.getpName());
                mfgDate.setText(model.getpMfgDate());
                expDate.setText(model.getpExpDate());
                price.setText(model.getpPrice());
                details.setText(model.getpDetails());

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();

                        map.put("pName", name.getText().toString());
                        map.put("pMfgDate", mfgDate.getText().toString());
                        map.put("pExpDate", expDate.getText().toString());
                        map.put("pPrice", price.getText().toString());
                        map.put("pDetails", details.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("ProductDetails")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.delete.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("ProductDetails")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing on "No" button click
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_single_view, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView edit, delete;
        TextView pName, pMfgDate, pExpDate, pPrice, pDetails;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.nameTxt);
            pMfgDate = itemView.findViewById(R.id.mfgTxt);
            pExpDate = itemView.findViewById(R.id.expTxt);
            pPrice = itemView.findViewById(R.id.priceTxt);
            pDetails = itemView.findViewById(R.id.detailsTxt);

            edit = itemView.findViewById(R.id.editIcon);
            delete = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
