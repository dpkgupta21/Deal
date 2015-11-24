package com.deal.exap.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.deal.exap.R;
import com.deal.exap.navigationdrawer.NavigationDrawerActivity;


public class SignUp extends Activity {

    private static final String TAG = "SignUp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        setUpToolbar();
        ((Button) findViewById(R.id.btn_signup)).setOnClickListener(signUpClick);


    }


    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        LinearLayout ll = (LinearLayout) mToolbar.findViewById(R.id.ll_title_single);
        ll.setVisibility(View.VISIBLE);
        TextView mToolBarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mToolBarTitle.setText(getString(R.string.profile_header));
    }

    View.OnClickListener signUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(SignUp.this, NavigationDrawerActivity.class);
            startActivity(i);

            // CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");

        }
    };


}
