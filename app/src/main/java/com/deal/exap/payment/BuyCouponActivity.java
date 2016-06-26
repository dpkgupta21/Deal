package com.deal.exap.payment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.deal.exap.R;
import com.deal.exap.chat.ChatActivity;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.ImageActivity;
import com.deal.exap.misc.MapSupport;
import com.deal.exap.model.DealDTO;
import com.deal.exap.partner.FollowingPartnerDetails;
import com.deal.exap.termscondition.TermsConditionActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.mobile.connect.PWConnect;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.payment.PWAccount;
import com.mobile.connect.service.PWProviderBinder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyCouponActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "BuyCouponActivity";
    private DealDTO dealDTO;
    private String orderId;  // It is custom Identifier
    private DisplayImageOptions options;
    private PWProviderBinder _binder;

    // For production
    private static final String APPLICATIONIDENTIFIER = "Hyperpay.6085WorldOfSS.mcommerce";//"gate2play.WorldofSS.mcommerce.test";
    private static final String PROFILETOKEN = "44a2f1d0f1a711e5a7dc11fc67275b56"; //"930e6e9744154563afc4718ab0352b9a";

    // For test
//    private static final String APPLICATIONIDENTIFIER = "gate2play.WorldofSS.mcommerce.test";
//    private static final String PROFILETOKEN = "930e6e9744154563afc4718ab0352b9a";
    private double transactionPrice = 0.0;

    private ArrayList<String> imageList;
    private GoogleMap mMap;
    private StringBuffer shareStr = new StringBuffer();
    private Activity mActivity;
    private String imageName;
    private String currentLocation;
    //private int topHeight=0;


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
                _binder.initializeProvider(PWConnect.PWProviderMode.LIVE, APPLICATIONIDENTIFIER, PROFILETOKEN);
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

        mActivity = BuyCouponActivity.this;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        //startService(new Intent(this, PWConnectService.class));
        //bindService(new Intent(this, PWConnectService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        sharedSettings = getSharedPreferences(DealPreferences.PREF_NAME, 0);

        init();

        addShareView();

