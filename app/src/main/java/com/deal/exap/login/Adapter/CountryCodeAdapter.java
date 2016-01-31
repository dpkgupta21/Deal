package com.deal.exap.login.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewReg14;

import java.util.List;
import java.util.Map;

public class CountryCodeAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;


    public CountryCodeAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            mView = (View) mInflater.inflate(R.layout.layout_county_code_row,
                    parent, false);
            holder = new ViewHolder();
            holder.txtCountryCode = (MyTextViewReg14) mView.findViewById(R.id.txt_contry_code);
            holder.txtCountryName = (MyTextViewReg14) mView.findViewById(R.id.txt_county_name);

            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }

        holder.txtCountryCode.setText(list.get(position).get("dial_code"));
        holder.txtCountryName.setText(list.get(position).get("name"));
        return mView;
    }

    private static class ViewHolder {
        private MyTextViewReg14 txtCountryCode;
        private MyTextViewReg14 txtCountryName;
    }

}
