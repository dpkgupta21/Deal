package com.deal.exap.alert.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;

import java.util.List;

public class AlertListAdapter extends BaseAdapter {

    private Object object;
    public List<DataObject> list;


    public AlertListAdapter(Object object, List<DataObject> list) {

        this.object = object;
        this.list = list;
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
