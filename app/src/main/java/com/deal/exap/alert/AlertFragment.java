package com.deal.exap.alert;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.alert.adapter.AlertListAdapter;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseFragment;

import java.util.ArrayList;


public class AlertFragment extends BaseFragment {


    private View view;
    private ListView lvMessage, lvNotification;
    private Button btnMessage, btnNotification;
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

        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvMessage = (ListView) view.findViewById(R.id.lv_messsage);
        lvNotification = (ListView) view.findViewById(R.id.lv_notification);
        //applyStyle(switchbtn.getTextOn(), switchbtn.getTextOff());
        lvMessage.setAdapter(new AlertListAdapter(getActivity(), getDataSet(),1,getActivity()));
        lvNotification.setAdapter(new AlertListAdapter(getActivity(), getDataSet(),0,getActivity()));
        //setTitleFragment(getString(R.string.alert_screen_title));

        lvNotification.setVisibility(View.VISIBLE);
        lvMessage.setVisibility(View.GONE);
    }

    private void init(){
        setTouchNClick(R.id.btn_notification, view);
        setTouchNClick(R.id.btn_message, view);
        btnNotification = (Button) view.findViewById(R.id.btn_notification);
        btnNotification.setSelected(true);
        btnMessage = (Button) view.findViewById(R.id.btn_message);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.btn_message:
                btnMessage.setSelected(true);
                btnNotification.setSelected(false);
                lvMessage.setVisibility(View.VISIBLE);
                lvNotification.setVisibility(View.GONE);
                break;
            case R.id.btn_notification:
                btnMessage.setSelected(false);
                btnNotification.setSelected(true);
                lvMessage.setVisibility(View.GONE);
                lvNotification.setVisibility(View.VISIBLE);
                break;
        }
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

    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }
}