//        final Button btn_buy=(Button)findViewById(R.id.btn_buy);
//        btn_buy.post(new Runnable() {
//            @Override
//            public void run() {
//                topHeight = btn_buy.getheTop();
//                addShareView();
//            }
//        });


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    private void init() {

        String dealId = getIntent().getStringExtra("id");

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

        // call Deal Detail Webservice
        getDealDetails(dealId);


        setClick(R.id.btn_buy);
        setClick(R.id.iv_back);

        setClick(R.id.txt_terms_conditions);
        setClick(R.id.txt_customer_reviews);
        setClick(R.id.thumbnail);

        setClick(R.id.img_title);

        setClick(R.id.txt_on_which);


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
                callDealCheckWebservice();

                break;

            case R.id.img_title:
                i = new Intent(this, FollowingPartnerDetails.class);
                i.putExtra("partnerId", dealDTO.getPartner_id());
                startActivity(i);
                break;

            case R.id.img_chat:

                i = new Intent(this, ChatActivity.class);
                i.putExtra("receiverID", dealDTO.getPartner_id() + "");
                i.putExtra("dealId", dealDTO.getId());
                startActivity(i);

                break;
            case R.id.txt_on_which:
                try {

                    if (dealDTO.getWebsite() != null && !dealDTO.equals("")) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dealDTO.getWebsite()));
                        startActivity(myIntent);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "No application can handle this request."
                            + " Please install a web browser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

        }
    }

    private void callDealCheckWebservice() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.DEAL_CHECK);
            params.put("user_id", Utils.getUserId(this));
            params.put("deal_id", dealDTO.getId());

            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                if (Utils.getWebServiceStatus(response)) {
                                    orderId = response.getString("order_id");

                                    if (dealDTO.getType().equalsIgnoreCase("Paid")) {

                                        buyPaymentDialog(dealDTO.getFinal_price());
                                        //buyFromCheckoutScreen(dealDTO.getFinal_price());
                                    } else {
                                        buyDeal(null, null);
                                    }
                                } else {
                                    Utils.showDialog(mActivity, "Error", response.getString("message"));
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


    private void getDealDetails(String dealId) {


        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_DEAL_DETAIL);
            params.put("lang", Utils.getSelectedLanguage(BuyCouponActivity.this));
            params.put("lat", String.valueOf(DealPreferences.getLatitude(BuyCouponActivity.this.
                    getApplicationContext())));
            params.put("lng", String.valueOf(DealPreferences.getLongitude(this.
                    getApplicationContext())));
            params.put("user_id", Utils.getUserId(this));
            params.put("deal_id", dealId);

            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                if (Utils.getWebServiceStatus(response)) {
                                    dealDTO = new Gson().fromJson(response.getJSONObject("deal").toString(), DealDTO.class);
                                    if (!TextUtils.isEmpty(dealDTO.getDeal_image())) {
                                        requestForImage();
                                    }
                                    setData();
                                } else {
                                    Utils.showDialog(mActivity, "Error",
                                            response.getString("message"));
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

    /**
     * this method fetches the bitmap from the given url, and save it to the internal storage.
     */
    private void requestForImage() {
        ImageRequest imageRequest = new ImageRequest(
                dealDTO.getDeal_image(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            Bitmap mBitmap = response;
                            imageName = (new SimpleDateFormat("yyyMMdd_HHmmss")
                                    .format(new Date())) + ".jpeg";
                            OutputStream outputStream = null;
                            String directory = Environment.getExternalStorageDirectory() + "/"
                                    + "Deal";
                            File path = new File(directory);
                            if (!path.exists()) {
                                path.mkdir();
                            }
                            File imageFile = new File(directory, imageName);
                            if (imageFile.exists()) {
                                imageFile.delete();
                            } else {
                                try {
                                    imageFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                outputStream = new FileOutputStream(imageFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Deal Info", "Error occured while downloading image..."
                                + error.toString());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(imageRequest);
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
        setTextViewText(R.id.txt_discount_rate, dealDTO.getDiscount() + getString(R.string.percent_off));
        if (HelpMe.isArabic(this)) {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_ara());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_ara());
        } else {
            setTextViewText(R.id.txt_on_which, dealDTO.getName_eng());
            setTextViewText(R.id.txt_details, dealDTO.getDetail_eng());
        }

        setTextViewText(R.id.txt_views_val, dealDTO.getView_count());

        Button btn_purchase = (Button) findViewById(R.id.btn_buy);

        if (dealDTO.getDeal_code() != null && !dealDTO.getDeal_code().equalsIgnoreCase("")) {
            //btn_purchase.setVisibility(View.GONE);
            btn_purchase.setVisibility(View.VISIBLE);
            btn_purchase.setEnabled(false);
            btn_purchase.setText(getString(R.string.redeemed));
            btn_purchase.setBackgroundResource(R.drawable.btn_green_bcg_shape);
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


        //setTextViewText(R.id.txt_address, dealDTO.getLocation());
        RatingBar ratingbar = (RatingBar) findViewById(R.id.rating_bar);
        if (Build.VERSION.SDK_INT >= 23) {
            LayerDrawable drawable = (LayerDrawable) ratingbar.getProgressDrawable();
            drawable.getDrawable(0).setColorFilter(Color.parseColor("#FFFFFFFF"),
                    PorterDuff.Mode.SRC_ATOP);
            ratingbar.setRating(dealDTO.getRating());
        } else {
            ratingbar.setRating(dealDTO.getRating());
        }

        String review = String.valueOf(dealDTO.getReview());
        if (!review.equalsIgnoreCase("0")) {
            setTextViewText(R.id.txt_review, "(" + review + ")");
        }

        //setTextViewText(R.id.txt_end_date_val, dealDTO.getEnd_date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date now = new Date();
        //  String currDate = sdf.format(now);

        String duration = HelpMe.getDurationTime(dealDTO.getEnd_date());
        setViewText(R.id.tv_duration_time, duration);

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


        if (dealDTO.getIs_chat_on() != null && dealDTO.getIs_chat_on().equalsIgnoreCase("1")) {
            setViewVisibility(R.id.img_chat, View.VISIBLE);

            // set chat listener
            setClick(R.id.img_chat);
        } else {
            setViewVisibility(R.id.img_chat, View.GONE);
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
        shareStr.append(dealDTO.getName_eng()).append("\n").append("Price " + dealDTO.getFinal_price()).append("\n")
                .append(dealDTO.getDiscount()).append(" % Off");

    }

    private void buyPaymentDialog(String price) {

        String merge = "";
        String dealName = "";
        String partnerName = "";
        Intent intent = new Intent(BuyCouponActivity.this, PaymentDetailsActivity.class);
        if (HelpMe.isArabic(mActivity)) {
            dealName = dealDTO.getName_ara();
            partnerName = dealDTO.getPartner_name_eng();

        } else {
            dealName = dealDTO.getName_eng();
            partnerName = (dealDTO.getPartner_name_ara() != null &&
                    !dealDTO.getPartner_name_ara().equalsIgnoreCase("")) ? dealDTO.getPartner_name_ara() :
                    dealDTO.getPartner_name_eng();
        }
        merge = dealName + "" + ((partnerName == null || partnerName.equalsIgnoreCase("")) ?
                "" : " " + getString(R.string.txt_at_the_rate_of) + " " + partnerName);
        intent.putExtra("dealName", merge);
        intent.putExtra("BUY_PRICE", price);
        startActivityForResult(intent, 10001);

    }

//    private void buyFromCheckoutScreen(String price) {
//
//        try {
//            transactionPrice = Double.parseDouble(price);
//            Intent i = new Intent(BuyCouponActivity.this, PWConnectCheckoutActivity.class);
//            PWConnectCheckoutSettings settings = null;
//            PWPaymentParams genericParams = null;
//
//
//            // configure amount, currency, and subject of the transaction
//            genericParams = _binder.getPaymentParamsFactory().createGenericPaymentParams(transactionPrice,
//                    PWCurrency.SAUDI_ARABIA_RIYAL,
//                    "test subject");
//            // configure payment params with customer data
////                genericParams.setCustomerGivenName("Aliza");
////                genericParams.setCustomerFamilyName("Foo");
////                genericParams.setCustomerAddressCity("Sampletown");
////                genericParams.setCustomerAddressCountryCode("US");
////                genericParams.setCustomerAddressState("PA");
////                genericParams.setCustomerAddressStreet("123 Grande St");
////                genericParams.setCustomerAddressZip("1234");
////                genericParams.setCustomerEmail("aliza.foo@foomail.com");
////                genericParams.setCustomerIP("255.0.255.0");
////                genericParams.setCustomIdentifier("myCustomIdentifier");
//
//            genericParams.setCustomIdentifier(orderId);
//            // create the settings for the payment screens
//            settings = new PWConnectCheckoutSettings();
//            settings.setHeaderDescription("mobile.connect");
//            settings.setHeaderIconResource(R.mipmap.ic_launcher);
//            settings.setSupportedDirectDebitCountries(new String[]{"DE"});
//            settings.setSupportedPaymentMethods(new PWConnectCheckoutPaymentMethod[]{
//                    PWConnectCheckoutPaymentMethod.VISA,
//                    PWConnectCheckoutPaymentMethod.MASTERCARD});
//            // ask the user if she wants to store the account
//            settings.setCreateToken(PWConnectCheckoutCreateToken.PROMPT);
//
//            // retrieve the stored accounts from the settings
//            accounts = _binder.getAccountFactory().deserializeAccountList(sharedSettings.getString(DealPreferences.ACCOUNTS,
//                    _binder.getAccountFactory().serializeAccountList(new ArrayList<PWAccount>())));
//            settings.setStoredAccounts(accounts);
//
//            i.putExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_SETTINGS, settings);
//            i.putExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_GENERIC_PAYMENT_PARAMS, genericParams);
//            startActivityForResult(i, PWConnectCheckoutActivity.CONNECT_CHECKOUT_ACTIVITY);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        } catch (PWException e) {
//            Utils.ShowLog(TAG, "error creating the payment page");
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String processUniqueIdentifier = data.getStringExtra("processUniqueIdentifier");
            String transactionPrice = String.valueOf(data.getDoubleExtra("transactionPrice", 0.0));
            buyDeal(processUniqueIdentifier, transactionPrice);
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    //    @SuppressWarnings("unchecked")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_CANCELED) {
//            Utils.ShowLog(TAG, "user canceled the checkout process/error");
//            Utils.showDialog(this, getString(R.string.alert_screen_title), "The request has been canceled, No transaction has been performed.");
//            //updateText("Checkout cancelled or an error occurred.");
//        } else if (resultCode == RESULT_OK) {
//
//            //updateText("Thank you for shopping!");
//
//            // if the user added a new account, store it
//            if (data.hasExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_ACCOUNT)) {
//                Utils.ShowLog(TAG, "checkout went through, callback has an account");
//                PWTransaction transaction = data.getExtras().getParcelable(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_TRANSACTION);
//
//                buyDeal(transaction.getProcessorUniqueIdentifier());
//
//                ArrayList<PWAccount> newAccounts = data.getExtras().getParcelableArrayList(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_ACCOUNT);
//
//                accounts.addAll(newAccounts);
//                try {
//                    sharedSettings.edit().putString(DealPreferences.ACCOUNTS,
//                            _binder.getAccountFactory().serializeAccountList(accounts)).commit();
//                } catch (PWProviderNotInitializedException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                PWTransaction transaction = data.getExtras().getParcelable(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_TRANSACTION);
//
//                buyDeal(transaction.getProcessorUniqueIdentifier());
//
//                Utils.ShowLog(TAG, "checkout went through, callback has transaction result");
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unbindService(serviceConnection);
//        stopService(new Intent(this, PWConnectService.class));
    }


    private void buyDeal(String transactionID, String transactionPrice) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.READ_REDEEM);
            params.put("deal_id", dealDTO.getId());
            params.put("partner_id", dealDTO.getPartner_id() + "");
            params.put("user_id", Utils.getUserId(this));
            params.put("category_id", dealDTO.getCategory_id() + "");
            params.put("order_id", orderId);
            if (transactionID != null && !transactionID.equalsIgnoreCase("")) {
                params.put("redeem_amount", "" + transactionPrice);
                params.put("transaction_id", transactionID);
            }
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

                                    Button btn_purchase = (Button) findViewById(R.id.btn_buy);

                                    if (dealDTO.getDeal_code() != null && !dealDTO.getDeal_code().equalsIgnoreCase("")) {
                                        //btn_purchase.setVisibility(View.GONE);
                                        btn_purchase.setVisibility(View.VISIBLE);
                                        btn_purchase.setEnabled(false);
                                        btn_purchase.setText(getString(R.string.redeemed));
                                        btn_purchase.setBackgroundResource(R.drawable.btn_green_bcg_shape);
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
        params = new FloatingActionButton.LayoutParams(70, 70);
        params.setMargins(0, 120, 30, 0);

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
                facebookIntent.putExtra(Intent.EXTRA_TEXT, shareStr.toString());
                startActivity(facebookIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mActivity, "Facebook app is not in your phone.", Toast.LENGTH_SHORT).show();
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
                instagramIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory() + "/" + "Deal" +
                                "/" + imageName)));
                startActivity(instagramIntent);
            } catch (Exception e) {
                Toast.makeText(mActivity, "Instagram app is not in your phone.", Toast.LENGTH_SHORT).show();
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
                if (dealDTO.getDeal_image() == null) {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, shareStr.toString());
                } else {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, dealDTO.getDeal_image() + "\n" + shareStr.toString());
                }

                startActivity(twitterIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mActivity, "Twitter app is not in your phone.", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener whatsShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setPackage("com.whatsapp");
                if (dealDTO.getDeal_image() == null) {
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareStr.toString());
                } else {
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareStr.toString());
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory() + "/" + "Deal" +
                                    "/" + imageName
                    )));
                    whatsappIntent.setType("image/*");
                }
                startActivity(whatsappIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mActivity, "Whats app is not in your phone.", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            if (mMap != null) {
                MapSupport.createMarker(mMap, DealPreferences.getLatitude(this.
                        getApplicationContext()), DealPreferences.getLongitude(this.
                        getApplicationContext()), "current", this, "");
                MapSupport.createMarker(mMap, dealDTO.getLat(), dealDTO.getLng(),
                        "position", this, dealDTO.getDiscount() + "%");

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        try {

                            if (dealDTO.getWebsite() != null && !dealDTO.equals("")) {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(dealDTO.getWebsite()));
                                startActivity(myIntent);
                            }
                        } catch (Exception e) {
                            Toast.makeText(mActivity, "No application can handle this request."
                                    + " Please install a web browser", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
                new MapSupport().drawPath(DealPreferences.getLatitude(this.
                        getApplicationContext()), DealPreferences.getLongitude(this.
                        getApplicationContext()), dealDTO.getLat(), dealDTO.getLng(), mMap);
//        new MapSupport().drawPath(DealPreferences.getLatitude(this.
//                getApplicationContext()), DealPreferences.getLongitude(this.
//                getApplicationContext()), 27.221721, 77.488052, mMap);

//        LatLngBounds latLngBounds= new LatLngBounds.Builder().build();
//        latLngBounds.including(currentLatlng);
//        latLngBounds.including(dealLatlng);
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

                retrieveAddress(DealPreferences.getLatitude(this.
                        getApplicationContext()), DealPreferences.getLongitude(this.
                        getApplicationContext()));

                LatLng currentLatlng = new LatLng(DealPreferences.getLatitude(this.
                        getApplicationContext()), DealPreferences.getLongitude(this.
                        getApplicationContext()));
                LatLng dealLatlng = new LatLng(dealDTO.getLat(), dealDTO.getLng());
                LatLngBounds settingBounds = null;
                try {
                    settingBounds = new LatLngBounds(
                            dealLatlng, currentLatlng);
                } catch (IllegalArgumentException e) {
                    settingBounds = new LatLngBounds(
                            currentLatlng, dealLatlng);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(settingBounds, 50));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {

//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse("http://maps.google.com/maps?saddr=" + DealPreferences.getLatitude(BuyCouponActivity.this) + ","
//                                        + DealPreferences.getLongitude(BuyCouponActivity.this) + "&daddr=" +
//                                        dealDTO.getLat() + "," + dealDTO.getLng() + ""));

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr='" +
                                        currentLocation + "'&daddr='" +
                                        dealDTO.getLocation() + "'"));

                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void retrieveAddress(final double lat, final double lng) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                String address = Utils.getAddress(lat, lng,
                        mActivity);
                if (address != null) {
                    currentLocation = address;
                }

            }
        };
        handler.post(r);

    }

}
