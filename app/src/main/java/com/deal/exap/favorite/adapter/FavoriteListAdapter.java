package com.deal.exap.favorite.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewReg16;
import com.deal.exap.model.CategoryDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView
        .Adapter<FavoriteListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "FavoriteListAdapter";
    private List<CategoryDTO> favoriteValues;
    private static Context context;
    private DisplayImageOptions options;

    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        MyTextViewReg16 txt_like_number;
        MyTextViewReg16 txt_follower_number;
        MyTextViewReg16 txt_category_name;
        ImageView thumbnail;
        ImageView img_category;
        ImageView img_like;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_like_number = (MyTextViewReg16) itemView.findViewById(R.id.txt_like_number);
            txt_follower_number = (MyTextViewReg16) itemView.findViewById(R.id.txt_follower_number);
            txt_category_name = (MyTextViewReg16) itemView.findViewById(R.id.txt_category_name);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            img_category = (ImageView) itemView.findViewById(R.id.img_category);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);

            thumbnail.setOnClickListener(this);
            img_like.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public FavoriteListAdapter(List<CategoryDTO> favoriteValues, Context context) {
        this.favoriteValues = favoriteValues;
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
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_row_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txt_like_number.setText(favoriteValues.get(position).getCategory_favourite_count());
        holder.txt_category_name.setText(favoriteValues.get(position).getName());
        holder.txt_follower_number.setText(favoriteValues.get(position).getDeal_count());
        ImageLoader.getInstance().displayImage(favoriteValues.get(position).getImage(), holder.thumbnail,
                options);

        if (favoriteValues.get(position).getFavourite().equalsIgnoreCase("1")) {
            holder.img_like.setImageResource(R.drawable.heart_fill_icon);
        } else {
            holder.img_like.setImageResource(R.drawable.heart_icon);

        }

        ImageLoader.getInstance().displayImage(favoriteValues.get(position).getIcon_image(), holder.img_category,
                options);
    }


    @Override
    public int getItemCount() {
        return favoriteValues.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
