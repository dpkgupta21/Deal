package com.deal.exap.wallet;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.model.DealDTO;
import com.deal.exap.partner.FollowingPartnerDetails;
import com.deal.exap.termscondition.TermsConditionActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowWalletDetails extends BaseActivity {

    //private GoogleMap mMap;
    private DealDTO dealDTO;
    private DisplayImageOptions options;
    private ArrayList<String> imageList;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coupon);

        setViewVisibility(R.id.btn_buy, View.GONE);

        mActivity = ShowWalletDetails.this;
        GPSTracker gpsTracker = new GPSTracker(mActivity);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        addShareView();
        init();
    }


    private void init() {

        String id = getIntent().getStringExtra("id");


        imageList = new ArrayList<>();

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.default_img)
                .showImageOnFail(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img)
                .build();
        getDealDetails(id);

        setClick(R.id.iv_back);
        setClick(R.id.thumbnail);
        setClick(R.id.txt_terms_conditions);
        setClick(R.id.txt_customer_reviews);
        setClick(R.id.img_title);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/
    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.thumbnail:
                i = new Intent(this, ImageActivity.class);
                i.putExtra("images", imageList);
                startActivity(i);
                break;
//            case R.id.iv_chat:
//                startActivity(new Intent(this, ChatActivity.class));
//                break;
            case R.id.txt_terms_conditions:
                i = new Intent(this, TermsConditionActivity.class);
                if (HelpMe.isArabic(this))
                    i.putExtra("dealTerm", dealDTO.getTerm_ara());
                else
                    i.putExtra("dealTerm", dealDTO.getTerm_eng());
                startActivity(i);

                break;
            case R.id.txt_customer_reviews:
                i = new Intent(this, CustomerFeedBackActivity.class);
                i.putExtra("dealId", dealDTO.getId());
                i.putExtra("dealCode", dealDTO.getDeal_code());
                startActivity(i);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.img_title:
                i = new Intent(this, FollowingPartnerDetails.class);
                i.putExtra("partnerId", dealDTO.getPartner_id());
                startActivity(i);
                break;

        }
    }


    private void getDealDetails(String id) {


        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_DEAL_DETAIL);
            params.put("lang", Utils.getSelectedLanguage(ShowWalletDetails.this));
            params.put("lat", String.valueOf(DealPreferences.getLatitude(ShowWalletDetails.this.
                    getApplicationContext())));
            params.put("lng", String.valueOf(DealPreferences.getLongitude(this.
                    getApplicationContext())));
            params.put("user_id", Utils.getUserId(ShowWalletDetails.this));
            params.put("deal_id", id);

            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());

                                dealDTO = new Gson().fromJson(response.getJSONObject("deal").toString(), DealDTO.class);

                                setData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(ShowWalletDetails.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(this);
        }

    }


    private void setData() {
        setDealCode(dealDTO.getDeal_code());
        setTextViewText(R.id.txt_discount_rate, dealDTO.getDiscount() + " % " + getString(R.string.txt_off));
        if (HelpMe.isArabic(this)) {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_ara());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_ara());
        } else {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_eng());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_eng());
        }


        //setTextViewText(R.id.txt_address, dealDTO.getLocation());
        ((RatingBar) findViewById(R.id.rating_bar)).setRating(dealDTO.getRating());

        setTextViewText(R.id.txt_review, dealDTO.getReview() + "");
        setTextViewText(R.id.txt_end_date_val, dealDTO.getEnd_date());
        setTextViewText(R.id.txt_redeemed_val, dealDTO.getRedeemed() + "");
        if (HelpMe.isMiles(this))
            setTextViewText(R.id.txt_distance_val, HelpMe.convertKMToMiles(dealDTO.getDistance()) + " " +
                    HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_MILES_ENG, ShowWalletDetails.this));
        else
            setTextViewText(R.id.txt_distance_val, dealDTO.getDistance() + " " +
                    HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_MILES_ENG, ShowWalletDetails.this));
        setTextViewText(R.id.txt_redeem_option, dealDTO.getRedeem_option());
        setTextViewText(R.id.txt_discount, dealDTO.getDiscount() + "%");
        setTextViewText(R.id.txt_store_price, dealDTO.getFinal_price() + " " +
                HelpMe.getCurrencySign(ShowWalletDetails.this));

        ImageView partner = (ImageView) findViewById(R.id.img_title);
        ImageLoader.getInstance().displayImage(dealDTO.getPartner_logo(), partner,
                options);

        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
        ImageLoader.getInstance().displayImage(dealDTO.getDeal_image(), thumbnail,
                options);


        if (dealDTO.getDeal_image() != null && !dealDTO.getDeal_image().equalsIgnoreCase("")) {
            imageList.add(dealDTO.getDeal_image());
        }
        if (dealDTO.getDeal_images().size() > 0) {
            for (int i = 0; i < dealDTO.getDeal_images().size(); i++) {
                imageList.add(dealDTO.getDeal_images().get(i));
            }
        }


    }

    private void setDealCode(String dealCode) {
        if (dealCode != null) {
            setViewVisibility(R.id.ll_deal_price, View.GONE);
            setViewVisibility(R.id.ll_deal_code, View.VISIBLE);
            setTextViewText(R.id.deal_code, dealCode);
        }
    }

    private void addShareView() {
        final ImageView icon = new ImageView(this);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));

        FloatingActionButton.LayoutParams params = new FloatingActionButton.LayoutParams(50, 50);
        final FloatingActionButton fabButton;
        params.setMargins(0, 180, 20, 0);
        fabButton = new FloatingActionButton.Builder(this).setBackgroundDrawable(R.drawable.share_icon)
                .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
                .setLayoutParams(params)
                .build();
        SubActionButton.Builder subButton = new SubActionButton.Builder(this);
        ImageView icon1 = new ImageView(this);
        ImageView icon2 = new ImageView(this);
        ImageView icon3 = new ImageView(this);
        ImageView icon4 = new ImageView(this);

        icon1.setImageDrawable(getResources().getDrawable(R.drawable.insta_share));
        icon2.setImageDrawable(getResources().getDrawable(R.drawable.fb_share));
        icon3.setImageDrawable(getResources().getDrawable(R.drawable.tt_share));
        icon4.setImageDrawable(getResources().getDrawable(R.drawable.whatsup_share));
        SubActionButton button1 = subButton.setContentView(icon1).build();
        button1.setTag(1001);
        button1.setOnClickListener(instaShare);
        SubActionButton button2 = subButton.setContentView(icon2).build();
        button2.setTag(1002);
        button2.setOnClickListener(fbShare);
        SubActionButton button3 = subButton.setContentView(icon3).build();
        button3.setTag(3);
        button3.setOnClickListener(ttShare);
        SubActionButton button4 = subButton.setContentView(icon4).build();
        button4.setTag(4);
        button4.setOnClickListener(whatsShare);

        final FloatingActionMenu fabMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4).setRadius(120)
                .attachTo(fabButton).build();

        fabMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                icon.setRotation(0);
                PropertyValuesHolder holder;
                holder = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(icon, holder);
                animator.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                icon.setRotation(45);
                PropertyValuesHolder holder = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(icon, holder);
                animation.start();
            }
        });
    }


    View.OnClickListener fbShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent facebookIntent = new Intent(Intent.ACTION_SEND);
                facebookIntent.setType("text/plain");
                facebookIntent.setPackage("com.facebook.katana");
                facebookIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(facebookIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    View.OnClickListener instaShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent instagramIntent = new Intent(Intent.ACTION_SEND);
                instagramIntent.setType("image/*");
                instagramIntent.setPackage("com.instagram.android");
                instagramIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(instagramIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener ttShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent twitterIntent = new Intent(Intent.ACTION_SEND);
                twitterIntent.setType("text/plain");
                twitterIntent.setPackage("com.twitter.android");
                twitterIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(twitterIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    View.OnClickListener whatsShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(whatsappIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}