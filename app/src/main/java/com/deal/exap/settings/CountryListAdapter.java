package com.deal.exap.settings;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewReg14;
import com.deal.exap.model.CountriesDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;

import java.util.List;

public class CountryListAdapter extends BaseAdapter {
    private Context context;
    private List<CountriesDTO> countriesList;


    public CountryListAdapter(Context context, List<CountriesDTO> countriesList) {
        this.context = context;
        this.countriesList = countriesList;
    }


    @Override
    public int getCount() {
        return countriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return countriesList.get(position);
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
            mView = (View) mInflater.inflate(R.layout.layout_currency_row,
                    parent, false);
            holder = new ViewHolder();
            holder.txtCountryCode = (MyTextViewReg14) mView.findViewById(R.id.txt_currency);

            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        if (DealPreferences.getAPP_LANG(context).equalsIgnoreCase(Constant.LANG_ENGLISH_CODE)) {
            holder.txtCountryCode.setText(countriesList.get(position).getName());
        } else if (DealPreferences.getAPP_LANG(context).equalsIgnoreCase(Constant.LANG_ARABIC_CODE)) {
            holder.txtCountryCode.setText(countriesList.get(position).getName_ara());

        }
        return mView;
    }

    private static class ViewHolder {
        private MyTextViewReg14 txtCountryCode;
    }


}
