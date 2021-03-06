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
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.ConversionsDTO;
import com.deal.exap.chat.ChatActivity;
import com.deal.exap.utility.HelpMe;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

public class MessageListAdapter extends BaseAdapter {

    private Object object;
    public ArrayList<ConversionsDTO> list;
    private Context context;
    private DisplayImageOptions options;

    public MessageListAdapter(Object object, ArrayList<ConversionsDTO> list, int type, Context context) {

        this.object = object;
        this.list = list;
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
            holder.txt_title.setText(HelpMe.getRelatedPreferenceText(context,
                    list.get(position).getUser(),
                    list.get(position).getUser_ara()));
            ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.img,
                    options);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof BaseActivity) {
                        BaseActivity act = (BaseActivity) context;
                        Intent i;
                        i = new Intent(act, ChatActivity.class);
                        i.putExtra("receiverID", list.get(position).getUser_id());
                        act.startActivity(i);

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

