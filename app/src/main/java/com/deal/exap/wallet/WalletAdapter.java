package com.deal.exap.wallet;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.model.WalletDTO;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView
        .Adapter<WalletAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "WalletAdapter";
    private ArrayList<WalletDTO> walletValues;
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView dateTime;
        LinearLayout llCouponItem;
        Button btnBuy;
        LinearLayout llBuy;
        ImageView ivLogo;

        public DataObjectHolder(View itemView) {
            super(itemView);
            llCouponItem = (LinearLayout) itemView.findViewById(R.id.ll_filter_item);
            llBuy = (LinearLayout) itemView.findViewById(R.id.ll_buy);
            btnBuy = (Button) itemView.findViewById(R.id.btn_buy);
            ivLogo = (ImageView) itemView.findViewById(R.id.img_title);


        }


    }

    public WalletAdapter(ArrayList<WalletDTO> walletValues, Context context) {
        walletValues = walletValues;
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
        if (position % 2 == 0) {
            holder.btnBuy.setVisibility(View.VISIBLE);
            holder.llBuy.setVisibility(View.GONE);
        } else {
            holder.btnBuy.setVisibility(View.GONE);
            holder.llBuy.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return walletValues.size();
    }


}
