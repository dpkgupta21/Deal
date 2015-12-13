package com.deal.exap.alert;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.deal.exap.R;
import com.deal.exap.alert.adapter.AlertListAdapter;
import com.deal.exap.favorite.bean.DataObject;

import java.util.ArrayList;


public class AlertFragment extends Fragment {


    private View view;
    private Switch switchbtn;
    private ListView listView;
//    public static AlertFragment newInstance() {
//        AlertFragment fragment = new AlertFragment();
//        return fragment;
//    }

    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alert, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switchbtn = (Switch) view.findViewById(R.id.swtch);
        listView = (ListView) view.findViewById(R.id.alert_list_view);
        applyStyle(switchbtn.getTextOn(), switchbtn.getTextOff());
        listView.setAdapter(new AlertListAdapter(getActivity(), getDataSet()));

    }

    public void applyStyle(CharSequence switchTxtOn, CharSequence switchTxtOff) {

        Spannable styleText = new SpannableString(switchTxtOn);
        StyleSpan style = new StyleSpan(Typeface.BOLD);
        styleText.setSpan(style, 0, switchTxtOn.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        styleText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, switchTxtOn.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        switchbtn.setTextOn(styleText);

        styleText = new SpannableString(switchTxtOff);
        styleText.setSpan(style, 0, switchTxtOff.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        styleText.setSpan(new ForegroundColorSpan(Color.RED), 0, switchTxtOff.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        switchbtn.setTextOff(styleText);

    }

    public void togglestatehandler(View v) {
        Switch switchbtn = (Switch) v;
        boolean isChecked = switchbtn.isChecked();

        if (isChecked) {
            Toast.makeText(getActivity(), "STARTED......", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "STOPPED......", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }


    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 10; index++) {
            DataObject obj = new DataObject("Auto",
                    "" + index);
            results.add(index, obj);
        }
        return results;
    }
}
