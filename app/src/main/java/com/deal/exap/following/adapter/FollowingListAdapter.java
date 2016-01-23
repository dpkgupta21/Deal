package com.deal.exap.following.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.model.FollowingDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


public class FollowingListAdapter extends RecyclerView
        .Adapter<FollowingListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "FollowingListAdapter";
    private List<FollowingDTO> followingValues;
    private static Context context;
    private DisplayImageOptions options;


    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView txt_active_coupons_val;
        TextView txt_downloads_val;
        TextView txt_followers_val;
        TextView txt_place_tag;
        TextView txt_title;
        ImageView thumbnail;
        ImageView img_company;
        ImageView img_featured;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_active_coupons_val = (TextView) itemView.findViewById(R.id.txt_active_coupons_val);
            txt_downloads_val = (TextView) itemView.findViewById(R.id.txt_downloads_val);
            txt_followers_val = (TextView) itemView.findViewById(R.id.txt_followers_val);
            txt_place_tag = (TextView) itemView.findViewById(R.id.txt_place_tag);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            img_company = (ImageView) itemView.findViewById(R.id.img_company);
            img_featured = (ImageView) itemView.findViewById(R.id.img_featured);


        }

    }


    public FollowingListAdapter(List<FollowingDTO> followingValues, Context context) {
        this.followingValues = followingValues;
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
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.following_row_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txt_active_coupons_val.setText(followingValues.get(position).getActive_coupan());
        holder.txt_downloads_val.setText(followingValues.get(position).getDownload());
        holder.txt_followers_val.setText(followingValues.get(position).getFollower());

        if (Utils.getSelectedLanguage(context).equalsIgnoreCase(Constant.LANG_ENGLISH_CODE)) {
            holder.txt_place_tag.setText(followingValues.get(position).getAddress_eng() + " " + followingValues.get(position).getLocation());

        } else {
            holder.txt_place_tag.setText(followingValues.get(position).getAddress_ara() + " " + followingValues.get(position).getLocation());
        }

        if(followingValues.get(position).getIs_featured()==1){
            holder.img_featured.setVisibility(View.VISIBLE);
        }else{
            holder.img_featured.setVisibility(View.GONE);
        }

        holder.txt_title.setText(followingValues.get(position).getName());

        ImageLoader.getInstance().displayImage(followingValues.get(position).getImage(), holder.thumbnail,
                options);
        ImageLoader.getInstance().displayImage(followingValues.get(position).getLogo(), holder.img_company,
                options);
    }


    @Override
    public int getItemCount() {
        return followingValues.size();
    }


}
