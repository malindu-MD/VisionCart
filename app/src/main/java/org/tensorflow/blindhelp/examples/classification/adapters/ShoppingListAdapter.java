package org.tensorflow.blindhelp.examples.classification.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.blindhelp.examples.classification.models.CartList;
import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private ArrayList<CartList> sList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }

    public ShoppingListAdapter(ArrayList<CartList> sList) {
        this.sList = sList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_shopping_item, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartList request = sList.get(position);
        //  holder.cartList.setText(request.getCartList());
        holder.name.setText(request.getName());
        holder.price.setText(request.getPrice());
        holder.mfg.setText(request.getMfg());
        holder.exp.setText(request.getExp());
        holder.details.setText(request.getDetails());
    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //        TextView cartList;
        TextView name;
        TextView price;
        TextView mfg;
        TextView exp;
        TextView details;


        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.nameId);
            price = itemView.findViewById(R.id.priceId);
            mfg = itemView.findViewById(R.id.mfgId);
            exp = itemView.findViewById(R.id.expId);
            details = itemView.findViewById(R.id.detailsId);


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