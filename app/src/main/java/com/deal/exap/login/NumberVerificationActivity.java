package com.deal.exap.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewRegCustom;
import com.deal.exap.utility.SessionManager;

public class NumberVerificationActivity extends AppCompatActivity {
    private static final String TAG = "<NumberVerificationActivity>";
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_verification_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.app_color));
        setSupportActionBar(toolbar);
        MyTextViewRegCustom tv_skip_arb = (MyTextViewRegCustom) toolbar.findViewById(R.id.tv_skip_arb);
        tv_skip_arb.setVisibility(View.GONE);
        MyTextViewRegCustom tv_skip_eng = (MyTextViewRegCustom) toolbar.findViewById(R.id.tv_skip_eng);
        tv_skip_eng.setVisibility(View.GONE);

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


}

