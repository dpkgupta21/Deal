package com.deal.exap.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.deal.exap.R;
import com.deal.exap.misc.CustomAlertDialog;
import com.deal.exap.utility.TJPreferences;

public class SignUp extends Activity {

    private static final String TAG = "SignUp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // setUpToolbar();

        ((Button) findViewById(R.id.btn_signup)).setOnClickListener(signUpClick);


    }


//    private void setUpToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        title.setText(getString(R.string.profile_header));
//    }

    View.OnClickListener signUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent i = new Intent(SignUp.this, NumberVerificationActivity.class);
//            startActivity(i);

            CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");

        }
    };


}
