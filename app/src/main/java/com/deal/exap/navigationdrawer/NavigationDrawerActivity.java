package com.deal.exap.navigationdrawer;


import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.fragment.AlertFragment;
import com.deal.exap.category.CategoriesFragment;
import com.deal.exap.favorite.FavoriteFragment;
import com.deal.exap.following.FollowingFragment;
import com.deal.exap.nearby.NearByFragment;
import com.deal.exap.fragment.WalletFragment;
import com.deal.exap.interest.InterestFragment;
import com.deal.exap.settings.SettingFragment;


public class NavigationDrawerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mToolBarTitle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_activity);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        LinearLayout ll = (LinearLayout) mToolbar.findViewById(R.id.ll_title_single);
        ll.setVisibility(View.VISIBLE);
        mToolBarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_item_alert:
                        displayView(0);
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.nav_item_near_by:
                        displayView(1);
                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.nav_item_wallet:
                        displayView(2);
                        mCurrentSelectedPosition = 2;
                        return true;
                    case R.id.nav_item_interest:
                        displayView(3);
                        mCurrentSelectedPosition = 3;
                        return true;
                    case R.id.nav_item_favorite:
                        displayView(4);
                        mCurrentSelectedPosition = 4;
                        return true;
                    case R.id.nav_item_following:
                        displayView(5);
                        mCurrentSelectedPosition = 5;
                        return true;
                    case R.id.nav_item_categories:
                        displayView(6);
                        mCurrentSelectedPosition = 6;
                        return true;
                    case R.id.nav_item_settings:
                        displayView(7);
                        mCurrentSelectedPosition = 7;
                        return true;


                    default:
                        return true;
                }

            }
        });


        displayView(3);


    }


    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {

            case 0:
                fragment = new AlertFragment();
                title = "";
                break;

            case 1:
                fragment = new NearByFragment();
                title = getString(R.string.nearby_screen_title);
                break;
            case 2:
                fragment = new WalletFragment();
                title = "";
                break;
            case 3:
                fragment = new InterestFragment();
                title = getString(R.string.interest_screen_title);
                break;
            case 4:
                fragment = new FavoriteFragment();
                title = getString(R.string.favorite_screen_title);
                break;

            case 5:
                fragment = new FollowingFragment();
                title = getString(R.string.following_screen_title);
                break;

            case 6:
                fragment = new CategoriesFragment();
                title = getString(R.string.categories_title);
                break;
            case 7:
                fragment = new SettingFragment();
                title = getString(R.string.setting_screen_title);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.body_layout, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            mToolBarTitle.setText(title);
        }
    }

}
