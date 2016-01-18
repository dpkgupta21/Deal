package com.deal.exap.customerfeedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customerfeedback.adapter.CustomerFeedBackListAdapter;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.feedback.PostFeedbackActivity;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.DealDTO;
import com.deal.exap.model.ReviewDTO;
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
import java.util.List;
import java.util.Map;

public class CustomerFeedBackActivity extends BaseActivity {

    private ListView listView;
    private List<ReviewDTO> reviewList;

    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feed_back);


        id = getIntent().getStringExtra("dealId");
        getReviewList();
        init();

    }

    private void init() {
        setRightClick();
        setHeader(getString(R.string.title_custom_feedback));
        setTouchNClick(R.id.btn_post_comments);
        listView = (ListView) findViewById(R.id.list_view_feedback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_post_comments:
                startActivity(new Intent(this, PostFeedbackActivity.class));
                break;
        }
    }


    public void getReviewList() {
        if (Utils.isOnline(CustomerFeedBackActivity.this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_REVIEW);
            params.put("lang", Utils.getSelectedLanguage(CustomerFeedBackActivity.this));
            params.put("deal_id", id);
            final ProgressDialog pdialog = Utils.createProgeessDialog(CustomerFeedBackActivity.this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<ReviewDTO>>() {
                                }.getType();
                                reviewList = new Gson().fromJson(response.getJSONArray("review").toString(), type);
                                setReviewValues();
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
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(CustomerFeedBackActivity.this);
        }
    }


    private void setReviewValues() {

        listView.setAdapter(new CustomerFeedBackListAdapter(CustomerFeedBackActivity.this, reviewList));


    }


}
