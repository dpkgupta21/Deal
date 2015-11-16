package com.deal.exap.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.exap.R;
import com.deal.exap.utility.HelpMe;

/**
 * Created by deepak on 16/11/15.
 */


public class NumberVerificationActivity extends Activity {

    private static final String TAG = "<NumberVer1>";
    EditText countryCode;
    EditText phoneNumber;
    private BroadcastReceiver sendBroadcastReceiver;
    private BroadcastReceiver deliveryBroadcastReceiver;
    private TextView timerCountDownTxt;
    private TextView verifyingtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification_1);

        countryCode = (EditText) findViewById(R.id.number_ver1_country_code);
        phoneNumber = (EditText) findViewById(R.id.number_ver1_phone_number);

    }

    public void verifyPhone(View v) {
        Log.d(TAG, "verifying phone number now with phone number = " + phoneNumber);

        if (!HelpMe.isValidMobile(phoneNumber.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please enter a valid mobile number of 10 digits", Toast.LENGTH_LONG)
                    .show();
            return;
        }

       // sendSMS(phoneNumber.getText().toString().trim());


    }

    private void startTimer(int millisec) {
        CountDownTimer counter = new CountDownTimer(millisec, 1000) {

            public void onTick(long millisUntilFinished) {
                timerCountDownTxt.setText((millisUntilFinished / 1000) + " sec");
            }

            public void onFinish() {
                timerCountDownTxt.setText("0 secs");
                verifyingtxt.setText("Sorry, please try again");
            }
        }.start();
    }


//    private void sendSMS(String phoneNumber) {
//        String url = Constants.URL_VERIFY_NUMBER + "?api_key=" + TJPreferences.getApiKey(this)
//                + "&user_id=" + TJPreferences.getUserId(this)
//                + "&phone=" + phoneNumber;
//        Log.d(TAG, url);
//
//        CustomJsonRequest jsonObjReq = new CustomJsonRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String otpNumber = response.getString("otp");
////                            Toast.makeText(getApplicationContext(),
////                                    "OTP :" + otpNumber, Toast.LENGTH_LONG)
////                                    .show();
//                            TJPreferences.setUSER_OTP(NumberVerificationActivity.this, otpNumber);
//                            // Start a 60 sec timer on the UI
//                            verifyingtxt = (TextView) findViewById(R.id.number_ver1_verifying_text);
//                            timerCountDownTxt = (TextView) findViewById(R.id.number_ver1_countdown_timer);
//                            Button verifyBtn = (Button) findViewById(R.id.number_ver1_bt_next);
//
//                            verifyBtn.setVisibility(View.INVISIBLE);
//                            verifyingtxt.setVisibility(View.VISIBLE);
//                            timerCountDownTxt.setVisibility(View.VISIBLE);
//
//                            startTimer(60000);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),
//                        "Error while verifying !", Toast.LENGTH_LONG)
//                        .show();
//            }
//        });
//
//        if (HelpMe.isNetworkAvailable(NumberVerificationActivity.this)) {
//            AppController.getInstance().getRequestQueue().add(jsonObjReq);
//        } else {
//            Toast.makeText(NumberVerificationActivity.this, "Network unavailable please turn on your data", Toast.LENGTH_SHORT).show();
//        }
//
//    }




    @Override
    protected void onStop() {
        try {
            unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReceiver);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "exception in unregistering receiver" + ex);
        }
        super.onStop();
    }

}