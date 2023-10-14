package org.tensorflow.blindhelp.examples.classification.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;

import com.google.firebase.database.DatabaseReference;

import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class Completed extends RecyclerView.Adapter<Completed.ViewHolder> {

    private ArrayList<VolunteerRequest> rList;
    private OnItemClickListener mListener;
    private DatabaseReference databaseReference;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteButtonClick(int position); // Added delete method

    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public Completed(ArrayList<VolunteerRequest> rList, OnItemClickListener clickListener) {
        this.rList = rList;
        this.mListener = clickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deleterequest, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VolunteerRequest request = rList.get(position);
        holder.name.setText(request.getName());
        holder.date.setText(request.getDate());
        holder.time.setText(request.getTime());
        holder.phoneNumber.setText(request.getPhoneNumber());

        holder.delete.setOnClickListener(new View.OnClickListener() {
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
        return rList.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView time;

        TextView phoneNumber;
        ImageView delete;
        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name1);
            date = itemView.findViewById(R.id.date3);
            time = itemView.findViewById(R.id.time3);
            phoneNumber = itemView.findViewById(R.id.phoneNumber1);
            delete =itemView.findViewById(R.id.deleted);

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