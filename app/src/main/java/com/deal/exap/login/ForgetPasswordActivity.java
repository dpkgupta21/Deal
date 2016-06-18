package com.deal.exap.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.CustomAlert;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = "<ForgetPasswordActivity>";
    private Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        mActivity = ForgetPasswordActivity.this;

        setHeader(getString(R.string.app_name));
        setLeftClick();

        setClick(R.id.btn_submit);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                doForgetPassword();
                break;
        }
    }

    private void doForgetPassword() {
        Utils.hideKeyboard(mActivity);
        if (validateForm()) {
            if (Utils.isOnline(mActivity)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.FORGOT_PASSWORD);
                params.put("email", getViewText(R.id.edt_enter_email_id));
                final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Resonse -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        new CustomAlert(mActivity, mActivity)
                                                .singleButtonAlertDialog(
                                                        response.getString("message"),
                                                        getString(R.string.btn_text),
                                                        "singleBtnCallbackResponse", 1000);
                                        //Utils.showDialog(getActivity(), "Message", Utils.getWebServiceMessage(response));

                                    } else {
                                        Utils.showDialog(mActivity, "Error", Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(mActivity);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(mActivity);
            }
        }
    }


    public void singleBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            startActivity(new Intent(mActivity, SplashScreen.class));

        }

    }


    public boolean validateForm() {

        if (getViewText(R.id.edt_enter_email_id).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter email id.");
            return false;
        } else if (!Utils.isValidEmail(getViewText(R.id.edt_enter_email_id))) {
            Utils.showDialog(mActivity, "Message", "Please enter valid email id");
            return false;
        }
        return true;
    }
}

