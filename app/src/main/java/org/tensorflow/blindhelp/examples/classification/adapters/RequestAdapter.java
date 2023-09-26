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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public RequestAdapter(ArrayList<VolunteerRequest> rList) {
        this.rList = rList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_to_instructor, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VolunteerRequest request = rList.get(position);
        holder.status.setText(request.getStatus());
        holder.date.setText(request.getDate());
        holder.time.setText(request.getTime());
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView status;
        TextView date;
        TextView time;

        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            status = itemView.findViewById(R.id.status1);
            date = itemView.findViewById(R.id.date1);
            time = itemView.findViewById(R.id.time1);

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



