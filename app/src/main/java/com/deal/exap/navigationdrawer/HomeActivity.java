package com.deal.exap.navigationdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.deal.exap.R;
import com.deal.exap.alert.AlertFragment;
import com.deal.exap.category.CategoriesFragment;
import com.deal.exap.com.exap.sidemenu.ResideMenuItem;
import com.deal.exap.com.exap.sidemenu.ResideMenuSecond;
import com.deal.exap.favorite.FavoriteFragment;
import com.deal.exap.following.FollowingFragment;
import com.deal.exap.interest.InterestFragment;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.nearby.NearByFragment;
import com.deal.exap.settings.SettingFragment;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.TJPreferences;
import com.deal.exap.wallet.WalletFragment;

public class HomeActivity extends BaseActivity {

    public ResideMenuSecond resideMenu;
    private HomeActivity mContext;
    private ResideMenuItem itemAlert, itemNearby, itemWallet, itemInterest, itemFavorite, itemFollowing, itemCategory, itemSetting;
    //Boolean isopend = false;
    View topView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        init();
        setUpMenu();

    }

    private void init() {
        setHeader(getString(R.string.interest_screen_title));
        setLeftClick(R.drawable.menu_btn);
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenuSecond(this);
        resideMenu.setBackgroundColor(getResources().getColor(R.color.nav_drw_bg));
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        // create menu items;
        itemAlert = new ResideMenuItem(this, R.drawable.nav_bell_icon, getString(R.string.menu_alert));
        itemNearby = new ResideMenuItem(this, R.drawable.nav_nearby_icon, getString(R.string.menu_near_by));
        itemWallet = new ResideMenuItem(this, R.drawable.nav_wallet_icon, getString(R.string.menu_wallet));
        itemInterest = new ResideMenuItem(this, R.drawable.nav_interest_icon, getString(R.string.menu_interest));
        itemFavorite = new ResideMenuItem(this, R.drawable.nav_fav_icon, getString(R.string.menu_favorite));
        itemFollowing = new ResideMenuItem(this, R.drawable.nav_following_icon, getString(R.string.menu_following));
        itemCategory = new ResideMenuItem(this, R.drawable.nav_categories_icon, getString(R.string.menu_categories));
        itemSetting = new ResideMenuItem(this, R.drawable.nav_settings_icon, getString(R.string.menu_setting));

        itemAlert.setOnClickListener(this);
        itemNearby.setOnClickListener(this);
        itemWallet.setOnClickListener(this);
        itemInterest.setOnClickListener(this);
        itemFavorite.setOnClickListener(this);
        itemFollowing.setOnClickListener(this);
        itemCategory.setOnClickListener(this);
        itemSetting.setOnClickListener(this);

        if (TJPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ENGLISH_CODE)) {
            resideMenu.addMenuItem(itemAlert, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemNearby, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemWallet, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemInterest, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemFavorite, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemFollowing, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemCategory, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemSetting, ResideMenuSecond.DIRECTION_LEFT);

            // resideMenu.setSwipeDirectionDisable(ResideMenuSecond.DIRECTION_LEFT);

        } else if (TJPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ARABIC_CODE)) {
            resideMenu.addMenuItem(itemAlert, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemNearby, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemWallet, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemInterest, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemFavorite, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemFollowing, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemCategory, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemSetting, ResideMenuSecond.DIRECTION_RIGHT);

            // resideMenu.setSwipeDirectionDisable(ResideMenuSecond.DIRECTION_RIGHT);

        }
        changeFragment(new InterestFragment());
/*
        findViewById(R.id.title_bar_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(isopend==false)
                {
                    resideMenu.openMenu();
                    isopend=true;
                }
                else
                {
                    resideMenu.closeMenu();
                    isopend=false;
                }

            }
        });*/
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Toast.makeText(getApplicationContext(), "i m in Touch"+ev.getAction(),Toast.LENGTH_LONG).show();
        return resideMenu.onInterceptTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        resideMenu.closeMenu();

        if (view == itemAlert) {
            changeFragment(new AlertFragment());
            setHeader(getString(R.string.alert_screen_title));
        } else if (view == itemNearby) {
            changeFragment(new NearByFragment());
            setHeader(getString(R.string.nearby_screen_title));
        } else if (view == itemWallet) {
            changeFragment(new WalletFragment());
            setHeader(getString(R.string.wallet_screen_title));
        } else if (view == itemInterest) {
            changeFragment(new InterestFragment());
            setHeader(getString(R.string.interest_screen_title));
        } else if (view == itemFavorite) {
            changeFragment(new FavoriteFragment());
            setHeader(getString(R.string.favorite_screen_title));
        } else if (view == itemFollowing) {
            changeFragment(new FollowingFragment());
            setHeader(getString(R.string.following_screen_title));
        } else if (view == itemCategory) {
            changeFragment(new CategoriesFragment());
            setHeader(getString(R.string.categories_title));
        } else if (view == itemSetting) {
            changeFragment(new SettingFragment());
            setHeader(getString(R.string.setting_screen_title));
        }

        switch (view.getId()) {
            case R.id.iv_back:
                //if (isopend == false) {
                  ///  isopend = true;
                if(resideMenu.isOpened()==false){
                    resideMenu.setOpened(true);
                    if (TJPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ENGLISH_CODE)) {
                        resideMenu.openMenu(ResideMenuSecond.DIRECTION_LEFT);
                    } else if (TJPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ARABIC_CODE)) {
                        resideMenu.openMenu(ResideMenuSecond.DIRECTION_RIGHT);
                    }

                } else {
                    //isopend = false;
                    resideMenu.setOpened(false);
                    resideMenu.closeMenu();
                }
                break;
        }


    }

    private ResideMenuSecond.OnMenuListener menuListener = new ResideMenuSecond.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_layout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }



    // What good method is to access resideMenu？
    public ResideMenuSecond getResideMenu() {
        return resideMenu;
    }
}
