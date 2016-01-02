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
        Intent i = new Intent(getContext(), HomeActivity.class);
        startActivity(i);
        // callbackmanager.onActivityResult(requestCode, resultCode, data);
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
}

