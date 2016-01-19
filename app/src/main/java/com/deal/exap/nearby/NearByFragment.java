package com.deal.exap.nearby;

import android.app.ProgressDialog;
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
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.following.FollowingPartnerDetails;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.model.DealDTO;
import com.deal.exap.nearby.adapter.NearByListAdapter;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.TJPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NearByFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private LinearLayout llFilter;
    private Button btnKm, btnMiles, btnDistantLth, btnDistantHtl, btnDiscountLth, btnDiscountHtl, btnDateStl, btnDateLts;
    private ArrayList<DealDTO> dealList;
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

    private void init() {
        llFilter = (LinearLayout) view.findViewById(R.id.ll_filter);
        setTouchNClick(R.id.btn_km, view);
        setTouchNClick(R.id.btn_miles, view);
        btnMiles = (Button) view.findViewById(R.id.btn_miles);
        btnKm = (Button) view.findViewById(R.id.btn_km);
        btnDistantLth = (Button) view.findViewById(R.id.btn_distance_lth);
        btnDistantLth.setSelected(true);
        btnDistantHtl = (Button) view.findViewById(R.id.btn_distance_htl);

        btnDiscountLth = (Button) view.findViewById(R.id.btn_discount_lth);
        btnDiscountLth.setSelected(true);
        btnDiscountHtl = (Button) view.findViewById(R.id.btn_discount_htl);
        btnDateStl = (Button) view.findViewById(R.id.btn_date_stl);
        btnDateStl.setSelected(true);
        btnDateLts = (Button) view.findViewById(R.id.btn_date_lts);

        setTouchNClick(R.id.btn_discount_htl, view);
        setTouchNClick(R.id.btn_discount_lth, view);
        setTouchNClick(R.id.btn_distance_lth, view);
        setTouchNClick(R.id.btn_distance_htl, view);
        setTouchNClick(R.id.btn_date_lts, view);
        setTouchNClick(R.id.btn_date_stl, view);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.nearby_screen_title));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getDealList();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_filter:
                if (llFilter.getVisibility() == View.GONE)
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
        switch (arg0.getId()) {
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
            case R.id.btn_discount_htl:
                btnDiscountHtl.setSelected(true);
                btnDiscountLth.setSelected(false);
                break;
            case R.id.btn_discount_lth:
                btnDiscountHtl.setSelected(false);
                btnDiscountLth.setSelected(true);
                break;
            case R.id.btn_distance_htl:
                btnDistantHtl.setSelected(true);
                btnDistantLth.setSelected(false);
                break;
            case R.id.btn_distance_lth:
                btnDistantHtl.setSelected(false);
                btnDistantLth.setSelected(true);
                break;
            case R.id.btn_date_lts:
                btnDateLts.setSelected(true);
                btnDateStl.setSelected(false);
                break;
            case R.id.btn_date_stl:
                btnDateLts.setSelected(false);
                btnDateStl.setSelected(true);
                break;
        }
    }

    public void getDealList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_NEAR_DEAL);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("lat", String.valueOf(TJPreferences.getLatitude(getActivity().
                    getApplicationContext())));
            params.put("lng", String.valueOf(TJPreferences.getLongitude(getActivity().
                    getApplicationContext())));

            final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<DealDTO>>() {
                                }.getType();
                                dealList = new Gson().fromJson(response.getJSONArray("deal").toString(), type);
                                setDealList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }
    }

    public void setDealList() {
        mAdapter = new NearByListAdapter(dealList, getActivity());
        mRecyclerView.setAdapter(mAdapter);


        ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent i;
                switch (v.getId()) {
                    case R.id.thumbnail:
                        i = new Intent(getActivity(), BuyCouponActivity.class);
                        i.putExtra("id", dealList.get(position).getId());
                        startActivity(i);
                        break;

                    case R.id.img_title:
                        i = new Intent(getActivity(), FollowingPartnerDetails.class);
                        i.putExtra("partnerId", dealList.get(position).getPartner_id());
                        startActivity(i);

                }


            }
        });
    }
}
