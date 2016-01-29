package com.deal.exap.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */

public class ForgetPasswordFragment extends BaseFragment {

    private View view;
    private static final String TAG = "ForgetPasswordFragment";

    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        return fragment;
    }

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.forget_password, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();


    }

    public  void init()
    {
        setClick(R.id.btn_submit, view);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId())
       {
           case R.id.btn_submit:
               doForgetPassword();
               break;
       }
    }

   private void  doForgetPassword()
   {
       Utils.hideKeyboard(getActivity());
       if(validateForm() ) {
           if (Utils.isOnline(getActivity())) {
               Map<String, String> params = new HashMap<>();
               params.put("action", Constant.FORGOT_PASSWORD);
               params.put("email", getViewText(R.id.edt_enter_email_id, view));
               final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
               CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                       new Response.Listener<JSONObject>() {
                           @Override
                           public void onResponse(JSONObject response) {
                               Utils.ShowLog(TAG, "Resonse -> " + response.toString());
                               pdialog.dismiss();
                               try {
                                   if (Utils.getWebServiceStatus(response)) {
                                       Utils.showDialog(getActivity(), "Message", Utils.getWebServiceMessage(response));
                                       startActivity(new Intent(getActivity(), SplashScreen.class));

                                   } else {
                                       Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
                                   }
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }

                           }
                       }, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                       pdialog.dismiss();
                       Utils.showExceptionDialog(getActivity());
                   }
               });
               pdialog.show();
               AppController.getInstance().getRequestQueue().add(postReq);
               postReq.setRetryPolicy(new DefaultRetryPolicy(
                       30000, 0,
                       DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    } else {
               Utils.showNoNetworkDialog(getActivity());
           }
       }
   }


    public boolean validateForm() {

        if (getViewText(R.id.edt_enter_email_id, view).equals("")) {
            Utils.showDialog(getActivity(), "Message", "Please enter email id.");
            return false;
        }
        else if (!Utils.isValidEmail(getViewText(R.id.edt_enter_email_id,view))) {
            Utils.showDialog(getActivity(), "Message", "Please enter valid email id");
            return false;
        }
        return true;
    }
}

