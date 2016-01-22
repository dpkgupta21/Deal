package com.deal.exap.nearby;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;
import com.deal.exap.following.FollowingPartnerDetails;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.model.DealDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.termscondition.TermsConditionActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
//import com.google.android.gms.maps.GoogleMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyCouponActivity extends BaseActivity {

    //private GoogleMap mMap;
    private TextView txtMonth;
    private TextView txtYear;
    private ArrayList<String> months;
    private ArrayList<String> years;
    private DealDTO dealDTO;
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coupon);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        init();
    }


    private void init() {

        String id = getIntent().getStringExtra("id");
        getDealDetails(id);


        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();


        months = Utils.getMonths();
        years = Utils.getYears();
        setClick(R.id.btn_buy);
        setClick(R.id.iv_back);
        setClick(R.id.thumbnail);
        //  setClick(R.id.iv_chat);
        setClick(R.id.txt_terms_conditions);
        setClick(R.id.txt_customer_reviews);


        setClick(R.id.img_title);


    }

    private void openPaymentDialog() {
        final Dialog dialog = new Dialog(BuyCouponActivity.this, R.style.Theme_Dialog);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        txtMonth = (TextView) dialog.findViewById(R.id.txt_month);
        txtYear = (TextView) dialog.findViewById(R.id.txt_year);
        Button btnBuy = (Button) dialog.findViewById(R.id.btn_pay);

        txtMonth.setText(months.get(0));
        txtYear.setText(years.get(0));

        txtMonth.setOnClickListener(monthDialog);

        txtYear.setOnClickListener(yearDialog);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnBuy.setOnClickListener(buyDeal);
        dialog.show();
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
                i.putExtra("image", dealDTO.getDeal_image());
                startActivity(i);
                break;
//            case R.id.iv_chat:
//                startActivity(new Intent(this, ChatActivity.class));
//                break;
            case R.id.txt_terms_conditions:
                i = new Intent(this, TermsConditionActivity.class);
                if (Utils.isArebic(this))
                    i.putExtra("dealTerm", dealDTO.getTerm_ara());
                else
                    i.putExtra("dealTerm", dealDTO.getTerm_eng());
                startActivity(i);

                break;
            case R.id.txt_customer_reviews:
                i = new Intent(this, CustomerFeedBackActivity.class);
                i.putExtra("dealId", dealDTO.getId());
                startActivity(i);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_buy:
                openPaymentDialog();
                break;

            case R.id.btn_redeem:
                readRedeeme();
                break;
            case R.id.img_title:
                i = new Intent(this, FollowingPartnerDetails.class);
                i.putExtra("partnerId", dealDTO.getPartner_id());
                startActivity(i);
                break;

        }
    }

    private View.OnClickListener monthDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openMonthDialog();

        }
    };

    private View.OnClickListener yearDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openYearDialog();
        }
    };

    private View.OnClickListener buyDeal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            readRedeeme();
        }
    };


    public void openMonthDialog() {
        final Dialog dialog = new Dialog(BuyCouponActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, months);
        listView.setAdapter(adapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtMonth.setText(months.get(position));
                dialog.dismiss();
            }
        });


    }

    public void openYearDialog() {
        final Dialog dialog = new Dialog(BuyCouponActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, years);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtYear.setText(years.get(position));
                dialog.dismiss();
            }
        });


    }


    private void getDealDetails(String id) {


        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_DEAL_DETAIL);
            params.put("lang", Utils.getSelectedLanguage(BuyCouponActivity.this));
            params.put("lat", String.valueOf(DealPreferences.getLatitude(BuyCouponActivity.this.
                    getApplicationContext())));
            params.put("lng", String.valueOf(DealPreferences.getLongitude(this.
                    getApplicationContext())));

            params.put("deal_id", id);

            final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
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
                    Utils.showExceptionDialog(BuyCouponActivity.this);
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

//        if (dealDTO.getType().equalsIgnoreCase("paid")) {
//            setViewVisibility(R.id.btn_buy_deal, View.VISIBLE);
//            setViewVisibility(R.id.btn_redeem, View.GONE);
//
//        } else {
//            setViewVisibility(R.id.btn_buy_deal, View.GONE);
//            setViewVisibility(R.id.btn_redeem, View.VISIBLE);
//        }
        setTextViewText(R.id.txt_discount_rate, dealDTO.getDiscount() + "% off");
        if (Utils.isArebic(this)) {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_ara());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_ara());
        } else {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_eng());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_eng());
        }


        setTextViewText(R.id.txt_address, dealDTO.getLocation());
        ((RatingBar) findViewById(R.id.rating_bar)).setRating(dealDTO.getRating());

        setTextViewText(R.id.txt_review, dealDTO.getReview() + "");
        setTextViewText(R.id.txt_end_date_val, dealDTO.getEnd_date());
        setTextViewText(R.id.txt_redeemed_val, dealDTO.getRedeemed() + "");
        setTextViewText(R.id.txt_distance_val, dealDTO.getDistance());
        setTextViewText(R.id.txt_redeem_option, dealDTO.getRedeem_option());
        setTextViewText(R.id.txt_discount, dealDTO.getDiscount() + "%");
        setTextViewText(R.id.txt_store_price, dealDTO.getFinal_price());


        ImageView imgThumnail = (ImageView) findViewById(R.id.thumbnail);
        ImageView partner = (ImageView) findViewById(R.id.img_title);

        ImageLoader.getInstance().displayImage(dealDTO.getDeal_image(), imgThumnail,
                options);
        ImageLoader.getInstance().displayImage(dealDTO.getPartner_logo(), partner,
                options);

    }


    private void readRedeeme() {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.READ_REDEEM);
            params.put("deal_id", dealDTO.getId());
            params.put("partner_id", dealDTO.getPartner_id() + "");
            params.put("user_id", Utils.getUserId(this));
            params.put("category_id", dealDTO.getCategory_id() + "");

            if (dealDTO.getType().equalsIgnoreCase("paid")) {
                params.put("redeem_amount", "");
                params.put("transaction_id", "");
            }
            final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());

                                if (Utils.getWebServiceStatus(response)) {
                                    finish();
                                    callWalletFragment();

                                } else {
                                    Utils.showDialog(BuyCouponActivity.this, "Error", Utils.getWebServiceMessage(response));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(BuyCouponActivity.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(this);
        }


    }


    private void callWalletFragment() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fragmentName", getString(R.string.wallet_screen_title));
        startActivity(intent);
    }
}
