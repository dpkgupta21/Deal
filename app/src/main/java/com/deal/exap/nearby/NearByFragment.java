package com.deal.exap.nearby;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.model.CategoryDTO;
import com.deal.exap.model.DealDTO;
import com.deal.exap.nearby.adapter.CategoryListAdapter;
import com.deal.exap.nearby.adapter.NearByListAdapter;
import com.deal.exap.partner.FollowingPartnerDetails;
import com.deal.exap.payment.BuyCouponActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NearByFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private LinearLayout llFilter;
    private TextView txtCategory;
    private Button btnKm, btnDistantLth,
            btnDistantHtl, btnDiscountLth, btnDiscountHtl,
            btnDateStl, btnDateLts;
    private ArrayList<DealDTO> dealList = new ArrayList<DealDTO>();
    private ArrayList<DealDTO> visibleDealList = new ArrayList<DealDTO>();
    private RecyclerView.Adapter mAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //private List<CategoryDTO> categoryList;
    private Dialog dialog;

    public static NearByFragment newInstance() {
        NearByFragment fragment = new NearByFragment();

        return fragment;
    }

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
        txtCategory = (TextView) view.findViewById(R.id.et_category);
        txtCategory.setText(getActivity().getString(R.string.all));
        setTouchNClick(R.id.btn_km, view);
        //setTouchNClick(R.id.btn_miles, view);
        //btnMiles = (Button) view.findViewById(R.id.btn_miles);
        btnKm = (Button) view.findViewById(R.id.btn_km);

//        setViewText(R.id.btn_miles, HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_MILES_ENG,
//                        getActivity().getApplicationContext()),
//                view);

        setViewText(R.id.btn_km,
                HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_KM_ENG,
                        getActivity().getApplicationContext()),
                view);

        selectedKMButton(DealPreferences.getDistanceUnit(getActivity().getApplicationContext()));

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
        setTouchNClick(R.id.et_category, view);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.nearby_screen_title));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getDealList();


        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getDealList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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


            case R.id.et_category:
                visibleDealList.clear();
//                for (DealDTO dealOBJ : dealList) {
//                    visibleDealList.add(dealOBJ);
//                }
//                visibleDealList=dealList;
                getCategoryList();
                break;
//            case R.id.btn_miles:
//                // DealPreferences.setDistanceUnit(getActivity().getApplicationContext(), Constant.DISTANCE_UNIT_MILES_ENG);
//                selectedKMButton(Constant.DISTANCE_UNIT_MILES_ENG);
//                mAdapter.notifyDataSetChanged();
////                btnMiles.setSelected(true);
////                btnMiles.setTextColor(getResources().getColor(R.color.white));
////                btnKm.setSelected(false);
////                btnKm.setTextColor(getResources().getColor(R.color.tv_color));
//                break;
            case R.id.btn_km:

                //DealPreferences.setDistanceUnit(getActivity().getApplicationContext(), Constant.DISTANCE_UNIT_KM_);
                selectedKMButton(Constant.DISTANCE_UNIT_KM_ENG);
                mAdapter.notifyDataSetChanged();
//                btnMiles.setSelected(false);
//                btnMiles.setTextColor(getResources().getColor(R.color.tv_color));
//                btnKm.setSelected(true);
//                btnKm.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_discount_htl:
                btnDiscountHtl.setSelected(true);
                btnDiscountLth.setSelected(false);
                sortByProperty(6);
                break;
            case R.id.btn_discount_lth:
                btnDiscountHtl.setSelected(false);
                btnDiscountLth.setSelected(true);
                sortByProperty(5);

                break;
            case R.id.btn_distance_htl:
                btnDistantHtl.setSelected(true);
                btnDistantLth.setSelected(false);
                sortByProperty(2);
                break;
            case R.id.btn_distance_lth:
                btnDistantHtl.setSelected(false);
                btnDistantLth.setSelected(true);
                sortByProperty(1);
                break;
            case R.id.btn_date_lts:
                btnDateLts.setSelected(true);
                btnDateStl.setSelected(false);
                sortByProperty(3);
                break;
            case R.id.btn_date_stl:
                btnDateLts.setSelected(false);
                btnDateStl.setSelected(true);
                sortByProperty(4);
                break;
        }
    }

    public void getDealList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_NEAR_DEAL);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("lat", String.valueOf(DealPreferences.getLatitude(getActivity().
                    getApplicationContext())));
            params.put("lng", String.valueOf(DealPreferences.getLongitude(getActivity().
                    getApplicationContext())));
            params.put("user_id", Utils.getUserId(getActivity()));

            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    TextView txt_blank = (TextView) view.findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.GONE);
                                    Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                    Type type = new TypeToken<ArrayList<DealDTO>>() {
                                    }.getType();
                                    dealList = new Gson().fromJson(response.getJSONArray("deal").toString(), type);
