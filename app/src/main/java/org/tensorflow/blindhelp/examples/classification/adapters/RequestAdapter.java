package org.tensorflow.blindhelp.examples.classification.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.models.VolunteerRequest;
import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private ArrayList<VolunteerRequest> rList;
    private OnItemClickListener mListener;

    private OnItemLongClickListener mLongClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }


    public RequestAdapter(ArrayList<VolunteerRequest> rList) {
        this.rList = rList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_to_instructor, parent, false);
        return new ViewHolder(itemView, mListener,mLongClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VolunteerRequest request = rList.get(position);
        holder.status.setText(request.getStatus());
        holder.requestNumber.setText("Request Number "+request.getRequestId());
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
        TextView status;
        TextView requestNumber;


        public ViewHolder(View itemView, final OnItemClickListener clickListener,final OnItemLongClickListener longClickListener) {
            super(itemView);
            requestNumber = itemView.findViewById(R.id.requestNumber);
            status=itemView.findViewById(R.id.status1);


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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(position);
                            return true; // Consume the long click event
                        }
                    }
                    return false;
                }
            });
        }
    }
}



