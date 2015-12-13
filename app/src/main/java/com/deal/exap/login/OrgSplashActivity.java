package com.deal.exap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.deal.exap.R;

import java.util.Timer;
import java.util.TimerTask;

public class OrgSplashActivity extends FragmentActivity {

    private long splashDelay = 3000;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
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
