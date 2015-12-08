package com.deal.exap.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.feedback.PostFeedbackActivity;
import com.deal.exap.termscondition.TermsConditionActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CouponDetailsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView txt_customer_reviews = (TextView) findViewById(R.id.txt_customer_reviews);
        txt_customer_reviews.setOnClickListener(customerReviewClickListener);
        ((Button) findViewById(R.id.btn_rate)).setOnClickListener(gotoRateCoupon);
        ((Button) findViewById(R.id.btn_buy)).setOnClickListener(gotoBuyCoupon);

        ((TextView) findViewById(R.id.txt_terms_conditions)).setOnClickListener(this);
        init();
    }


    private void init(){

        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private View.OnClickListener customerReviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent postFeedbackIntent = new Intent(CouponDetailsActivity.this, PostFeedbackActivity.class);
            startActivity(postFeedbackIntent);

        }
    };


    View.OnClickListener gotoRateCoupon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Intent i = new Intent(CouponDetailsActivity.this, RateCouponActivity.class);
            startActivity(i);*/

            Intent postFeedbackIntent = new Intent(CouponDetailsActivity.this, PostFeedbackActivity.class);
            startActivity(postFeedbackIntent);
            // CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");

        }
    };


    View.OnClickListener gotoBuyCoupon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(CouponDetailsActivity.this, BuyCouponActivity.class);
            startActivity(i);

            // CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_terms_conditions:
                startActivity(new Intent(this, TermsConditionActivity.class));
                break;
        }
    }
}
