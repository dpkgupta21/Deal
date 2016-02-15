package com.deal.exap.payment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.misc.MapSupport;
import com.deal.exap.model.DealDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.partner.FollowingPartnerDetails;
import com.deal.exap.termscondition.TermsConditionActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.mobile.connect.PWConnect;
import com.mobile.connect.checkout.dialog.PWConnectCheckoutActivity;
import com.mobile.connect.checkout.meta.PWConnectCheckoutCreateToken;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;
import com.mobile.connect.checkout.meta.PWConnectCheckoutSettings;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.exception.PWProviderNotInitializedException;
import com.mobile.connect.payment.PWAccount;
import com.mobile.connect.payment.PWCurrency;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.provider.PWTransaction;
import com.mobile.connect.service.PWConnectService;
import com.mobile.connect.service.PWProviderBinder;
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
import java.util.List;
import java.util.Map;

public class BuyCouponActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "BuyCouponActivity";
    //private GoogleMap mMap;
    //private TextView txtMonth;
    //private TextView txtYear;
    //private ArrayList<String> months;
    //private ArrayList<String> years;
    private DealDTO dealDTO;
    private DisplayImageOptions options;
    //private Dialog dialog;
    private PWProviderBinder _binder;

    private static final String APPLICATIONIDENTIFIER = "gate2play.WorldofSS.mcommerce.test";// "payworks.swipeandbuy";
    private static final String PROFILETOKEN = "930e6e9744154563afc4718ab0352b9a";
    private double transactionPrice = 0.0;

    private ArrayList<String> imageList;
    private GoogleMap mMap;


    //private ProgressBar progressBar;

    /**
     * A list of the stored accounts
     */
    private List<PWAccount> accounts;

    /**
     * Reference to the preferences where the accounts are stored
     */
    private SharedPreferences sharedSettings;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _binder = (PWProviderBinder) service;
            try {
                // replace by custom sandbox access
                _binder.initializeProvider(PWConnect.PWProviderMode.TEST, APPLICATIONIDENTIFIER, PROFILETOKEN);
            } catch (PWException ee) {
                ee.printStackTrace();
            }
            Utils.ShowLog(TAG, "bound to remote service...!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            _binder = null;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coupon);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        startService(new Intent(this, PWConnectService.class));
        bindService(new Intent(this, PWConnectService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        sharedSettings = getSharedPreferences(DealPreferences.PREF_NAME, 0);

        init();

        addShareView();
    }


    @Override
    protected void onResume() {
        super.onResume();


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


        setClick(R.id.btn_buy);
        setClick(R.id.iv_back);

        setClick(R.id.txt_terms_conditions);
        setClick(R.id.txt_customer_reviews);
        setClick(R.id.thumbnail);

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
            case R.id.btn_buy:

                if (dealDTO.getType().equalsIgnoreCase("Paid")) {
                    buyFromCheckoutScreen(dealDTO.getFinal_price());
                    //openPaymentDialog(dealDTO.getFinal_price());
//                    Intent intent = new Intent(getApplicationContext(), BuyPaymentDialogActivity.class);
//                    intent.putExtra("BUY_PRICE",5.0);
//                    startActivity(intent);
                } else {
                    redeemDeal();
                }
                break;

            case R.id.img_title:
                i = new Intent(this, FollowingPartnerDetails.class);
                i.putExtra("partnerId", dealDTO.getPartner_id());
                startActivity(i);
                break;

        }
    }

//    private View.OnClickListener monthDialog = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            openMonthDialog();
//
//        }
//    };
//
//    private View.OnClickListener yearDialog = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            openYearDialog();
//        }
//    };

//    private View.OnClickListener redeemDeal = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            redeemDeal();
//        }
//    };


//    public void openMonthDialog() {
//        final Dialog dialog = new Dialog(BuyCouponActivity.this);
//        // Include dialog.xml file
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_country_code);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ListView listView = (ListView) dialog.findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, months);
//        listView.setAdapter(adapter);
//        dialog.show();
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                txtMonth.setText(months.get(position));
//                dialog.dismiss();
//            }
//        });
//
//
//    }
//
//    public void openYearDialog() {
//        final Dialog dialog = new Dialog(BuyCouponActivity.this);
//        // Include dialog.xml file
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_country_code);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ListView listView = (ListView) dialog.findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, years);
//        listView.setAdapter(adapter);
//        dialog.show();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                txtYear.setText(years.get(position));
//                dialog.dismiss();
//            }
//        });
//
//
//    }


    private void getDealDetails(String id) {


        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_DEAL_DETAIL);
            params.put("lang", Utils.getSelectedLanguage(BuyCouponActivity.this));
            params.put("lat", String.valueOf(DealPreferences.getLatitude(BuyCouponActivity.this.
                    getApplicationContext())));
            params.put("lng", String.valueOf(DealPreferences.getLongitude(this.
                    getApplicationContext())));
            params.put("user_id", Utils.getUserId(this));
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setData() {
        String dealCode = dealDTO.getDeal_code();
        setDealCode(dealCode);
//        if (dealDTO.getType().equalsIgnoreCase("paid")) {
//            setViewVisibility(R.id.btn_buy_deal, View.VISIBLE);
//            setViewVisibility(R.id.btn_redeem, View.GONE);
//
//        } else {
//            setViewVisibility(R.id.btn_buy_deal, View.GONE);
//            setViewVisibility(R.id.btn_redeem, View.VISIBLE);
//        }
        setTextViewText(R.id.txt_discount_rate, dealDTO.getDiscount() + " % " + getString(R.string.txt_off));
        if (HelpMe.isArabic(this)) {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_ara());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_ara());
        } else {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_eng());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_eng());
        }

        Button btn_purchase = (Button) findViewById(R.id.btn_buy);

        if (dealDTO.getDeal_code() != null && !dealDTO.getDeal_code().equalsIgnoreCase("")) {
            btn_purchase.setVisibility(View.GONE);
        } else {


            btn_purchase.setVisibility(View.VISIBLE);
            if (dealDTO.getType().equalsIgnoreCase("Paid")) {
                btn_purchase.setText(getString(R.string.txt_buy));
                btn_purchase.setBackgroundResource(R.drawable.btn_green_bcg_shape);
            } else {
                btn_purchase.setText(getString(R.string.btn_reedme));
                btn_purchase.setBackgroundResource(R.drawable.btn_red_bcg_shape);
            }
        }


        setTextViewText(R.id.txt_address, dealDTO.getLocation());
        ((RatingBar) findViewById(R.id.rating_bar)).setRating(dealDTO.getRating());

        setTextViewText(R.id.txt_review, dealDTO.getReview() + "");
        setTextViewText(R.id.txt_end_date_val, dealDTO.getEnd_date());
        setTextViewText(R.id.txt_redeemed_val, dealDTO.getRedeemed() + "");
        if (HelpMe.isMiles(this))
            setTextViewText(R.id.txt_distance_val, HelpMe.convertKMToMiles(dealDTO.getDistance()) + " " +
                    HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_MILES_ENG, BuyCouponActivity.this));
        else
            setTextViewText(R.id.txt_distance_val, dealDTO.getDistance() + " " +
                    HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_KM_ENG, BuyCouponActivity.this));
        setTextViewText(R.id.txt_redeem_option, dealDTO.getRedeem_option());

        if (dealDTO.getDeal_code() != null && !dealDTO.getDeal_code().equalsIgnoreCase("")) {
            setViewVisibility(R.id.ll_deal_price, View.GONE);
            setViewVisibility(R.id.ll_deal_code, View.VISIBLE);
            setTextViewText(R.id.deal_code, dealDTO.getDeal_code());

        } else {
            setViewVisibility(R.id.ll_deal_price, View.VISIBLE);
            setViewVisibility(R.id.ll_deal_code, View.GONE);
            setTextViewText(R.id.txt_discount, dealDTO.getDiscount() + "%");
            setTextViewText(R.id.txt_store_price, dealDTO.getFinal_price() + " " +
                    HelpMe.getCurrencySign(BuyCouponActivity.this));

        }


        // ImageView imgThumnail = (ImageView) findViewById(R.id.thumbnail);
        ImageView partner = (ImageView) findViewById(R.id.img_title);
        ImageLoader.getInstance().displayImage(dealDTO.getPartner_logo(), partner,
                options);

        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
        ImageLoader.getInstance().displayImage(dealDTO.getDeal_image(), thumbnail,
                options);

        // Here we create a deal image url list so we can show image slider
        if (dealDTO.getDeal_image() != null && !dealDTO.getDeal_image().equalsIgnoreCase("")) {
            imageList.add(dealDTO.getDeal_image());
        }
        if (dealDTO.getDeal_images().size() > 0) {
            for (int i = 0; i < dealDTO.getDeal_images().size(); i++) {
                imageList.add(dealDTO.getDeal_images().get(i));
            }
        }


