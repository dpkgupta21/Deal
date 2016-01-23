package com.deal.exap.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.deal.exap.R;
import com.deal.exap.databasemanager.DatabaseHelper;
import com.deal.exap.databasemanager.DatabaseManager;
import com.deal.exap.model.DealDTO;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.j256.ormlite.dao.Dao;

import java.util.Timer;
import java.util.TimerTask;

public class OrgSplashActivity extends FragmentActivity {

    private long splashDelay = 3000;
    private Context mContext;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(this);
        Dao<DealDTO, String> dealDao = null;
        try {
            dealDao = db.getDealDao();
            int i = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_org_splash);
        mContext = OrgSplashActivity.this;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                HelpMe.setLocale(DealPreferences.getAPP_LANG(mContext), mContext);

                UserDTO userDTO = DealPreferences.getObjectFromPref(OrgSplashActivity.this, Constant.USER_INFO);

                Intent i = null;
                if (userDTO == null) {
                    i = new Intent(OrgSplashActivity.this, SplashScreen.class);
                } else {
                    i = new Intent(OrgSplashActivity.this, HomeActivity.class);
                    i.putExtra("fragmentName", getString(R.string.interest_screen_title));
                }
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
