package com.deal.exap.alert.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.nearby.BuyCouponActivity;
import com.deal.exap.partner.ChatActivity;

import java.util.List;

public class AlertListAdapter extends BaseAdapter {

    private Object object;
    public List<DataObject> list;
    int type=0; //0 FOR NOTIFICATION, 1 FOR MESSAGE.
    private Context context;
    public AlertListAdapter(Object object, List<DataObject> list, int type, Context context) {

        this.object = object;
        this.list = list;
        this.type = type;
        this.context = context;
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
                view = (View) li.inflate(R.layout.alert_row_layout,
                        parent, false);
                holder = new ViewHolder();
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context instanceof BaseActivity) {
                        BaseActivity act = (BaseActivity)context;
                        if (type == 0) {
                            act.startActivity(new Intent(act, BuyCouponActivity.class));
                        } else {
                            act.startActivity(new Intent(act, ChatActivity.class));
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
