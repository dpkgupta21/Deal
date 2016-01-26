package com.deal.exap.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg12;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */

public class SignInFragment extends BaseFragment {
    private static final String TAG = "SignInFragment";
    private View view;
    private CallbackManager callbackmanager;
    private static final String TWITTER_KEY = "0WzgEZ838raQlA7BPASXLgsub";
    private static final String TWITTER_SECRET = "szOdlqn9obH0MEMaGnz2dTMMQXIdcbSQvtDcT7YkOjyALQKuEF";
    private GPSTracker gpsTracker;
    TwitterLoginButton btnTwitterLogin;
    TwitterSession session;

    LoginButton btnFbLogin;


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sign_in, container, false);


        init();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        gpsTracker = new GPSTracker(getActivity());
        ((MyTextViewReg12) view.findViewById(R.id.txt_sign_up_click)).
                setOnClickListener(goToNumberVerificationClick);
        ((MyButtonViewSemi) view.findViewById(R.id.btn_login)).
                setOnClickListener(goToHomePage);
        //((MyTextViewRegCustom) view.findViewById(R.id.btn_facebook_login)).setOnClickListener(goToFacebookLogin);


        super.onActivityCreated(savedInstanceState);


    }

    private void init() {

        btnTwitterLogin = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        setClick(R.id.btn_twitter_login, view);
        setClick(R.id.btn_facebook_login, view);
        setClick(R.id.txt_forgot_password, view);
        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                session = result.data;

                String username = session.getUserName();
                Long userid = session.getUserId();
                getEmailidFromTwitter();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        btnFbLogin = (LoginButton) view.findViewById(R.id.btnFb);
        setFbClick();
    }

    void getEmailidFromTwitter() {

        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Log.d("", "" + result.data);
                doSocialLogin("twitter", result.data, session.getId() + "");
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                doSocialLogin("twitter", "coolmack999@gmail.com", session.getId() + "");
            }
        });

/*
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {

                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        String twitterImage = user.profileImageUrl;

                        try {
                            Log.d("imageurl", user.profileImageUrl);
                            Log.d("name", user.name);
                            //Log.d("email",user.email);
                            Log.d("des", user.description);
                            Log.d("followers ", String.valueOf(user.followersCount));
                            Log.d("createdAt", user.createdAt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });
*/


    }

    View.OnClickListener goToNumberVerificationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NumberVerificationFragment numberVerificationFragment = NumberVerificationFragment.newInstance();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_lay, numberVerificationFragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    };


    private void forgotpassword() {
        ForgetPasswordFragment forgetPasswordFragment = ForgetPasswordFragment.newInstance();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm
                .beginTransaction();
        ft.replace(R.id.frame_lay, forgetPasswordFragment);
        ft.addToBackStack(null);
        ft.commit();

    }


    View.OnClickListener goToHomePage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent i = new Intent(getContext(), HomeActivity.class);
//            startActivity(i);
            while (gpsTracker.canGetLocation()) {
                doLogin();
                break;
            }

        }
    };


    private void setFbClick() {
        callbackmanager = CallbackManager.Factory.create();

        //fbLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        btnFbLogin.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println("Success");
                GraphRequest req = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json,
                                                    GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Log.i("info", "onCompleted Error.");
                                } else {
                                    System.out.println("Success");
                                    String jsonresult = String.valueOf(json);
                                    try {
                                        doSocialLogin("facebook", json.getString("email"), json.getString("id"));
                                    } catch (Exception e) {
                                        Utils.sendEmail(getActivity(), "Error", e.getMessage());
                                    }
                                }
                            }
                        }

                );
                Bundle param = new Bundle();
                param.putString("fields", "id,name,email,gender,birthday,first_name,last_name,link");
                req.setParameters(param);
                req.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("", "");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent i = new Intent(getContext(), HomeActivity.class);
        // startActivity(i);
        if (requestCode == 64206) {
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        } else {
            //
            btnTwitterLogin.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }


    public void doLogin() {
        Utils.hideKeyboard(getActivity());
        if (validateForm()) {
            if (Utils.isOnline(getActivity())) {
                String android_id = Secure.getString(getContext().getContentResolver(),
                        Secure.ANDROID_ID);
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.DO_LOGIN);
                params.put("email", getViewText(R.id.edt_username, view));
                params.put("password", getViewText(R.id.edt_password, view));
                params.put("device", "android");
                params.put("device_id", android_id);
                params.put("lat", "" + gpsTracker.getLatitude());
                params.put("lng", "" + gpsTracker.getLongitude());
                params.put("address", "");

                final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                        DealPreferences.setUserId(getActivity().getApplicationContext(), userDTO.getId());
                                        DealPreferences.putObjectIntoPref(getActivity(), userDTO, Constant.USER_INFO);
                                        DealPreferences.setUserType(getActivity().getApplicationContext(), Constant.REGISTER);
                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        intent.putExtra("fragmentName", getActivity().getString(R.string.interest_screen_title));
                                        startActivity(intent);
                                    } else {
                                        Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(getActivity());
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(getActivity());
            }
        }
    }

    public void doSocialLogin(String socialType, String email, String socialId) {
        Utils.hideKeyboard(getActivity());

        if (Utils.isOnline(getActivity())) {
            String android_id = Secure.getString(getContext().getContentResolver(),
                    Secure.ANDROID_ID);

            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.DO_SOCIAL_LOGIN);
            params.put("email", email);
            params.put("social_id", socialId);
            params.put("device", "android");
            params.put("device_id", android_id);
            params.put("social_type", socialType);
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                    DealPreferences.putObjectIntoPref(getActivity(), userDTO, Constant.USER_INFO);
                                    //startActivity(new Intent(getActivity(), HomeActivity.class));
                                    DealPreferences.setUserType(getActivity().getApplicationContext(), Constant.REGISTER);
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.putExtra("fragmentName", getActivity().getString(R.string.interest_screen_title));

                                } else {
                                    Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(getActivity());
                }
            });
            pdialog.show();
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }

    }

    public boolean validateForm() {

        if (getViewText(R.id.edt_username, view).equals("")) {
            Utils.showDialog(getActivity(), "Message", "Please enter username");
            return false;
        } else if (getViewText(R.id.edt_password, view).equals("")) {
            Utils.showDialog(getActivity(), "Message", "Please enter password");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_twitter_login:
                btnTwitterLogin.performClick();
                break;
            case R.id.btn_facebook_login:
                btnFbLogin.performClick();
                break;
            case R.id.txt_forgot_password:
                forgotpassword();
                break;
        }
    }
}

