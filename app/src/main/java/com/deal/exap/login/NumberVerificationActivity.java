package com.deal.exap.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.deal.exap.R;
import com.deal.exap.login.adapter.SplashScreenPagerAdapter;
import com.deal.exap.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberVerificationActivity extends AppCompatActivity {
    private static final String TAG = "<NumberVerificationActivity>";
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
        setContentView(R.layout.number_verification_base);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        FrameLayout frame_lay = (FrameLayout) findViewById(R.id.frame_lay);
        populateRadioButtonIds();
        populateSplashBackgrounds();

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


        NumberVerificationFragment numberVerificationFragment = NumberVerificationFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm
                .beginTransaction();
        ft.replace(R.id.frame_lay, numberVerificationFragment, "My_Fragment");
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

    private void populateSplashBackgrounds() {
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





}

