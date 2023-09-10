package com.example.visioncart.adapters;



import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visioncart.R;
import com.example.visioncart.activities.helpDesk;
import com.example.visioncart.models.VolunteerRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

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

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                String sts="accept";
                String vol="pavan";
                String id=request.getID();


                HashMap User = new HashMap();
                User.put("status",sts);
                User.put("volunteer",vol);

                databaseReference = FirebaseDatabase.getInstance().getReference("VolunteerRequest");
                databaseReference.child(id).updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()){

                            notifyItemRangeChanged(position, rList.size());


                        }else {


                        }

                    }
                });







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
        Button update;
        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date2);
            time = itemView.findViewById(R.id.time2);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            update =itemView.findViewById(R.id.accept);

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