//        ImageLoader.getInstance().displayImage(dealImageList.get(0), imgThumnail,
//                options);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    private void buyFromCheckoutScreen(String price) {

        try {
            transactionPrice = Double.parseDouble(price);
            double vatAmount = 4.5;
            Intent i = new Intent(BuyCouponActivity.this, PWConnectCheckoutActivity.class);
            PWConnectCheckoutSettings settings = null;
            PWPaymentParams genericParams = null;


            // configure amount, currency, and subject of the transaction
            genericParams = _binder.getPaymentParamsFactory().createGenericPaymentParams(transactionPrice,
                    PWCurrency.SAUDI_ARABIA_RIYAL,
                    "test subject");
            // configure payment params with customer data
//                genericParams.setCustomerGivenName("Aliza");
//                genericParams.setCustomerFamilyName("Foo");
//                genericParams.setCustomerAddressCity("Sampletown");
//                genericParams.setCustomerAddressCountryCode("US");
//                genericParams.setCustomerAddressState("PA");
//                genericParams.setCustomerAddressStreet("123 Grande St");
//                genericParams.setCustomerAddressZip("1234");
//                genericParams.setCustomerEmail("aliza.foo@foomail.com");
//                genericParams.setCustomerIP("255.0.255.0");
//                genericParams.setCustomIdentifier("myCustomIdentifier");


            // create the settings for the payment screens
            settings = new PWConnectCheckoutSettings();
            settings.setHeaderDescription("mobile.connect");
            settings.setHeaderIconResource(R.mipmap.ic_launcher);
            settings.setPaymentVATAmount(vatAmount);
            settings.setSupportedDirectDebitCountries(new String[]{"DE"});
            settings.setSupportedPaymentMethods(new PWConnectCheckoutPaymentMethod[]{
                    PWConnectCheckoutPaymentMethod.VISA, PWConnectCheckoutPaymentMethod.MASTERCARD});
            // ask the user if she wants to store the account
            settings.setCreateToken(PWConnectCheckoutCreateToken.PROMPT);

            // retrieve the stored accounts from the settings
            accounts = _binder.getAccountFactory().deserializeAccountList(sharedSettings.getString(DealPreferences.ACCOUNTS,
                    _binder.getAccountFactory().serializeAccountList(new ArrayList<PWAccount>())));
            settings.setStoredAccounts(accounts);

            i.putExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_SETTINGS, settings);
            i.putExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_GENERIC_PAYMENT_PARAMS, genericParams);
            startActivityForResult(i, PWConnectCheckoutActivity.CONNECT_CHECKOUT_ACTIVITY);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (PWException e) {
            Utils.ShowLog(TAG, "error creating the payment page");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Utils.ShowLog(TAG, "user canceled the checkout process/error");
            //updateText("Checkout cancelled or an error occurred.");
        } else if (resultCode == RESULT_OK) {

            //updateText("Thank you for shopping!");

            // if the user added a new account, store it
            if (data.hasExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_ACCOUNT)) {
                Utils.ShowLog(TAG, "checkout went through, callback has an account");
                PWTransaction transaction = data.getExtras().getParcelable(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_TRANSACTION);

                buyDeal(transaction.getProcessorUniqueIdentifier());

                ArrayList<PWAccount> newAccounts = data.getExtras().getParcelableArrayList(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_ACCOUNT);

                accounts.addAll(newAccounts);
                try {
                    sharedSettings.edit().putString(DealPreferences.ACCOUNTS,
                            _binder.getAccountFactory().serializeAccountList(accounts)).commit();
                } catch (PWProviderNotInitializedException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.ShowLog(TAG, "checkout went through, callback has transaction result");
            }
        }
    }


