package com.deal.exap.navigationdrawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
import com.deal.exap.menucount.MenuCountHandler;
import com.deal.exap.model.MenuDTO;
import com.deal.exap.model.UserDTO;
import com.deal.exap.nearby.NearByFragment;
import com.deal.exap.settings.SettingFragment;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.SessionManager;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.deal.exap.wallet.WalletFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    public ResideMenuSecond resideMenu;
    private Context mContext;
    private ResideMenuItem itemAlert, itemNearby, itemWallet,
            itemInterest, itemFavorite, itemFollowing,
            itemCategory, itemSetting;
    //Boolean isopend = false;
    View topView;
    private String fragmentName;
    private boolean isFromLogin;
    private MenuDTO menuDTO;
    private boolean backPressedToExitOnce = false;
    private final MenuHandler myHandler =
            new MenuHandler(HomeActivity.this);

    private Tracker mTracker;


    /**
     * Called when the activity is first created.
     */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        fragmentName = getIntent().getStringExtra("fragmentName");

        // Google analytics

        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();


        //mixpannal

        String projectToken = "de8b3e7f77ee97d7eb8f44f96cc45cd0"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

        // set latitude and longitude in Deal Preferences
        GPSTracker gpsTracker = new GPSTracker(HomeActivity.this);
        init();
        getMenuCount();
    }

    private void init() {
        setHeader(fragmentName);
        setLeftClick(R.drawable.menu_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + TAG);
        mTracker.setScreenName("Image~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenuSecond(this);
        resideMenu.setBackgroundColor(getResources().getColor(R.color.nav_drw_bg));
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        if (menuDTO != null) {
            // create menu items;

            itemAlert = new ResideMenuItem(this, R.drawable.nav_bell_icon, getString(R.string.menu_alert) + (menuDTO.getAlert() == 0 ? "" : " (" + menuDTO.getAlert() + ")"));

            itemNearby = new ResideMenuItem(this, R.drawable.nav_nearby_icon, getString(R.string.menu_near_by) + (menuDTO.getNearby() == 0 ? "" : " (" + menuDTO.getNearby() + ")"));
            itemWallet = new ResideMenuItem(this, R.drawable.nav_wallet_icon, getString(R.string.menu_wallet) + (menuDTO.getWallet() == 0 ? "" : " (" + menuDTO.getWallet() + ")"));
            itemInterest = new ResideMenuItem(this, R.drawable.nav_interest_icon, getString(R.string.menu_interest));
            itemFavorite = new ResideMenuItem(this, R.drawable.nav_fav_icon, getString(R.string.menu_favorite) + (menuDTO.getFavorite() == 0 ? "" : " (" + menuDTO.getFavorite() + ")"));
            itemFollowing = new ResideMenuItem(this, R.drawable.nav_following_icon, getString(R.string.menu_following) + (menuDTO.getFollowing() == 0 ? "" : " (" + menuDTO.getFollowing() + ")"));
            itemCategory = new ResideMenuItem(this, R.drawable.nav_categories_icon, getString(R.string.menu_home));
            itemSetting = new ResideMenuItem(this, R.drawable.nav_settings_icon, getString(R.string.menu_setting));
        } else {
            itemAlert = new ResideMenuItem(this, R.drawable.nav_bell_icon, getString(R.string.menu_alert));
            itemNearby = new ResideMenuItem(this, R.drawable.nav_nearby_icon, getString(R.string.menu_near_by));
            itemWallet = new ResideMenuItem(this, R.drawable.nav_wallet_icon, getString(R.string.menu_wallet));
            itemInterest = new ResideMenuItem(this, R.drawable.nav_interest_icon, getString(R.string.menu_interest));
            itemFavorite = new ResideMenuItem(this, R.drawable.nav_fav_icon, getString(R.string.menu_favorite));
            itemFollowing = new ResideMenuItem(this, R.drawable.nav_following_icon, getString(R.string.menu_following));
            itemCategory = new ResideMenuItem(this, R.drawable.nav_categories_icon, getString(R.string.menu_home));
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

            resideMenu.addMenuItem(itemCategory, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemAlert, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemNearby, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemWallet, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemInterest, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemFavorite, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemFollowing, ResideMenuSecond.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemSetting, ResideMenuSecond.DIRECTION_LEFT);
            // resideMenu.setSwipeDirectionDisable(ResideMenuSecond.DIRECTION_LEFT);

        } else if (DealPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ARABIC_CODE)) {

            resideMenu.addMenuItem(itemCategory, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemAlert, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemNearby, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemWallet, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemInterest, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemFavorite, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemFollowing, ResideMenuSecond.DIRECTION_RIGHT);
            resideMenu.addMenuItem(itemSetting, ResideMenuSecond.DIRECTION_RIGHT);

            // resideMenu.setSwipeDirectionDisable(ResideMenuSecond.DIRECTION_RIGHT);

        }


        if (fragmentName.equalsIgnoreCase(getString(R.string.wallet_screen_title))) {
            changeFragment(new WalletFragment());
        } else if (fragmentName.equalsIgnoreCase(getString(R.string.following_screen_title))) {
            changeFragment(new FollowingFragment());
        } else if (fragmentName.equalsIgnoreCase(getString(R.string.setting_screen_title))) {
            changeFragment(new SettingFragment());
        } else if (fragmentName.equalsIgnoreCase(getString(R.string.alert_screen_title))) {
            boolean isForInbox = getIntent().getBooleanExtra("isForInbox", false);
            changeFragment(AlertFragment.newInstance(isForInbox));
        } else if (fragmentName.equalsIgnoreCase(getString(R.string.nearby_screen_title))) {
            changeFragment(NearByFragment.newInstance());
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

        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {


        if (view == itemAlert) {
            resideMenu.closeMenu();
            if (Utils.getUserType(mContext).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(mContext, getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {
                changeFragment(AlertFragment.newInstance(false));
                setHeader(getString(R.string.menu_alert));
            }
        } else if (view == itemNearby) {
            resideMenu.closeMenu();
            changeFragment(new NearByFragment());
            setHeader(getString(R.string.menu_near_by));
        } else if (view == itemWallet) {
            resideMenu.closeMenu();
            if (Utils.getUserType(mContext).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(mContext, getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {
                changeFragment(new WalletFragment());
                setHeader(getString(R.string.menu_wallet));
            }
        } else if (view == itemInterest) {
            resideMenu.closeMenu();
            changeFragment(new InterestFragment());
            setHeader(getString(R.string.menu_interest));
        } else if (view == itemFavorite) {
            resideMenu.closeMenu();
            if (Utils.getUserType(mContext).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(mContext, getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {
                changeFragment(new FavoriteFragment());
                setHeader(getString(R.string.menu_favorite));
            }
        } else if (view == itemFollowing) {
            resideMenu.closeMenu();
            if (Utils.getUserType(mContext).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(mContext, getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {
                changeFragment(new FollowingFragment());
                setHeader(getString(R.string.menu_following));
            }
        } else if (view == itemCategory) {
            resideMenu.closeMenu();
            changeFragment(new CategoriesFragment());
            setHeader(getString(R.string.menu_home));
        } else if (view == itemSetting) {
            resideMenu.closeMenu();
            if (Utils.getUserType(mContext).contains(Constant.NON_REGISTER)) {
                Utils.showDialog(mContext, getString(R.string.message), getString(R.string.for_access_this_please_login), "Login", "Cancel", login);
            } else {

                changeFragment(new SettingFragment());
                setHeader(getString(R.string.menu_setting));
            }
        }

        switch (view.getId()) {
            case R.id.iv_back:
                //if (isopend == false) {
                ///  isopend = true;
                if (resideMenu.isOpened() == false) {

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

        Utils.ShowLog(TAG, "changeFragment");
        // call menu count webservice in handler
        new Thread(new MenuCountHandler(myHandler,
                HomeActivity.this)).start();

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

        if (Utils.isOnline(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.MENU_COUNT);
            params.put("user_id", Utils.getUserId(mContext));

            final ProgressDialog pdialog = Utils.createProgressDialog(mContext, null, false);
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
                    Utils.showExceptionDialog(mContext);
                    setUpMenu();
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
            Utils.showNoNetworkDialog(mContext);
        }


    }


    DialogInterface.OnClickListener login = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SessionManager.logoutUser(mContext);
            ;
        }
    };


    public static class MenuHandler extends Handler {

        public final WeakReference<HomeActivity> mActivity;

        MenuHandler(HomeActivity activity) {
            mActivity = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Utils.ShowLog(TAG, "handleMessage in MenuHandler");
            HomeActivity activity = mActivity.get();
            activity.menuDTO = ((MenuDTO) msg.obj);
            activity.changeMenuTitle();


        }
    }

    private void changeMenuTitle() {
        itemNearby.setTitle(getString(R.string.menu_near_by) + (menuDTO.getNearby() == 0 ? "" : " (" + menuDTO.getNearby() + ")"));
        itemWallet.setTitle(getString(R.string.menu_wallet) + (menuDTO.getWallet() == 0 ? "" : " (" + menuDTO.getWallet() + ")"));
        itemInterest.setTitle(getString(R.string.menu_interest));
        itemFavorite.setTitle(getString(R.string.menu_favorite) + (menuDTO.getFavorite() == 0 ? "" : " (" + menuDTO.getFavorite() + ")"));
        itemFollowing.setTitle(getString(R.string.menu_following) + (menuDTO.getFollowing() == 0 ? "" : " (" + menuDTO.getFollowing() + ")"));
        itemCategory.setTitle(getString(R.string.menu_home));
        itemSetting.setTitle(getString(R.string.menu_setting));
        itemAlert.setTitle(getString(R.string.menu_alert) + (menuDTO.getAlert() == 0 ? "" : " (" + menuDTO.getAlert() + ")"));
    }

    @Override
    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            Log.d(TAG, "closing the drawer");
//            drawerLayout.closeDrawer(Gravity.LEFT);
//        } else {
        if (backPressedToExitOnce) {
            UserDTO userDTO = DealPreferences.getObjectFromPref(mContext, Constant.USER_INFO);
            if (userDTO.getUserType().equalsIgnoreCase(Constant.NON_REGISTER)) {
                userDTO = null;
                DealPreferences.putObjectIntoPref(mContext, userDTO, Constant.USER_INFO);
                SessionManager.logoutUser(mContext);
            }


            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(HomeActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }


}
