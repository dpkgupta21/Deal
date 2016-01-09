package com.deal.exap.category.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

public class CategoriesListAdapter extends RecyclerView
        .Adapter<CategoriesListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "WalletListAdapter";
    private ArrayList<CategoryDTO> mDataset;
    private static MyClickListener myClickListener;
    private DisplayImageOptions options;
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView txtCategoryName;
        TextView txtFavNumber;
        TextView txtFolloNumber;
        ImageView ivThumb;
        public DataObjectHolder(View itemView) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txt_category_name);
            txtFavNumber = (TextView) itemView.findViewById(R.id.txt_like_number);
            txtFolloNumber = (TextView) itemView.findViewById(R.id.txt_follower_number);
            ivThumb = (ImageView) itemView.findViewById(R.id.thumbnail);

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

    public CategoriesListAdapter(ArrayList<CategoryDTO> myDataset) {
        mDataset = myDataset;
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
                .inflate(R.layout.favorite_row_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txtCategoryName.setText(mDataset.get(position).getName());
        holder.txtFavNumber.setText("");
        holder.txtFolloNumber.setText(mDataset.get(position).getDeal_count());
        ImageLoader.getInstance().displayImage(mDataset.get(position).getImage(), holder.ivThumb,
                options);
    }

    public void addItem(CategoryDTO dataObj, int index) {
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
