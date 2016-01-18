package com.deal.exap.nearby;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.deal.exap.R;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.model.CategoryDTO;
import com.deal.exap.model.DealDTO;
import com.deal.exap.partner.ChatActivity;
import com.deal.exap.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
//import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Calendar;

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

        if (getIntent() != null && getIntent().getExtras() != null) {
            dealDTO = (DealDTO) getIntent().getExtras().getSerializable("DealDTO");
        }
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

        ImageView imgThumnail = (ImageView) findViewById(R.id.thumbnail);
        ImageView partner = (ImageView) findViewById(R.id.img_title);

        ImageLoader.getInstance().displayImage(dealDTO.getDeal_image(), imgThumnail,
                options);
        ImageLoader.getInstance().displayImage(dealDTO.getPartner_logo(), partner,
                options);

        months = Utils.getMonths();
        years = Utils.getYears();
        setClick(R.id.btn_buy);
        setClick(R.id.iv_back);
        setClick(R.id.thumbnail);
        setClick(R.id.iv_chat);
        setClick(R.id.txt_terms_conditions);
        setClick(R.id.txt_customer_reviews);


        setTextViewText(R.id.txt_discount_rate, dealDTO.getDiscount() + "% off");
        if (Utils.isArebic(this)) {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_ara());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_ara());
        } else {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_eng());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_eng());
        }


        setTextViewText(R.id.txt_address, "");
        ((RatingBar) findViewById(R.id.rating_bar)).setRating(dealDTO.getRating());

        setTextViewText(R.id.txt_review, dealDTO.getReview() + "");
        setTextViewText(R.id.txt_end_date_val, dealDTO.getEnd_date());
        setTextViewText(R.id.txt_redeemed_val, dealDTO.getRedeemed() + "");
        setTextViewText(R.id.txt_distance_val, dealDTO.getDistance());
        setTextViewText(R.id.txt_redeem_option, dealDTO.getRedeem_option());
        setTextViewText(R.id.txt_discount, dealDTO.getDiscount() + "%");
        setTextViewText(R.id.txt_store_price, dealDTO.getFinal_price());


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
        switch (view.getId()) {
            case R.id.thumbnail:
                startActivity(new Intent(this, ImageActivity.class));
                break;
            case R.id.iv_chat:
                startActivity(new Intent(this, ChatActivity.class));
                break;
            case R.id.txt_terms_conditions:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")));
                break;
            case R.id.txt_customer_reviews:
                Intent i = new Intent(this, CustomerFeedBackActivity.class);
                i.putExtra("dealId", dealDTO.getId());
                startActivity(i);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_buy:
                openPaymentDialog();
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

}
