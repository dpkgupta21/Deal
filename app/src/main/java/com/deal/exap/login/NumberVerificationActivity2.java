package com.deal.exap.login;

import android.app.Activity;
import android.os.Bundle;

import com.deal.exap.R;
import com.deal.exap.utility.TJPreferences;

/**
 * Created by deepak on 16/11/15.
 */

public class NumberVerificationActivity2 extends Activity {

    private static final String TAG = "<NumberVer2>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification_2);

        // Retrieve phone number saved in TJPreferences by SMSReceiver
        // IF not null update the same on the server
        String phoneNumber = TJPreferences.getPhone(this);
        if (phoneNumber != "" && phoneNumber != null) {
            //updateUserPhoneNumber();
        }

    }


}