//    private void buyTransaction(String cardHolderName,
//                                String cardNumber,
//                                String month,
//                                String year,
//                                String cvv,
//                                double transactionPrice) {
//
//        PWPaymentParams paymentParams = null;
//        try {
//
//
//            paymentParams = _binder
//                    .getPaymentParamsFactory()
//                    .createCreditCardPaymentParams(transactionPrice,
//                            PWCurrency.EURO,
//                            "A test charge",
//                            cardHolderName,
//                            PWCreditCardType.VISA,
//                            cardNumber, year, month, cvv);
//
//        } catch (PWProviderNotInitializedException e) {
//            progressBarOnUi(View.GONE);
//            setStatusText("Error: Provider not initialized!");
//            e.printStackTrace();
//            return;
//        } catch (PWException e) {
//            progressBarOnUi(View.GONE);
//            setStatusText("Error: Invalid Parameters!");
//            e.printStackTrace();
//            return;
//        }
//
//        setStatusText("Preparing...");
//
//        try {
//            _binder.createAndRegisterDebitTransaction(paymentParams);
//        } catch (PWException e) {
//            progressBarOnUi(View.GONE);
//            setStatusText("Error: Could not contact Gateway!");
//            e.printStackTrace();
//        }
//    }
//

    private void redeemDeal() {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.READ_REDEEM);
            params.put("deal_id", dealDTO.getId());
            params.put("partner_id", dealDTO.getPartner_id() + "");
            params.put("user_id", Utils.getUserId(this));
            params.put("category_id", dealDTO.getCategory_id() + "");

            final ProgressDialog pdialog = Utils.createProgressDialog(BuyCouponActivity.this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());

                                if (Utils.getWebServiceStatus(response)) {
                                    //  finish();
                                    //callWalletFragment();

                                    String dealCode = response.getString("dealcode");
                                    setDealCode(dealCode);

                                } else {
                                    Utils.showDialog(BuyCouponActivity.this, "Message", Utils.getWebServiceMessage(response));
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
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(this);
        }


    }


    private void setDealCode(String dealCode) {
        if (dealCode != null) {
            setViewVisibility(R.id.ll_deal_price, View.GONE);
            setViewVisibility(R.id.ll_deal_code, View.VISIBLE);
            setTextViewText(R.id.deal_code, dealCode);
        }
    }


    private void callWalletFragment() {
//        unbindService(_serviceConnection);
//        stopService(new Intent(this,
//                com.mobile.connect.service.PWConnectService.class));
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fragmentName", getString(R.string.wallet_screen_title));
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);
        stopService(new Intent(this, PWConnectService.class));
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        unbindService(serviceConnection);
//        stopService(new Intent(this, PWConnectService.class));
//    }


    private void buyDeal(String transactionID) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.READ_REDEEM);
            params.put("deal_id", dealDTO.getId());
            params.put("partner_id", dealDTO.getPartner_id() + "");
            params.put("user_id", Utils.getUserId(this));
            params.put("category_id", dealDTO.getCategory_id() + "");
            params.put("redeem_amount", "" + transactionPrice);
            params.put("transaction_id", transactionID);
            final ProgressDialog pdialog = Utils.createProgressDialog(BuyCouponActivity.this, null, false);


            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());

                                if (Utils.getWebServiceStatus(response)) {
                                    String dealCode = response.getString("dealcode");
                                    setDealCode(dealCode);
                                    // finish();
                                    //callWalletFragment();

                                } else {
                                    Utils.showDialog(BuyCouponActivity.this, "Message", Utils.getWebServiceMessage(response));
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

            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(this);
        }


    }


    private void addShareView() {
        final ImageView icon = new ImageView(this);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));

        FloatingActionButton.LayoutParams params;
        final FloatingActionButton fabButton;
