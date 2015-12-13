package com.deal.exap.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.nearby.adapter.NearByListAdapter;

import java.util.ArrayList;


public class NearByFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private LinearLayout llFilter;
    private Button btnKm, btnMiles;
//    public static NearByFragment newInstance() {
//        NearByFragment fragment = new NearByFragment();
//
//        return fragment;
//    }

    public NearByFragment() {
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

        view = inflater.inflate(R.layout.fragment_near_by, container, false);

        init();

        return view;
    }

    private void init(){
        llFilter = (LinearLayout) view.findViewById(R.id.ll_filter);
        setTouchNClick(R.id.btn_km, view);
        setTouchNClick(R.id.btn_miles, view);
        btnMiles = (Button) view.findViewById(R.id.btn_miles);
        btnKm = (Button) view.findViewById(R.id.btn_km);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleFragment(getString(R.string.nearby_screen_title));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NearByListAdapter(getDataSet(), getActivity());
        mRecyclerView.setAdapter(mAdapter);


        ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent i = new Intent(getActivity(),RedeemCouponActivity.class);
                startActivity(i);

            }
        });

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_filter:
                if(llFilter.getVisibility()==View.GONE)
                    llFilter.setVisibility(View.VISIBLE);
                else
                     llFilter.setVisibility(View.GONE);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.btn_miles:
                btnMiles.setSelected(true);
                btnMiles.setTextColor(getResources().getColor(R.color.white));
                btnKm.setSelected(false);
                btnKm.setTextColor(getResources().getColor(R.color.tv_color));
                break;
            case R.id.btn_km:
                btnMiles.setSelected(false);
                btnMiles.setTextColor(getResources().getColor(R.color.tv_color));
                btnKm.setSelected(true);
                btnKm.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
}
