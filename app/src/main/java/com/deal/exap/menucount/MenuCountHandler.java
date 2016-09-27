package com.deal.exap.menucount;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.model.MenuDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by deepak.gupta on 07-09-2015.
 */
public class MenuCountHandler implements Runnable {

    private static final String TAG = "MenuCountHandler";
    private Handler handler;
    private Activity mActivity;

    public MenuCountHandler(Handler handler, Activity mActivity) {
        this.handler = handler;
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        getMenuCount();
    }

    private void getMenuCount() {

        if (Utils.isOnline(mActivity)) {
            GPSTracker gpsTracker = new GPSTracker(mActivity, false);
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.MENU_COUNT);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("lat", gpsTracker.getLatitude() + "");
            params.put("lng", gpsTracker.getLongitude() + "");


            //CustomProgressDialog.showProgDialog(mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //CustomProgressDialog.hideProgressDialog();
                                Utils.ShowLog(Constant.TAG, "got Menu count response = " + response.toString());
                                MenuDTO menuDTO;
                                menuDTO = new Gson().fromJson(response.getJSONObject("count").toString(), MenuDTO.class);
                                //setUpMenu();
                                handleMenuCountResponse(menuDTO);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // CustomProgressDialog.hideProgressDialog();
                    //Utils.showExceptionDialog(mActivity);
                    //setUpMenu();
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } else {

        }


    }

    private void handleMenuCountResponse(MenuDTO menuDTO) {
        Utils.ShowLog(TAG, "handleMenuCountResponse");
        Message msg = handler.obtainMessage(Constant.MENU_COUNT_HANDLER, menuDTO);
        handler.sendMessage(msg);

    }

//
}
