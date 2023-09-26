package org.tensorflow.blindhelp.examples.classification.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.models.Offers;

import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private OnItemClickListener mListener;
    ArrayList<Offers> list;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public MyAdapter(ArrayList<Offers> list) {
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offercard,parent,false);
        return  new MyViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Offers offers = list.get(position);
        holder.pname.setText(offers.getPname());
//        holder.fromDay.setText(offers.getFromday());
//        holder.fromMonth.setText(offers.getFromMonth());
//        holder.toDay.setText(offers.getToDay());
//        holder.toMonth.setText(offers.getToMonth());
       holder.offerDet.setText(offers.getOfferDetails());


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pname,offerDet;

        public MyViewHolder(@NonNull View itemView,final OnItemClickListener clickListener) {
            super(itemView);

            pname = itemView.findViewById(R.id.prName);
            offerDet = itemView.findViewById(R.id.offerDet);

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