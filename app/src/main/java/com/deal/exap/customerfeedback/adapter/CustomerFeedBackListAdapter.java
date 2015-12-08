package com.deal.exap.customerfeedback.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;

import java.util.ArrayList;

public class CustomerFeedBackListAdapter extends RecyclerView
        .Adapter<CustomerFeedBackListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "CustomerFeedBackListAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView txtCategoryName;
        TextView txtFavNumber;
        TextView txtFolloNumber;

        public DataObjectHolder(View itemView) {
            super(itemView);
//            txtCategoryName = (TextView) itemView.findViewById(R.id.txt_category_name);
//            txtFavNumber = (TextView) itemView.findViewById(R.id.txt_like_number);
//            txtFolloNumber = (TextView) itemView.findViewById(R.id.txt_follower_number);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CustomerFeedBackListAdapter(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_feed_back_row_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
//        holder.txtCategoryName.setText(mDataset.get(position).getmText1());
//        holder.txtFavNumber.setText(mDataset.get(position).getmText2());
//        holder.txtFolloNumber.setText(mDataset.get(position).getmText2());
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
