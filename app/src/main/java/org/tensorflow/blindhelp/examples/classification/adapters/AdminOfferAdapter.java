// AdminOfferAdapter.java
package org.tensorflow.blindhelp.examples.classification.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.tensorflow.blindhelp.examples.classification.models.Offers;
import org.tensorflow.lite.examples.classification.R;
import java.util.ArrayList;

public class AdminOfferAdapter extends RecyclerView.Adapter<AdminOfferAdapter.MyViewHolder> {

    private OnItemClickListener mListener;
    ArrayList<Offers> list;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onUpdateButtonClick(int position);

        void onDeleteButtonClick(int position); // Added delete method
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public AdminOfferAdapter(ArrayList<Offers> list, OnItemClickListener clickListener) {
        this.list = list;
        this.mListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewoffersadmincard, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Offers offers = list.get(position);
        holder.pname.setText(offers.getPname());
        holder.startDate.setText(offers.getDateStart());
        holder.endDate.setText(offers.getDateEnd());
        holder.offerDet.setText(offers.getOfferDetails());

        // Set a click listener for the update ImageButton
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onUpdateButtonClick(position);
                    }
                }
            }
        });

        // Set a click listener for the delete ImageButton
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onDeleteButtonClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pname, offerDet, startDate, endDate;
        ImageButton updateButton, deleteButton;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener clickListener) {
            super(itemView);

            pname = itemView.findViewById(R.id.adminPrName);
            offerDet = itemView.findViewById(R.id.adminOfferdet);
            startDate = itemView.findViewById(R.id.adminFromdate);
            endDate = itemView.findViewById(R.id.adminToDate);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
