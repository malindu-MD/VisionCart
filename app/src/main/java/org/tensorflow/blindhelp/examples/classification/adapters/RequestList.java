package org.tensorflow.blindhelp.examples.classification.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;

import com.google.firebase.database.DatabaseReference;

import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class RequestList extends RecyclerView.Adapter<RequestList.ViewHolder> {

    private ArrayList<VolunteerRequest> rList;
    private OnItemClickListener mListener;
    private DatabaseReference databaseReference;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public RequestList(ArrayList<VolunteerRequest> rList) {
        this.rList = rList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_requestcard, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VolunteerRequest request = rList.get(position);
        holder.name.setText(request.getName());
        holder.date.setText(request.getDate());
        holder.time.setText(request.getTime());
        holder.phoneNumber.setText(request.getPhoneNumber());



    }

    @Override
    public int getItemCount() {
        return rList.size();
    }
    public void updateDataSet(ArrayList<VolunteerRequest> newDataSet) {
        rList.clear();
        rList.addAll(newDataSet);
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView time;

        TextView phoneNumber;
        Button update;
        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name1);
            date = itemView.findViewById(R.id.date3);
            time = itemView.findViewById(R.id.time3);
            phoneNumber = itemView.findViewById(R.id.phoneNumber1);
            update =itemView.findViewById(R.id.accept);

            update.setOnClickListener(new View.OnClickListener() {
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




