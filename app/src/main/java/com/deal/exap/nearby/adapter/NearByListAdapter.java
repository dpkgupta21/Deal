package com.deal.exap.nearby.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.following.FollowingPartnerDetails;
import com.deal.exap.login.BaseActivity;

import java.util.ArrayList;

public class NearByListAdapter extends RecyclerView
        .Adapter<NearByListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "NearByListAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        LinearLayout llCouponItem;
        Button btnBuy;
        LinearLayout llBuy;
        ImageView ivLogo;

        public DataObjectHolder(View itemView) {
            super(itemView);
//            label = (TextView) itemView.findViewById(R.id.txt_category_name);
//            dateTime = (TextView) itemView.findViewById(R.id.txt_like_number);
            llCouponItem = (LinearLayout) itemView.findViewById(R.id.ll_filter_item);
            llBuy = (LinearLayout) itemView.findViewById(R.id.ll_buy);
            btnBuy = (Button) itemView.findViewById(R.id.btn_buy);
            ivLogo = (ImageView) itemView.findViewById(R.id.img_title);

            ivLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context instanceof BaseActivity){
                        BaseActivity act = (BaseActivity) context;
                        act.startActivity(new Intent(act, FollowingPartnerDetails.class));
                    }
                }
            });


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

    public NearByListAdapter(ArrayList<DataObject> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_row_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if(position%2==0) {
            holder.btnBuy.setVisibility(View.VISIBLE);
            holder.llBuy.setVisibility(View.GONE);
        }
        else{
            holder.btnBuy.setVisibility(View.GONE);
            holder.llBuy.setVisibility(View.VISIBLE);
        }
//        holder.label.setText(mDataset.get(position).getmText1());
//        holder.dateTime.setText(mDataset.get(position).getmText2());
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
