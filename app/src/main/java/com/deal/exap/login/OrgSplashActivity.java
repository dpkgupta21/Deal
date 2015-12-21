package com.deal.exap.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.deal.exap.R;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.TJPreferences;

import java.util.Timer;
import java.util.TimerTask;

public class OrgSplashActivity extends FragmentActivity {

    private long splashDelay = 3000;
    private Context mContext;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_splash);
        mContext = OrgSplashActivity.this;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                HelpMe.setLocale(TJPreferences.getAPP_LANG(mContext), mContext);

                Intent i = null;
                i = new Intent(OrgSplashActivity.this, SplashScreen.class);
                finish();
                startActivity(i);
            }
        };
        timer = new Timer();
        timer.schedule(task, splashDelay);
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }

}
