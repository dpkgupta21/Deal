package com.deal.exap.category.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.model.CategoryDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView
        .Adapter<CategoriesListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "WalletListAdapter";
    private List<CategoryDTO> categoryValues;
    private DisplayImageOptions options;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder  implements View
            .OnClickListener {
        TextView txtCategoryName;
        TextView txtFavNumber;
        TextView txtFolloNumber;
        ImageView ivThumb;
        ImageView imgLike;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txt_category_name);
            txtFavNumber = (TextView) itemView.findViewById(R.id.txt_like_number);
            txtFolloNumber = (TextView) itemView.findViewById(R.id.txt_follower_number);
            ivThumb = (ImageView) itemView.findViewById(R.id.thumbnail);
            imgLike = (ImageView) itemView.findViewById(R.id.img_like);
            ivThumb.setOnClickListener(this);
            imgLike.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public CategoriesListAdapter(List<CategoryDTO> categoryValues, Context context) {
        this.categoryValues = categoryValues;
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
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.txtCategoryName.setText(categoryValues.get(position).getName());
        holder.txtFavNumber.setText(categoryValues.get(position).getCategory_favourite_count());
        holder.txtFolloNumber.setText(categoryValues.get(position).getDeal_count());

        holder.imgLike.setTag(position);
        if (categoryValues.get(position).getFavourite().equalsIgnoreCase("1")) {
            holder.imgLike.setImageResource(R.drawable.heart_fill_icon);
        } else {
            holder.imgLike.setImageResource(R.drawable.heart_icon);

        }


        ImageLoader.getInstance().displayImage(categoryValues.get(position).getImage(), holder.ivThumb,
                options);
    }


    @Override
    public int getItemCount() {
        return categoryValues.size();
    }




    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
