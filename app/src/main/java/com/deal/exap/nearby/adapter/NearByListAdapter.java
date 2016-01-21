package com.deal.exap.nearby.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.deal.exap.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

public class NearByListAdapter extends RecyclerView
        .Adapter<NearByListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "NearByListAdapter";
    private ArrayList<DealDTO> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;
    private DisplayImageOptions options;
    private DisplayImageOptions options1;

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
        Button btnBuy;
        LinearLayout llBuy;
        ImageView ivLogo;
        ImageView ivThumnail;
        TextView txt_final_price;
        TextView txt_visible_price;

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
            btnBuy = (Button) itemView.findViewById(R.id.btn_buy);
            ivLogo = (ImageView) itemView.findViewById(R.id.img_title);
            ivThumnail = (ImageView) itemView.findViewById(R.id.thumbnail);

//            ivLogo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (context instanceof BaseActivity) {
//                        BaseActivity act = (BaseActivity) context;
//
//                        Intent intent = new Intent(act, FollowingPartnerDetails.class);
//                        act.startActivity(intent);
//                    }
//                }
//            });


            Log.i(LOG_TAG, "Adding Listener");
            ivThumnail.setOnClickListener(this);
            ivLogo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
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
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();
        options1 = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.burger_king)
                .showImageOnFail(R.drawable.burger_king)
                .showImageForEmptyUri(R.drawable.burger_king)
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
        if (position % 2 == 0) {
            holder.btnBuy.setVisibility(View.VISIBLE);
            holder.llBuy.setVisibility(View.GONE);
        } else {
            holder.btnBuy.setVisibility(View.GONE);
            holder.llBuy.setVisibility(View.VISIBLE);
        }
        ImageLoader.getInstance().displayImage(mDataset.get(position).getDeal_image(), holder.ivThumnail,
                options);
        ImageLoader.getInstance().displayImage(mDataset.get(position).getPartner_logo(), holder.ivLogo,
                options1);
        holder.tvDiscount.setText(mDataset.get(position).getDiscount() + " % Off");
        holder.tvEnddate.setText(mDataset.get(position).getEnd_date());
        holder.tvDistance.setText(mDataset.get(position).getDistance());
        if (Utils.isArebic(context))
            holder.tvDetail.setText(mDataset.get(position).getName_ara());
        else
            holder.tvDetail.setText(mDataset.get(position).getName_eng());
        holder.tvReview.setText("(" + mDataset.get(position).getReview() + ")");
        holder.ratingBar.setRating(mDataset.get(position).getRating());

        holder.txt_final_price.setText(mDataset.get(position).getFinal_price());
        holder.txt_visible_price.setText(mDataset.get(position).getVisible_price());
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
