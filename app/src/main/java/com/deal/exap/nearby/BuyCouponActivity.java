package com.deal.exap.nearby;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.partner.ChatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class BuyCouponActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txtMonth;
    private TextView txtYear;
    private ArrayList<String> months;
    private ArrayList<String> years;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coupon);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView txt_customer_reviews = (TextView) findViewById(R.id.txt_customer_reviews);
        txt_customer_reviews.setOnClickListener(customerReviewClickListener);


        init();
    }


    private void init() {

        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnBuy = (Button) findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaymentDialog();
            }
        });

        months = new ArrayList<String>();
        months.add("MM");
        months.add("01");
        months.add("02");
        months.add("03");
        months.add("04");
        months.add("05");
        months.add("06");
        months.add("07");
        months.add("08");
        months.add("09");
        months.add("10");
        months.add("11");
        months.add("12");


        setClick(R.id.thumbnail);
        setClick(R.id.iv_chat);
        setClick(R.id.txt_terms_conditions);
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

        years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("YYYY");
        for (int i = thisYear; i <= 2050; i++) {
            years.add(Integer.toString(i));
        }

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
            Intent postFeedbackIntent = new Intent(BuyCouponActivity.this, CustomerFeedBackActivity.class);
            startActivity(postFeedbackIntent);

        }
    };

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
