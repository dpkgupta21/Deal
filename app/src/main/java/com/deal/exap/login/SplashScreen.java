package com.deal.exap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.utility.SessionManager;
import com.deal.exap.utility.TJPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "<SplashScreen>";
    private SessionManager session;
    private ViewPager mViewPager;
    private SplashScreenPagerAdapter mAdapter;
    private Map<Integer, String> mPictures;
    private List<Integer> mPictureIdsList;

    private RadioGroup mSwipeIndicator;
    // List of Ids of radio buttons for displaying the dot of currently displayed picture
    private List<Integer> mRadioButtonIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        String language = TJPreferences.getAPP_LANG(SplashScreen.this);
        setUpToolbar();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        FrameLayout frame_lay = (FrameLayout) findViewById(R.id.frame_lay);
        populateRadioButtonIds();
        if (language.equalsIgnoreCase("ENG")) {
            populateEnglishSplashBackgrounds();
        } else {
            populateArabicSplashBackgrounds();
        }

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
        ft.replace(R.id.frame_lay, signInFragment, "My_Fragment");
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();
    }

    private void populateRadioButtonIds() {
        mRadioButtonIds = new ArrayList<>();
        mRadioButtonIds.add(R.id.pic1_indicator);
        mRadioButtonIds.add(R.id.pic2_indicator);
        mRadioButtonIds.add(R.id.pic3_indicator);
        mRadioButtonIds.add(R.id.pic4_indicator);
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


    private void populateArabicSplashBackgrounds() {
        mPictures = new HashMap<>();


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


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView titleExp = (TextView) toolbar.findViewById(R.id.toolbar_title_left);
        titleExp.setText(getString(R.string.title_left));
        titleExp.setOnClickListener(engLanguage);

        TextView titleArb = (TextView) toolbar.findViewById(R.id.toolbar_title_right);
        titleArb.setText(getString(R.string.title_right));
        titleArb.setOnClickListener(arbLanguage);

    }


    View.OnClickListener engLanguage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TJPreferences.setAPP_LANG(SplashScreen.this, "ENG");
            Intent i = new Intent(SplashScreen.this, SplashScreen.class);
            startActivity(i);
            finish();

        }
    };


    View.OnClickListener arbLanguage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TJPreferences.setAPP_LANG(SplashScreen.this, "ARB");
            Intent i = new Intent(SplashScreen.this, SplashScreen.class);
            startActivity(i);
            finish();

        }
    };


}

