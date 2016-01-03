package com.deal.exap.settings;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.partner.ChatActivity;
import com.deal.exap.utility.Utils;

import java.util.ArrayList;

public class PaymentDetailsActivity extends BaseActivity {

    private ArrayList<String> months;
    private ArrayList<String> years;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        init();
    }

    private void init() {
        months = Utils.getMonths();
        years = Utils.getYears();
        setClick(R.id.txt_month);
        setClick(R.id.txt_year);
        setViewText(R.id.txt_month, months.get(0));
        setViewText(R.id.txt_year, years.get(0));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_month:
                openMonthDialog();
                break;
            case R.id.txt_year:
                openYearDialog();
                break;

        }
    }


    public void openMonthDialog() {
        final Dialog dialog = new Dialog(PaymentDetailsActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, months);
        listView.setAdapter(adapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewText(R.id.txt_month, months.get(position));
                dialog.dismiss();
            }
        });


    }

    public void openYearDialog() {
        final Dialog dialog = new Dialog(PaymentDetailsActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, years);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewText(R.id.txt_year, years.get(position));
                dialog.dismiss();
            }
        });


    }


}
