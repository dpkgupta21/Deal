package com.deal.exap.alert.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.partner.FollowingPartnerDetails;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.NotificationDTO;
import com.deal.exap.nearby.BuyCouponActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

public class AlertListAdapter extends BaseAdapter {

    private Object object;
    public ArrayList<NotificationDTO> list;
    int type = 0; //0 FOR NOTIFICATION, 1 FOR MESSAGE.
    private Context context;
    private DisplayImageOptions options;

    public AlertListAdapter(Object object, ArrayList<NotificationDTO> list, int type, Context context) {

        this.object = object;
        this.list = list;
        this.type = type;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        try {
            if (view == null) {
                LayoutInflater li = (LayoutInflater) ((Activity) object)
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (View) li.inflate(R.layout.alert_row_layout,
                        parent, false);
                holder = new ViewHolder();

                holder.txt_date_time = (TextView) view.findViewById(R.id.date_time);
                holder.txt_desc = (TextView) view.findViewById(R.id.txt_description);
                holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
                holder.img = (ImageView) view.findViewById(R.id.img_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.txt_date_time.setText(list.get(position).getTimestamp());
            holder.txt_desc.setText(list.get(position).getMessage());
            holder.txt_title.setText(list.get(position).getUser());
            ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.img,
                    options);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof BaseActivity) {
                        BaseActivity act = (BaseActivity) context;
                        Intent i;
                        if (list.get(position).getType().equalsIgnoreCase("Partner")) {

                            i = new Intent(act, FollowingPartnerDetails.class);
                            i.putExtra("partnerId", list.get(position).getDataid());
                            act.startActivity(i);
                        } else {

                            i = new Intent(act, BuyCouponActivity.class);
                            i.putExtra("id", list.get(position).getDataid() + "");
                            act.startActivity(i);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public static class ViewHolder {
        TextView txt_title;
        TextView txt_desc;
        TextView txt_date_time;
        ImageView img;
    }

}