//        if (Utils.isArabic(BuyCouponActivity.this)) {
//            params = new FloatingActionButton.LayoutParams(50, 50);
//            params.setMargins(0, 180, 30, 0);
//            fabButton = new FloatingActionButton.Builder(this).setBackgroundDrawable(R.drawable.share_icon)
//                    .setPosition(FloatingActionButton.POSITION_TOP_LEFT)
//                    .setLayoutParams(params)
//                    .build();
//        } else {
        params = new FloatingActionButton.LayoutParams(50, 50);
        params.setMargins(0, 180, 20, 0);
        fabButton = new FloatingActionButton.Builder(this).setBackgroundDrawable(R.drawable.share_icon)
                .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
                .setLayoutParams(params)
                .build();
//        }
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
//                if (Utils.isArabic(BuyCouponActivity.this))
//                    holder = PropertyValuesHolder.ofFloat(View.ROTATION, 360);
//                else
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


//    private void updateText(final String string) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                ((TextView) findViewById(R.id.status)).setText(string);
//            }
//        });
//    }

//    private void openPaymentDialog(String price) {
//        dialog = new Dialog(BuyCouponActivity.this, R.style.Theme_Dialog);
//        // Include dialog.xml file
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_payment);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//
//        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
//        txtMonth = (TextView) dialog.findViewById(R.id.txt_month);
//        txtYear = (TextView) dialog.findViewById(R.id.txt_year);
//        transactionPrice = Double.parseDouble(price);
//        progressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
//        Button btn_pay = (Button) dialog.findViewById(R.id.btn_pay);
//
//        btn_pay.setText("Pay " + transactionPrice);
//        txtMonth.setText(months.get(0));
//        txtYear.setText(years.get(0));
//
//        txtMonth.setOnClickListener(monthDialog);
//
//        txtYear.setOnClickListener(yearDialog);
//        ivClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        btn_pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                String cardHolderName = ((EditText) dialog.findViewById(R.id.edt_card_holder_name)).
//                        getText().toString().trim();
//                String cardNumber = ((EditText) dialog.findViewById(R.id.edt_card_number)).
//                        getText().toString().trim();//"4005550000000001";
//                String cvv = ((EditText) dialog.findViewById(R.id.edt_cvv)).
//                        getText().toString().trim();//123
//                String month = txtMonth.
//                        getText().toString().trim();//05
//                String year = txtYear.
//                        getText().toString().trim();//"2017";
//                CheckBox chkRememberMe = (CheckBox) dialog.findViewById(R.id.chk_remember_me);
//                if (!cardHolderName.equalsIgnoreCase("") &&
//                        !cardNumber.equalsIgnoreCase("") &&
//                        !cvv.equalsIgnoreCase("") &&
//                        !month.equalsIgnoreCase("") &&
//                        !year.equalsIgnoreCase("")) {
//                    if (chkRememberMe.isChecked()) {
//                        DealPreferences.setCardholderName(getApplicationContext(), cardHolderName);
//                        DealPreferences.setCardNumber(getApplicationContext(), cardNumber);
//                        //DealPreferences.setCardCVV(getApplicationContext(), cvv);
//                        DealPreferences.setCardMonth(getApplicationContext(), month);
//                        DealPreferences.setCardYear(getApplicationContext(), year);
//
//                    }
//                    buyTransaction(cardHolderName, cardNumber, month, year, cvv, transactionPrice);
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
//        dialog.show();
//
//
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapSupport.createMarker(mMap, DealPreferences.getLatitude(this.
                getApplicationContext()), DealPreferences.getLongitude(this.
                getApplicationContext()), "current", this, "");
        MapSupport.createMarker(mMap, dealDTO.getLat(), dealDTO.getLng(), "position", this, dealDTO.getDiscount() + "%");

        new MapSupport().drawPath(DealPreferences.getLatitude(this.
                getApplicationContext()), DealPreferences.getLongitude(this.
                getApplicationContext()), dealDTO.getLat(), dealDTO.getLng(), mMap);
//        new MapSupport().drawPath(DealPreferences.getLatitude(this.
//                getApplicationContext()), DealPreferences.getLongitude(this.
//                getApplicationContext()), 27.221721, 77.488052, mMap);


    }


}
