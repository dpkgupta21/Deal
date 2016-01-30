package com.deal.exap.nearby.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewReg14;
import com.deal.exap.model.CategoryDTO;
import com.deal.exap.model.ConuntriesDTO;

import java.util.List;

/**
 * Created by YusataInfotech on 1/30/2016.
 */
public class CategoryListAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryDTO> categoryList;


    public CategoryListAdapter(Context context, List<CategoryDTO> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
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

        holder.txtCountryCode.setText(categoryList.get(position).getName());
        return mView;
    }

    private static class ViewHolder {
        private MyTextViewReg14 txtCountryCode;
    }







}