//                                    for (DealDTO dealOBJ : dealList) {
//                                        visibleDealList.add(dealOBJ);
//                                    }
                                    //visibleDealList = dealList;
                                    setDealList();
                                } else {
                                    mRecyclerView.setVisibility(View.GONE);
                                    String msg = response.getString("message");
                                    TextView txt_blank = (TextView) view.findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.VISIBLE);
                                    txt_blank.setText(msg);
                                }
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
            // set request time-out
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                        break;
                    case R.id.ll_buy:
                        i = new Intent(getActivity(), BuyCouponActivity.class);
                        i.putExtra("id", dealList.get(position).getId());
                        startActivity(i);
                        break;

                }


            }
        });
    }


    private void sortByProperty(final int sort_type) {
        try {
            Collections.sort(visibleDealList, new Comparator<DealDTO>() {
                @Override
                public int compare(DealDTO lhs, DealDTO rhs) {
                    // TODO Auto-generated method stub
                    int result = 0;
                    switch (sort_type) {
                        case 1:
                            if (Integer.parseInt(lhs.getDistance()) < Integer.parseInt(rhs.getDistance()))
                                result = -1;
                            else
                                result = 1;
                            break;
                        case 2:
                            if (Integer.parseInt(rhs.getDistance()) < Integer.parseInt(lhs.getDistance()))
                                result = -1;
                            else
                                result = 1;
                            break;
                        case 3:
                            SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd MMM yyyy");
                            try {
                                Date d1 = (Date) simpleFormatter.parse(lhs.getEnd_date());
                                Date d2 = (Date) simpleFormatter.parse(rhs.getEnd_date());
                                result = d1.compareTo(d2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            SimpleDateFormat simpleFormatter1 = new SimpleDateFormat("dd MMM yyyy");
                            try {
                                Date d1 = (Date) simpleFormatter1.parse(lhs.getEnd_date());
                                Date d2 = (Date) simpleFormatter1.parse(rhs.getEnd_date());
                                result = d2.compareTo(d1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 5:
                            if (Integer.parseInt(lhs.getDiscount()) < Integer.parseInt(rhs.getDiscount()))
                                result = -1;
                            else
                                result = 1;
                            break;

                        case 6:
                            if (Integer.parseInt(rhs.getDiscount()) < Integer.parseInt(lhs.getDiscount()))
                                result = -1;
                            else
                                result = 1;
                            break;
                    }
                    return result;
                }
            });
            ((NearByListAdapter) mAdapter).setDealList(visibleDealList);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getCategoryList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_CATEOGRY_LIST);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                    Type type = new TypeToken<ArrayList<CategoryDTO>>() {
                                    }.getType();

                                    List<CategoryDTO> categoryList = new Gson().fromJson(response.getJSONArray("category").toString(), type);
                                    CategoryDTO categoryDTO = new CategoryDTO();
                                    categoryDTO.setName("All");
                                    categoryDTO.setId("0");
                                    categoryList.add(0, categoryDTO);
                                    openCategoryDialog(categoryList);
                                } else {
                                    String msg = response.getString("message");

                                }
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
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();

        } else {

        }

    }


    private void openCategoryDialog(final List<CategoryDTO> categoryList) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        dialog.setCancelable(false);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getActivity(), categoryList);
        listView.setAdapter(categoryListAdapter);
        dialog.show();
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        if (dialog != null) {
                            dialog = null;
                        }

                        txtCategory.setText(categoryList.get(position).getName());

                        if (categoryList.get(position).getName().equalsIgnoreCase("All")) {
                            for (DealDTO dealOBJ : dealList) {
                                visibleDealList.add(dealOBJ);
                            }
                        } else {
                            for (int i = 0; i < dealList.size(); i++) {
                                if (dealList.get(i).getCategory_id() == Integer.parseInt(categoryList.get(position).getId())) {
                                    visibleDealList.add(dealList.get(i));
                                }
                            }
                        }
                        ((NearByListAdapter) mAdapter).setDealList(visibleDealList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
        );
    }


    private void selectedKMButton(String STATUS_CODE) {
        if (STATUS_CODE.contains(Constant.DISTANCE_UNIT_KM_ENG)) {

            btnKm.setBackgroundColor(getResources().getColor(R.color.btn_color));
            //btnMiles.setBackgroundColor(getResources().getColor(R.color.white));

            btnKm.setTextColor(getResources().getColor(R.color.white));
            //btnMiles.setTextColor(getResources().getColor(R.color.black));

        } else {

            //btnMiles.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btnKm.setBackgroundColor(getResources().getColor(R.color.white));

            //btnMiles.setTextColor(getResources().getColor(R.color.white));
            btnKm.setTextColor(getResources().getColor(R.color.black));
        }
    }


}
