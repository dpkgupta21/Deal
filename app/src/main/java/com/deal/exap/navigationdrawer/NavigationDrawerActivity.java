package com.deal.exap.navigationdrawer;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.alert.AlertFragment;
import com.deal.exap.camera.CameraChooseDialogFragment;
import com.deal.exap.camera.CameraSelectInterface;
import com.deal.exap.camera.GallerySelectInterface;
import com.deal.exap.category.CategoriesFragment;
import com.deal.exap.favorite.FavoriteFragment;
import com.deal.exap.following.FollowingFragment;
import com.deal.exap.interest.InterestFragment;
import com.deal.exap.login.EditProfileActivity;
import com.deal.exap.nearby.NearByFragment;
import com.deal.exap.settings.SettingFragment;
import com.deal.exap.wallet.WalletFragment;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.ByteArrayOutputStream;


public class NavigationDrawerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mToolBarTitle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentSelectedPosition;
    private ImageLoader image_loader;
    private ImageView img_profile;
    private CameraChooseDialogFragment dFragment;
    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;

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

        image_loader = ImageLoader.getInstance();

        DisplayImageOptions thumb_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user_profile_icon_same)
                .showImageOnFail(R.drawable.user_profile_icon_same).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888).considerExifParams(true)
                .cacheInMemory(true).displayer(new FadeInBitmapDisplayer(0))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                NavigationDrawerActivity.this)
                .defaultDisplayImageOptions(thumb_options)
                .memoryCache(new WeakMemoryCache())
                .diskCacheExtraOptions(800, 800, null).threadPoolSize(10)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(100 * 1024 * 1024))
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        image_loader.init(config);
        // mTitle = mDrawerTitle = getTitle();
        View headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        img_profile = (ImageView) headerView.findViewById(R.id.img_user_image);
        img_profile.setOnClickListener(addImageClick);

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
                //title = getString(R.string.alert_screen_title);
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


    View.OnClickListener addImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(NavigationDrawerActivity.this, EditProfileActivity.class));
        }
    };


    private void showAlertCamera() {
        try {
            if (dFragment == null) {
                dFragment = new CameraChooseDialogFragment();
            }
            dFragment.setCallBack(cameraSelectInterface, gallerySelectInterface);
            // Show DialogFragment
            FragmentManager fm = getSupportFragmentManager();
            dFragment.show(fm, "Dialog Fragment");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    CameraSelectInterface cameraSelectInterface = new CameraSelectInterface() {
        @Override
        public void startCamera() {
            clickPictureUsingCamera();
        }
    };

    GallerySelectInterface gallerySelectInterface = new GallerySelectInterface() {
        @Override
        public void startGallery() {
            selectImageFromGallery();
        }
    };


    /**
     * This method is used to click image using camera and set the clicked image
     * in round image view.
     */
    private void clickPictureUsingCamera() {
        try {
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectImageFromGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
        } catch (Exception e) {
            Log.i("info", e + "");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            byte[] hash = null;
            if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK
                    && null != data) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Uri selectedImage = data.getData();
                Log.d("DATA", data.toString());
                Log.d("uri ", selectedImage.toString());

                // setting image in image in profile pic.
                image_loader
                        .displayImage(selectedImage.toString(), img_profile);

                Bitmap bitmap = ((BitmapDrawable) img_profile.getDrawable())
                        .getBitmap();

                try {
                       bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Converting image's bitmap to byte array.
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //   bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            } else if (requestCode == CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                img_profile.setImageBitmap(photo);

                // Converting image's bitmap to byte array.
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


}
