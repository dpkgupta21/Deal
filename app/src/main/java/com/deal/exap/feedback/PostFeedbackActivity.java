package com.deal.exap.feedback;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.DealDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostFeedbackActivity extends BaseActivity implements View.OnClickListener{


    private RatingBar ratingBar;
    DealDTO dealDTO;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        init();
    }

    private void init(){

        setTouchNClick(R.id.btn_cancel);
        setTouchNClick(R.id.btn_post_comments);

        setHeader(getString(R.string.post_your_feedback_header));
        setRightClick();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        if(getIntent()!=null && getIntent().getExtras()!=null){
            dealDTO = (DealDTO) getIntent().getExtras().getSerializable("dealDTO");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_post_comments:
                postFeedback();
                break;
        }
    }

    public void postFeedback() {
        Utils.hideKeyboard(this);
        if(validateForm()) {
            if (Utils.isOnline(this)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.POST_REVIEW);
                params.put("lang", Utils.getSelectedLanguage(this));
                params.put("deal_id", "1");
                params.put("user_id", Utils.getUserId(this));
                params.put("review", getViewText(R.id.et_comment));
                params.put("rating", ""+(int)ratingBar.getRating());
                final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constant.TAG, "Resonse -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        Utils.showDialog(PostFeedbackActivity.this, "Message", Utils.getWebServiceMessage(response), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                    } else {
                                        Utils.showDialog(PostFeedbackActivity.this, "Error", Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(PostFeedbackActivity.this);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(PostFeedbackActivity.this);
            }
        }
    }

    public boolean validateForm(){

        if(getViewText(R.id.et_comment).equals("")){
            Utils.showDialog(this, "Message", "Please enter comments");
            return false;
        }
        return true;
    }
}
