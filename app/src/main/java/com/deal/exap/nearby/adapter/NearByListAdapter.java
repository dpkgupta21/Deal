package com.deal.exap.nearby.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.model.DealDTO;
import com.deal.exap.payment.BuyCouponActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class NearByListAdapter extends RecyclerView
        .Adapter<NearByListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "NearByListAdapter";
    private ArrayList<DealDTO> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;
    private DisplayImageOptions options;
    private DisplayImageOptions options1;
    private static boolean pressedTwoTime = false;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView tvDiscount;
        TextView tvEnddate;
        TextView tvDistance;
        TextView tvReview;
        TextView tvDetail;
        RatingBar ratingBar;
        LinearLayout llCouponItem;
        //Button btnBuy;
        LinearLayout llBuy;
        ImageView ivLogo;
        ImageView ivThumnail;
        TextView txt_final_price;
        TextView txt_visible_price;
        ;

        public DataObjectHolder(View itemView) {
            super(itemView);


            tvDiscount = (TextView) itemView.findViewById(R.id.txt_discount_rate);
            tvEnddate = (TextView) itemView.findViewById(R.id.txt_end_date_val);
            tvDistance = (TextView) itemView.findViewById(R.id.txt_distance_val);
            tvReview = (TextView) itemView.findViewById(R.id.txt_no_of_person);
            tvDetail = (TextView) itemView.findViewById(R.id.txt_on_which);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);

            txt_final_price = (TextView) itemView.findViewById(R.id.txt_final_price);
            txt_visible_price = (TextView) itemView.findViewById(R.id.txt_visible_price);

            llCouponItem = (LinearLayout) itemView.findViewById(R.id.ll_filter_item);
            llBuy = (LinearLayout) itemView.findViewById(R.id.ll_buy);
            //btnBuy = (Button) itemView.findViewById(R.id.btn_buy);
            ivLogo = (ImageView) itemView.findViewById(R.id.img_title);
            ivThumnail = (ImageView) itemView.findViewById(R.id.thumbnail);

//            btnBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    llBuy.setVisibility(View.VISIBLE);
//                    btnBuy.setVisibility(View.GONE);
//
//                }
//            });


//            llBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //if (pressedTwoTime) {
//                    myClickListener.onItemClick(getAdapterPosition(), view);
                    //}
//                    else {
//                        btnBuy.setVisibility(View.VISIBLE);
//                        llBuy.setVisibility(View.GONE);
//                    }
//                }
//            });

            Log.i(LOG_TAG, "Adding Listener");
            ivThumnail.setOnClickListener(this);
            ivLogo.setOnClickListener(this);
            // btnBuy.setOnClickListener(this);
            // llBuy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public void setDealList(ArrayList<DealDTO> mDataset) {
        this.mDataset = mDataset;
    }


    public NearByListAdapter(ArrayList<DealDTO> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.default_img)
                .showImageOnFail(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img)
                .build();
        options1 = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .build();
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
//        if (position % 2 == 0) {
//            holder.btnBuy.setVisibility(View.VISIBLE);
//            holder.llBuy.setVisibility(View.GONE);
//        } else {
//            holder.btnBuy.setVisibility(View.GONE);
//            holder.llBuy.setVisibility(View.VISIBLE);
//        }


//        if (mDataset.get(position).getType().equalsIgnoreCase("Paid")) {
//
//            holder.btnBuy.setText(context.getString(R.string.txt_buy));
//            holder.btnBuy.setBackgroundResource(R.drawable.btn_green_bcg_shape);
//        } else {
//            holder.btnBuy.setText(context.getString(R.string.btn_reedme));
//            holder.btnBuy.setBackgroundResource(R.drawable.btn_red_bcg_shape);
//        }

        ImageLoader.getInstance().displayImage(mDataset.get(position).getDeal_image(), holder.ivThumnail,
                options);
        ImageLoader.getInstance().displayImage(mDataset.get(position).getPartner_logo(), holder.ivLogo,
                options1);
        holder.tvDiscount.setText(mDataset.get(position).getDiscount() + " % - " + context.getString(R.string.txt_off));
        holder.tvEnddate.setText(mDataset.get(position).getEnd_date());
        if (HelpMe.isMiles(context))
            holder.tvDistance.setText(context.getString(R.string.txt_distance) + " " +
                    HelpMe.convertKMToMiles(mDataset.get(position).getDistance())
                    + " " + HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_MILES_ENG, context));
        else
            holder.tvDistance.setText(context.getString(R.string.txt_distance) + " " + mDataset.get(position).getDistance()
                    + " " + HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_KM_ENG, context));

        holder.tvDetail.setText(HelpMe.getRelatedPreferenceText(context,
                mDataset.get(position).getName_eng(),
                mDataset.get(position).getName_ara()));

        String noOfPerson = String.valueOf(mDataset.get(position).getReview());
        if(!noOfPerson.equalsIgnoreCase("0")){
            holder.tvReview.setText("(" + noOfPerson + ")");
        }

        if (Build.VERSION.SDK_INT >= 23) {
            LayerDrawable drawable = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            drawable.getDrawable(0).setColorFilter(Color.parseColor("#FFFFFFFF"),
                    PorterDuff.Mode.SRC_ATOP);
            holder.ratingBar.setRating(mDataset.get(position).getRating());
        } else {
            holder.ratingBar.setRating(mDataset.get(position).getRating());
        }

        holder.txt_final_price.setText(mDataset.get(position).getFinal_price() + " " +
                HelpMe.getCurrencySign(context));
        holder.txt_visible_price.setText(mDataset.get(position).getVisible_price() + " " +
                HelpMe.getCurrencySign(context));
        holder.txt_visible_price.setPaintFlags(holder.txt_visible_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void addItem(DealDTO dataObj, int index) {
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
