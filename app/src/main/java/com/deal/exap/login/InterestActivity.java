package com.deal.exap.login;


import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.FrameLayout;

import com.deal.exap.R;
import com.deal.exap.navdrawer.FragmentDrawer;

public class InterestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InterestActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InterestActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mToolbar.setAlpha(1 - slideOffset / 2);
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_item_alert:

                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.nav_item_near_by:

                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.nav_item_wallet:

                        mCurrentSelectedPosition = 2;
                        return true;
                    case R.id.nav_item_interest:
                        displayView(3);
                        mCurrentSelectedPosition = 3;
                        return true;
                    case R.id.nav_item_favorite:

                        mCurrentSelectedPosition = 4;
                        return true;
                    case R.id.nav_item_following:

                        mCurrentSelectedPosition = 5;
                        return true;
                    case R.id.nav_item_categories:

                        mCurrentSelectedPosition = 6;
                        return true;
                    case R.id.nav_item_settings:

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
            case 3:
                fragment = new InterestFragment();
                title = getString(R.string.interest_screen_title);
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
            getSupportActionBar().setTitle(title);
        }
    }

}
