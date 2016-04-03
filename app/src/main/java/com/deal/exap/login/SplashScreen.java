package com.deal.exap.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.CustomProgressDialog;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.login.Adapter.SplashScreenPagerAdapter;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.SessionManager;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreen extends BaseActivity {
    private static final String TAG = "<SplashScreen>";
    private SessionManager session;
    private ViewPager mViewPager;
    private SplashScreenPagerAdapter mAdapter;
    private Map<Integer, String> mPictures;
    private List<Integer> mPictureIdsList;
    private RadioGroup mSwipeIndicator;
    // List of Ids of radio buttons for displaying the dot of currently displayed picture
    private List<Integer> mRadioButtonIds;
    private GPSTracker gpsTracker;
    private Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mActivity = SplashScreen.this;

        //String language = DealPreferences.getAPP_LANG(SplashScreen.this);
        // setUpToolbar();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        FrameLayout frame_lay = (FrameLayout) findViewById(R.id.frame_lay);
        populateRadioButtonIds();
        populateEnglishSplashBackgrounds();

        setClick(R.id.tv_lang_change);
        setClick(R.id.tv_skip_eng);

        mAdapter = new SplashScreenPagerAdapter(this, mPictures, mPictureIdsList);
        mViewPager.setAdapter(mAdapter);

        mSwipeIndicator = (RadioGroup) findViewById(R.id.swipe_indicator_radio_group);
        mSwipeIndicator.check(mRadioButtonIds.get(0));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSwipeIndicator.clearCheck();
                mSwipeIndicator.check(mRadioButtonIds.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        SignInFragment signInFragment = SignInFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm
                .beginTransaction();
        ft.replace(R.id.frame_lay, signInFragment, "signFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();

        gpsTracker = new GPSTracker(this);
        CustomProgressDialog.hideProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = getSupportFragmentManager().findFragmentByTag("signFragment");
        if (f != null) {
            f.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void populateRadioButtonIds() {
        mRadioButtonIds = new ArrayList<>();
        mRadioButtonIds.add(R.id.pic1_indicator);
        mRadioButtonIds.add(R.id.pic2_indicator);
        mRadioButtonIds.add(R.id.pic3_indicator);
        mRadioButtonIds.add(R.id.pic4_indicator);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lang_change:
                CustomProgressDialog.showProgDialog(mActivity,null);
                if (DealPreferences.getAPP_LANG(mActivity).contains(Constant.LANG_ARABIC_CODE)) {
                    HelpMe.setLocale(Constant.LANG_ENGLISH_CODE, mActivity.getApplicationContext());
                    DealPreferences.setAPP_LANG(mActivity, Constant.LANG_ENGLISH_CODE);

                } else if (DealPreferences.getAPP_LANG(mActivity).contains(Constant.LANG_ENGLISH_CODE)) {
                    HelpMe.setLocale(Constant.LANG_ARABIC_CODE, mActivity.getApplicationContext());
                    DealPreferences.setAPP_LANG(mActivity, Constant.LANG_ARABIC_CODE);
                }

                startActivity(new Intent(mActivity, SplashScreen.class));
                finish();
                break;
            case R.id.tv_skip_eng:
                skipRegistration();
                // startActivity(new Intent(this, HomeActivity.class));
                break;

        }
    }

    private void populateEnglishSplashBackgrounds() {
        mPictures = new HashMap<>();

//        List<String> mPictureTextList = new ArrayList<>();
//        mPictureTextList.add("Capture all type of memories. Not just photos. Travel Jar can take anything");
//        mPictureTextList.add("Share those wonderful moments in the form of a small video created AUTOMATICALLY!.");
//        mPictureTextList.add("No need to chase people for pictures anymore. Travel Jar automatically syncs everything");
//        mPictureTextList.add("No need to chase people for pictures anymore. Travel Jar automatically syncs everything");

        mPictureIdsList = new ArrayList<>();
        mPictureIdsList.add(R.drawable.slide_img);
        mPictureIdsList.add(R.drawable.slide_img);
        mPictureIdsList.add(R.drawable.slide_img);
        mPictureIdsList.add(R.drawable.slide_img);

        int i = 0;
        for (Integer a : mPictureIdsList) {
            mPictures.put(a, "");
            i++;
        }
    }

    /*private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        LinearLayout ll = (LinearLayout) toolbar.findViewById(R.id.ll_title_twice);
        ll.setVisibility(View.VISIBLE);
        TextView titleExp = (TextView) toolbar.findViewById(R.id.toolbar_title_left);
        titleExp.setText(getString(R.string.title_left));
        titleExp.setOnClickListener(engLanguage);

        TextView titleArb = (TextView) toolbar.findViewById(R.id.toolbar_title_right);
        titleArb.setText(getString(R.string.title_right));
        titleArb.setOnClickListener(arbLanguage);

    }*/


    View.OnClickListener engLanguage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            DealPreferences.setAPP_LANG(SplashScreen.this, "en");
            Intent i = new Intent(SplashScreen.this, SplashScreen.class);
            startActivity(i);
            finish();

        }
    };


    View.OnClickListener arbLanguage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());


            DealPreferences.setAPP_LANG(SplashScreen.this, "ar");
            Intent i = new Intent(SplashScreen.this, SplashScreen.class);
            startActivity(i);
            finish();

        }
    };


    private void skipRegistration() {
        Utils.hideKeyboard(SplashScreen.this);
        if (Utils.isOnline(SplashScreen.this)) {

            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.SKIP_REGISTRATION);
            params.put("device", "android");
            params.put("device_id", DealPreferences.getPushRegistrationId(SplashScreen.this));
            params.put("lat", "" + gpsTracker.getLatitude());
            params.put("lng", "" + gpsTracker.getLongitude());

            final ProgressDialog pdialog = Utils.createProgressDialog(SplashScreen.this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                    userDTO.setUserType(Constant.NON_REGISTER);
                                    DealPreferences.putObjectIntoPref(SplashScreen.this, userDTO, Constant.USER_INFO);
                                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                    intent.putExtra("fragmentName", getString(R.string.interest_screen_title));
                                    startActivity(intent);
                                } else {
                                    Utils.showDialog(SplashScreen.this, "Error", Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(SplashScreen.this);
                }
            });
            pdialog.show();
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }


}

