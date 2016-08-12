package com.deal.exap.partner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.chat.ChatActivity;
import com.deal.exap.customviews.CustomProgressDialog;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.DealDTO;
import com.deal.exap.model.PartnerDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.nearby.adapter.NearByListAdapter;
import com.deal.exap.payment.BuyCouponActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
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
    private int partnerID;
    // private ArrayList<DealDTO> dealList;
    private PartnerDTO partnerDTO;
    private DisplayImageOptions options;
    private Activity mActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_partner_details);
        mActivity = FollowingPartnerDetails.this;

        GPSTracker gpsTracker = new GPSTracker(mActivity);
        init();

        getPartnerDetails();

        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getPartnerDetails();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean enable = false;
                if (mRecyclerView != null && mRecyclerView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = ((LinearLayoutManager) mRecyclerView.
                            getLayoutManager()).findFirstVisibleItemPosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mRecyclerView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }


    private void init() {
        CustomProgressDialog.showProgDialog(mActivity, null);
        partnerID = getIntent().getIntExtra("partnerId", -1);
        setClick(R.id.iv_back);
        setClick(R.id.btn_follow_this_partner);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.default_img)
                .showImageOnFail(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img)
                .build();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.iv_chat:

                Intent i = new Intent(mActivity, ChatActivity.class);
                // i.putExtra("partnerDTO", partnerDTO);
                i.putExtra("receiverID", partnerDTO.getId());
                startActivity(i);

                break;
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
        try {
            if (Utils.isOnline(mActivity)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.GET_PARTNER);
                params.put("partner_id", partnerID + "");
                params.put("lang", Utils.getSelectedLanguage(mActivity));
                params.put("user_id", Utils.getUserId(mActivity));
                params.put("lat", String.valueOf(DealPreferences.getLatitude(mActivity)));
                params.put("lng", String.valueOf(DealPreferences.getLongitude(mActivity)));

                //  final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {
                                        Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                        Type type = new TypeToken<ArrayList<DealDTO>>() {
                                        }.getType();
                                        // dealList = new Gson().fromJson(response.getJSONArray("deals").toString(), type);
                                        partnerDTO = new Gson().fromJson(response.getJSONObject("partner").toString(),
                                                PartnerDTO.class);
                                        setPartnerDetails();
                                    } else {
                                        mRecyclerView.setVisibility(View.GONE);
                                        String msg = response.getString("message");
                                        TextView txt_blank = (TextView) findViewById(R.id.txt_blank);
                                        txt_blank.setVisibility(View.VISIBLE);
                                        txt_blank.setText(msg);
                                    }
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
                        Utils.showExceptionDialog(mActivity);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setPartnerDetails() {

        try {


            if (partnerDTO.getDeals() != null) {
                mAdapter = new NearByListAdapter(partnerDTO.getDeals(), mActivity);
                mRecyclerView.setAdapter(mAdapter);
                ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        Intent i;
                        switch (v.getId()) {
                            case R.id.thumbnail:
                                i = new Intent(mActivity, BuyCouponActivity.class);
                                i.putExtra("id", partnerDTO.getDeals().get(position).getId());
                                startActivity(i);
                                break;
                            case R.id.ll_buy:
                                i = new Intent(mActivity, BuyCouponActivity.class);
                                i.putExtra("id", partnerDTO.getDeals().get(position).getId());
                                startActivity(i);
                                break;

                        }


                    }
                });
            }

            ImageView imgThumnail = (ImageView) findViewById(R.id.thumbnail);
            ImageView partner = (ImageView) findViewById(R.id.img_company);

            ImageLoader.getInstance().displayImage(partnerDTO.getImage(), imgThumnail,
                    options);
            ImageLoader.getInstance().displayImage(partnerDTO.getLogo(), partner,
                    options);


            if (HelpMe.isArabic(mActivity)) {
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
                setClick(R.id.iv_chat);
            }
//        if (partnerDTO.getIs_follow().equalsIgnoreCase("1")) {
//            setViewVisibility(R.id.btn_follow_this_partner, View.VISIBLE);
            if (partnerDTO.getUser_follow().equalsIgnoreCase("0")) {
                // Set Follow this partner label over button
                setButtonText(R.id.btn_follow_this_partner, getString(R.string.btn_follow_this_partner));
                //setViewVisibility(R.id.btn_follow_this_partner, View.VISIBLE);
            } else {
                // Set UnFollow this partner label over button
                setButtonText(R.id.btn_follow_this_partner, getString(R.string.btn_unfollow_this_partner));
                //setViewVisibility(R.id.btn_follow_this_partner, View.GONE);
            }
//        } else {
//            setViewVisibility(R.id.btn_follow_this_partner, View.GONE);
//        }
//        ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//
//                Intent i = new Intent(FollowingPartnerDetails.this, BuyCouponActivity.class);
//                startActivity(i);
//
//            }
//        });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void followPartner(int followStatus) {
        try {
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
                        Utils.showExceptionDialog(mActivity);
                        //       CustomProgressDialog.hideProgressDialog();
                    }
                });
                AppController.getInstance().getRequestQueue().add(postReq);
                pdialog.show();
            } else {
                Utils.showNoNetworkDialog(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callFollowingFragment() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fragmentName", getString(R.string.following_screen_title));
        startActivity(intent);
    }

}
