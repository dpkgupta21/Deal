package com.deal.exap.category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.following.FollowingPartnerDetails;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.CategoryDTO;
import com.deal.exap.model.DealDTO;
import com.deal.exap.nearby.BuyCouponActivity;
import com.deal.exap.nearby.adapter.NearByListAdapter;
import com.deal.exap.utility.Constant;
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

public class CouponListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryDTO categoryDTO;
    private ArrayList<DealDTO>dealList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);

        init();
        getDealList();
    }

    private void init(){
        if(getIntent()!=null && getIntent().getExtras()!=null){
            categoryDTO = (CategoryDTO) getIntent().getExtras().getSerializable("categoryDTO");
        }
        if(categoryDTO!=null)
            setHeader(categoryDTO.getName());
        setLeftClick();
        setHeaderNormal();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void getDealList() {
        if(Utils.isOnline(this)){
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_CATEGORY_DEAL);
            params.put("lang", Utils.getSelectedLanguage(this));
            params.put("lng", "0");
            params.put("lat", "0");
            params.put("category_id", categoryDTO.getId());
            final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<DealDTO>>(){}.getType();
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
                    Utils.showExceptionDialog(CouponListActivity.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        }
        else{
            Utils.showNoNetworkDialog(CouponListActivity.this);
        }
    }

    public void setDealList(){
        mAdapter = new NearByListAdapter(dealList, this);
        mRecyclerView.setAdapter(mAdapter);


        ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent i;
                switch (v.getId()) {
                    case R.id.thumbnail:
                        i = new Intent(CouponListActivity.this, BuyCouponActivity.class);
                        i.putExtra("id", dealList.get(position).getId());
                        startActivity(i);
                        break;

                    case R.id.img_title:
                        i = new Intent(CouponListActivity.this, FollowingPartnerDetails.class);
                        i.putExtra("partnerId", dealList.get(position).getPartner_id());
                        startActivity(i);

                }


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
