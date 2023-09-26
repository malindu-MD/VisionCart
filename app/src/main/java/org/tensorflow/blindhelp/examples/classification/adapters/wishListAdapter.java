package org.tensorflow.blindhelp.examples.classification.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.models.WishList;

import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class wishListAdapter extends RecyclerView.Adapter<wishListAdapter.ViewHolder>{



    private ArrayList<WishList> sList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public wishListAdapter(ArrayList<WishList> sList) {
        this.sList = sList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.onewishlist, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WishList request = sList.get(position);
        holder.pname.setText(request.getDetaillsID());
        holder.pqnt.setText(request.getQuantityID());

    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pname;
        TextView pqnt;

        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            pname = itemView.findViewById(R.id.pname);
            pqnt = itemView.findViewById(R.id.pqnt);

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
