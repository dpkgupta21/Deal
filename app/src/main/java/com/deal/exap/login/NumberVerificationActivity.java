package com.deal.exap.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewRegCustom;
import com.deal.exap.utility.SessionManager;

public class NumberVerificationActivity extends BaseActivity {
    private static final String TAG = "<NumberVerificationActivity>";
    private SessionManager session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_verification_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.app_color));
        setSupportActionBar(toolbar);

        setHeader(getString(R.string.app_name));
        setLeftClick();

        //FrameLayout frame_lay = (FrameLayout) findViewById(R.id.frame_lay);
        //NumberVerificationFragment verificationOptionFragment = NumberVerificationFragment.newInstance();

        VerificationOptionFragment verificationOptionFragment = VerificationOptionFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm
                .beginTransaction();
        ft.replace(R.id.frame_lay, verificationOptionFragment, "My_Fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

