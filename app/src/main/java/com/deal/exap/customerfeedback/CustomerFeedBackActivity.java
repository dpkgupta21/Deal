package com.deal.exap.customerfeedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customerfeedback.adapter.CustomerFeedBackListAdapter;
import com.deal.exap.feedback.PostFeedbackActivity;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.ReviewDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.android.gms.appdatasearch.Feature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerFeedBackActivity extends BaseActivity {

    private ListView listView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int REQUEST_CODE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feed_back);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_black_transparent_50);

        listView = (ListView) findViewById(R.id.list_view_feedback);
        String dealCode = getIntent().getStringExtra("dealCode");

        if (dealCode != null && !dealCode.equalsIgnoreCase("")) {
            setViewVisibility(R.id.btn_post_comments, View.VISIBLE);
        } else {
            setViewVisibility(R.id.btn_post_comments, View.GONE);

        }
        init();
        getReviewList();


        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getReviewList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void init() {
        setRightClick();
        setHeader(getString(R.string.title_custom_feedback));
        setTouchNClick(R.id.btn_post_comments);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_post_comments:

                Intent intent = new Intent(this, PostFeedbackActivity.class);
                intent.putExtra("dealId", getIntent().getStringExtra("dealId"));
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getReviewList();

    }

    public void getReviewList() {
        try {
            if (Utils.isOnline(CustomerFeedBackActivity.this)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.GET_REVIEW);
                params.put("lang", Utils.getSelectedLanguage(CustomerFeedBackActivity.this));
                params.put("deal_id", getIntent().getStringExtra("dealId"));
                params.put("user_id", Utils.getUserId(CustomerFeedBackActivity.this));

                final ProgressDialog pdialog = Utils.createProgressDialog(CustomerFeedBackActivity.this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {
                                        listView.setVisibility(View.VISIBLE);
                                        Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                        Type type = new TypeToken<ArrayList<ReviewDTO>>() {
                                        }.getType();
                                        List<ReviewDTO> reviewList = new Gson().
                                                fromJson(response.getJSONArray("review").toString(), type);
                                        setReviewValues(reviewList);
                                    } else {
                                        listView.setVisibility(View.GONE);
                                        String msg = response.getString("message");
                                        TextView txt_blank = (TextView) findViewById(R.id.txt_blank);
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
                        Utils.showExceptionDialog(CustomerFeedBackActivity.this);
                        //       CustomProgressDialog.hideProgressDialog();
                    }
                });
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                pdialog.show();
            } else {
                Utils.showNoNetworkDialog(CustomerFeedBackActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setReviewValues(List<ReviewDTO> reviewList) {

        listView.setAdapter(new CustomerFeedBackListAdapter(CustomerFeedBackActivity.this, reviewList));


    }


}
