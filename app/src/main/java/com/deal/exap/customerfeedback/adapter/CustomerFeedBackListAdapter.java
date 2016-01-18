package com.deal.exap.customerfeedback.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.model.ReviewDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class CustomerFeedBackListAdapter extends BaseAdapter {

    private Object object;
    public List<ReviewDTO> list;
    private DisplayImageOptions options;

    public CustomerFeedBackListAdapter(Object object, List<ReviewDTO> list) {
        this.object = object;
        this.list = list;
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
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        try {
            if (view == null) {
                LayoutInflater li = (LayoutInflater) ((Activity) object)
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (View) li.inflate(R.layout.customer_feed_back_row_layout,
                        parent, false);
                holder = new ViewHolder();

                holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                holder.txt_desc = (TextView) view.findViewById(R.id.txt_comment);
                holder.txt_title = (TextView) view.findViewById(R.id.txt_customer_name);
                holder.txt_date_time = (TextView) view.findViewById(R.id.txt_post_date);
                holder.img = (ImageView) view.findViewById(R.id.img_user_image);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.ratingBar.setRating(list.get(position).getRating());
            holder.txt_desc.setText(list.get(position).getReview());
            holder.txt_title.setText(list.get(position).getUser_name());
            holder.txt_date_time.setText("|" + list.get(position).getDate() + " | " + list.get(position).getTime());

            ImageLoader.getInstance().displayImage(list.get(position).getUser_image(), holder.img,
                    options);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public static class ViewHolder {

        RatingBar ratingBar;
        TextView txt_title;
        TextView txt_desc;
        TextView txt_date_time;
        ImageView img;
    }


}
