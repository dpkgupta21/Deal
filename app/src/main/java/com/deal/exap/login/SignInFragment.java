package com.deal.exap.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg12;
import com.deal.exap.customviews.MyTextViewRegCustom;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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

    TwitterLoginButton btnTwitterLogin;
    TwitterSession session;

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
        ((MyTextViewReg12) view.findViewById(R.id.txt_sign_up_click)).
                setOnClickListener(goToNumberVerificationClick);
        ((MyButtonViewSemi) view.findViewById(R.id.btn_login)).
                setOnClickListener(goToHomePage);
        ((MyTextViewRegCustom) view.findViewById(R.id.btn_facebook_login)).setOnClickListener(goToFacebookLogin);


        super.onActivityCreated(savedInstanceState);


    }

    private void init(){
        btnTwitterLogin = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        setClick(R.id.btn_twitter_login, view);
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
    }

    void getEmailidFromTwitter() {

        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Log.d("",""+result.data);
                doSocialLogin("twitter", result.data, session.getId()+"");
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                doSocialLogin("twitter", "coolmack999@gmail.com", session.getId()+"");
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


    View.OnClickListener goToForgetPasswordPage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ForgetPasswordFragment forgetPasswordFragment = ForgetPasswordFragment.newInstance();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_lay, forgetPasswordFragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    };

    View.OnClickListener goToHomePage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent i = new Intent(getContext(), HomeActivity.class);
//            startActivity(i);
            doLogin();

        }
    };

    View.OnClickListener goToFacebookLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onFblogin();

        }
    };

    // Private method to handle Facebook login and callback
    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
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
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");
                                                Toast.makeText(getActivity(),
                                                        "Frist Name : " + str_firstname,
                                                        Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                        ).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Info", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Info", error.toString());
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent i = new Intent(getContext(), HomeActivity.class);
       // startActivity(i);
        if(requestCode == 64206){
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }
        else {
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
        if(validateForm() ) {
            if (Utils.isOnline(getActivity())) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.DO_LOGIN);
                params.put("email", getViewText(R.id.edt_username, view));
                params.put("password", getViewText(R.id.edt_password, view));
                params.put("device", "android");
                params.put("device_id", "ABC");
                final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Resonse -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                        Utils.putObjectIntoPref(getActivity(), userDTO, Constant.USER_INFO);
                                        startActivity(new Intent(getActivity(), HomeActivity.class));
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
            } else {
                Utils.showNoNetworkDialog(getActivity());
            }
        }
    }

    public void doSocialLogin(String socialType, String email, String socialId) {
        Utils.hideKeyboard(getActivity());

            if (Utils.isOnline(getActivity())) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.DO_SOCIAL_LOGIN);
                params.put("email", email);
                params.put("social_id", socialId);
                params.put("device", "android");
                params.put("device_id", "ABC");
                params.put("social_type", socialType);
                final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Resonse -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                        Utils.putObjectIntoPref(getActivity(), userDTO, Constant.USER_INFO);
                                        startActivity(new Intent(getActivity(), HomeActivity.class));
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
            } else {
                Utils.showNoNetworkDialog(getActivity());
            }

    }

    public boolean validateForm(){

        if(getViewText(R.id.edt_username, view).equals("")){
            Utils.showDialog(getActivity(), "Message", "Please enter username");
            return false;
        }

        else if(getViewText(R.id.edt_password, view).equals("")){
            Utils.showDialog(getActivity(), "Message", "Please enter password");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.btn_twitter_login:
                btnTwitterLogin.performClick();
                break;
        }
    }
}

