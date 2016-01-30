package com.deal.exap.navigationdrawer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.alert.AlertFragment;
import com.deal.exap.category.CategoriesFragment;
import com.deal.exap.com.exap.sidemenu.ResideMenuItem;
import com.deal.exap.com.exap.sidemenu.ResideMenuSecond;
import com.deal.exap.favorite.FavoriteFragment;
import com.deal.exap.following.FollowingFragment;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.interest.InterestFragment;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.login.SplashScreen;
import com.deal.exap.model.MenuDTO;
import com.deal.exap.nearby.NearByFragment;
import com.deal.exap.settings.SettingFragment;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.deal.exap.wallet.WalletFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    public ResideMenuSecond resideMenu;
    private HomeActivity mContext;
    private ResideMenuItem itemAlert, itemNearby, itemWallet, itemInterest, itemFavorite, itemFollowing, itemCategory, itemSetting;
    //Boolean isopend = false;
    View topView;
    private String fragmentName;
    private MenuDTO menuDTO;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        fragmentName = getIntent().getStringExtra("fragmentName");

        // set latitude and longitude in Deal Preferences
        GPSTracker gpsTracker = new GPSTracker(HomeActivity.this);
        init();
        getMenuCount();
        // setUpMenu();

    }

    private void init() {

        setHeader(fragmentName);
        setLeftClick(R.drawable.menu_btn);
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenuSecond(this);
        resideMenu.setBackgroundColor(getResources().getColor(R.color.nav_drw_bg));
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        if (menuDTO != null) {
            // create menu items;
            itemAlert = new ResideMenuItem(this, R.drawable.nav_bell_icon, getString(R.string.menu_alert) + " (" + menuDTO.getAlert() + ")");
            itemNearby = new ResideMenuItem(this, R.drawable.nav_nearby_icon, getString(R.string.menu_near_by) + " (" + menuDTO.getNearby() + ")");
            itemWallet = new ResideMenuItem(this, R.drawable.nav_wallet_icon, getString(R.string.menu_wallet) + " (" + menuDTO.getWallet() + ")");
            itemInterest = new ResideMenuItem(this, R.drawable.nav_interest_icon, getString(R.string.menu_interest));
            itemFavorite = new ResideMenuItem(this, R.drawable.nav_fav_icon, getString(R.string.menu_favorite) + " (" + menuDTO.getFavorite() + ")");
            itemFollowing = new ResideMenuItem(this, R.drawable.nav_following_icon, getString(R.string.menu_following) + " (" + menuDTO.getFollowing() + ")");
            itemCategory = new ResideMenuItem(this, R.drawable.nav_categories_icon, getString(R.string.menu_categories));
            itemSetting = new ResideMenuItem(this, R.drawable.nav_settings_icon, getString(R.string.menu_setting));
        } else {
            itemAlert = new ResideMenuItem(this, R.drawable.nav_bell_icon, getString(R.string.menu_alert) + " (0)");
            itemNearby = new ResideMenuItem(this, R.drawable.nav_nearby_icon, getString(R.string.menu_near_by) + " (0)");
            itemWallet = new ResideMenuItem(this, R.drawable.nav_wallet_icon, getString(R.string.menu_wallet) + " (0)");
            itemInterest = new ResideMenuItem(this, R.drawable.nav_interest_icon, getString(R.string.menu_interest));
            itemFavorite = new ResideMenuItem(this, R.drawable.nav_fav_icon, getString(R.string.menu_favorite) + " (0)");
            itemFollowing = new ResideMenuItem(this, R.drawable.nav_following_icon, getString(R.string.menu_following) + " (0)");
            itemCategory = new ResideMenuItem(this, R.drawable.nav_categories_icon, getString(R.string.menu_categories));
            itemSetting = new ResideMenuItem(this, R.drawable.nav_settings_icon, getString(R.string.menu_setting));
        }
        itemAlert.setOnClickListener(this);
        itemNearby.setOnClickListener(this);
        itemWallet.setOnClickListener(this);
        itemInterest.setOnClickListener(this);
        itemFavorite.setOnClickListener(this);
        itemFollowing.setOnClickListener(this);
        itemCategory.setOnClickListener(this);
        itemSetting.setOnClickListener(this);

        if (DealPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ENGLISH_CODE)) {

            resideMenu.addMenuItem(itemAlert, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemNearby, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemWallet, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemInterest, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemFavorite, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemFollowing, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemCategory, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemSetting, ResideMenuSecond.DIRECTION_LEFT);
            // resideMenu.setSwipeDirectionDisable(ResideMenuSecond.DIRECTION_LEFT);

        } else if (DealPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ARABIC_CODE)) {

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


        if (fragmentName.equalsIgnoreCase(getString(R.string.wallet_screen_title))) {
            changeFragment(new WalletFragment());
        } else if (fragmentName.equalsIgnoreCase(getString(R.string.following_screen_title))) {
            changeFragment(new FollowingFragment());
        } else if (fragmentName.equalsIgnoreCase(getString(R.string.setting_screen_title))) {
            changeFragment(new SettingFragment());
        } else {
            changeFragment(new InterestFragment());
        }
/*}
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
            if (Utils.getUserType(this).contains(Constant.NON_REGISTER)) {

                Utils.showDialog(HomeActivity.this,getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);

            } else {
                changeFragment(new AlertFragment());
                setHeader(getString(R.string.alert_screen_title));
            }
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
            if (Utils.getUserType(this).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(HomeActivity.this,getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {
                changeFragment(new FavoriteFragment());
                setHeader(getString(R.string.favorite_screen_title));
            }
        } else if (view == itemFollowing) {
            if (Utils.getUserType(this).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(HomeActivity.this,getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {
                changeFragment(new FollowingFragment());
                setHeader(getString(R.string.following_screen_title));
            }
        } else if (view == itemCategory) {
            changeFragment(new CategoriesFragment());
            setHeader(getString(R.string.categories_title));
        } else if (view == itemSetting) {
            if (Utils.getUserType(this).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(HomeActivity.this,getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {

                changeFragment(new SettingFragment());
                setHeader(getString(R.string.setting_screen_title));
            }
        }

        switch (view.getId()) {
            case R.id.iv_back:
                //if (isopend == false) {
                ///  isopend = true;
                if (resideMenu.isOpened() == false) {
                    resideMenu.setOpened(true);
                    if (DealPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ENGLISH_CODE)) {
                        resideMenu.openMenu(ResideMenuSecond.DIRECTION_LEFT);
                    } else if (DealPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ARABIC_CODE)) {
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


    private void getMenuCount() {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.MENU_COUNT);
            params.put("user_id", Utils.getUserId(this));

            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                menuDTO = new Gson().fromJson(response.getJSONObject("count").toString(), MenuDTO.class);
                                setUpMenu();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(HomeActivity.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {

            setUpMenu();
            Utils.showNoNetworkDialog(this);
        }


    }


    DialogInterface.OnClickListener login = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent(HomeActivity.this, SplashScreen.class));
        }
    };


}
