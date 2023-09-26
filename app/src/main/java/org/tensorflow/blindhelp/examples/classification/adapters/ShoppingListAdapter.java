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
        holder.cartList.setText(request.getCartList());

    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartList;

        public ViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            cartList = itemView.findViewById(R.id.oneItemID);

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