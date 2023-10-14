package org.tensorflow.blindhelp.examples.classification.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        void onDeleteButtonClick(int position);
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



        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
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
        return sList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pname;
        TextView pqnt;
        Button btnDelete;

        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            pname = itemView.findViewById(R.id.pname);
            pqnt = itemView.findViewById(R.id.pqnt);
            btnDelete = itemView.findViewById(R.id.btnDelete);

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
