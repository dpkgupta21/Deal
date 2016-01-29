package com.deal.exap.partner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.CustomProgressDialog;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.DealDTO;
import com.deal.exap.model.PartnerDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.nearby.adapter.NearByListAdapter;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FollowingPartnerDetails extends BaseActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private int partnerID;
    // private ArrayList<DealDTO> dealList;
    private PartnerDTO partnerDTO;
    private DisplayImageOptions options;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_partner_details);
        mActivity = FollowingPartnerDetails.this;
        init();

    }


    private void init() {
        CustomProgressDialog.showProgDialog(mActivity, null);
        partnerID = getIntent().getIntExtra("partnerId", -1);
        setClick(R.id.iv_back);
        setClick(R.id.btn_follow_this_partner);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(FollowingPartnerDetails.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();

        getPartnerDetails();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_follow_this_partner:
                if (partnerDTO.getUser_follow().equalsIgnoreCase("0")) {
                    // From here we call follow the partner
                    followPartner(1);
                } else {
                    // From here we call unfollow the partner
                    followPartner(0);
                }
                break;
        }

    }

    private void getPartnerDetails() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_PARTNER);
            params.put("partner_id", partnerID + "");
            params.put("lang", Utils.getSelectedLanguage(this));
            params.put("user_id", Utils.getUserId(this));


          //  final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<DealDTO>>() {
                                }.getType();
                                // dealList = new Gson().fromJson(response.getJSONArray("deals").toString(), type);
                                partnerDTO = new Gson().fromJson(response.getJSONObject("partner").toString(),
                                        PartnerDTO.class);
                                setPartnerDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                            //pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //pdialog.dismiss();
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(FollowingPartnerDetails.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //pdialog.show();
        } else {
            CustomProgressDialog.hideProgressDialog();
            Utils.showNoNetworkDialog(this);
        }
    }


    private void setPartnerDetails() {


        if (partnerDTO.getDeals() != null) {
            mAdapter = new NearByListAdapter(partnerDTO.getDeals(), FollowingPartnerDetails.this);
            mRecyclerView.setAdapter(mAdapter);
        }

        ImageView imgThumnail = (ImageView) findViewById(R.id.thumbnail);
        ImageView partner = (ImageView) findViewById(R.id.img_company);

        ImageLoader.getInstance().displayImage(partnerDTO.getImage(), imgThumnail,
                options);
        ImageLoader.getInstance().displayImage(partnerDTO.getLogo(), partner,
                options);


        if (Utils.isArabic(FollowingPartnerDetails.this)) {
            setTextViewText(R.id.txt_place_tag, partnerDTO.getAddress_ara());
            setTextViewText(R.id.txt_title, partnerDTO.getName_ara());
        } else {
            setTextViewText(R.id.txt_place_tag, partnerDTO.getAddress_eng());
            setTextViewText(R.id.txt_title, partnerDTO.getName());
        }

        setTextViewText(R.id.txt_active_coupons_val, partnerDTO.getActive_coupan());
        setTextViewText(R.id.txt_downloads_val, partnerDTO.getDownload());
        setTextViewText(R.id.txt_followers_val, partnerDTO.getFollower());


        if (partnerDTO.getIs_featured().equalsIgnoreCase("0")) {
            setViewVisibility(R.id.img_featured, View.GONE);
        } else {
            setViewVisibility(R.id.img_featured, View.VISIBLE);
        }

        if (partnerDTO.getIs_chat_on().equalsIgnoreCase("0")) {
            setViewVisibility(R.id.iv_chat, View.GONE);
        } else {
            setViewVisibility(R.id.iv_chat, View.VISIBLE);
        }
        if (partnerDTO.getIs_follow().equalsIgnoreCase("1")) {
            setViewVisibility(R.id.btn_follow_this_partner, View.VISIBLE);
            if (partnerDTO.getUser_follow().equalsIgnoreCase("0")) {
                // Set Follow this partner label over button
                setButtonText(R.id.btn_follow_this_partner, getString(R.string.btn_follow_this_partner));
                //setViewVisibility(R.id.btn_follow_this_partner, View.VISIBLE);
            } else {
                // Set UnFollow this partner label over button
                setButtonText(R.id.btn_follow_this_partner, getString(R.string.btn_unfollow_this_partner));
                //setViewVisibility(R.id.btn_follow_this_partner, View.GONE);
            }
        } else {
            setViewVisibility(R.id.btn_follow_this_partner, View.GONE);
        }
//        ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//
//                Intent i = new Intent(FollowingPartnerDetails.this, BuyCouponActivity.class);
//                startActivity(i);
//
//            }
//        });

    }

    private void followPartner(int followStatus) {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.ADD_FOLLOWER);
            params.put("partner_id", partnerID + "");
            params.put("lang", Utils.getSelectedLanguage(this));
            params.put("user_id", Utils.getUserId(this));
            params.put("status", followStatus + "");


            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                if (Utils.getWebServiceStatus(response)) {
                                    finish();
                                    callFollowingFragment();

                                } else {
                                    //Utils.showDialog(FollowingPartnerDetails.this, "", Utils.getWebServiceMessage(response));
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
                    Utils.showExceptionDialog(FollowingPartnerDetails.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(this);
        }


    }


    private void callFollowingFragment() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fragmentName", getString(R.string.following_screen_title));
        startActivity(intent);
    }

}
