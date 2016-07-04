package com.deal.exap.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationOptionFragment extends BaseFragment {


    private static final String TAG = "VerificationOptionFragment";
    private View view;
    private Activity mActivity;

    public static VerificationOptionFragment newInstance() {
        VerificationOptionFragment fragment = new VerificationOptionFragment();
        return fragment;
    }

    public VerificationOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.verification_option, container, false);
        mActivity= getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        ((MyButtonViewSemi) view.findViewById(R.id.btn_register_from_email_address)).setOnClickListener(callToSignUp);
        ((MyButtonViewSemi) view.findViewById(R.id.btn_register_from_mob_number)).setOnClickListener(numberVerification);
        setClick(R.id.txt_sign_up_terms_use, view);
        setClick(R.id.txt_sign_up_private_policy_use, view);

    }

    View.OnClickListener callToSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity().getApplicationContext(), SignUp.class);
            startActivity(intent);

        }
    };


    View.OnClickListener numberVerification = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            verifyMobNumberDigit();


        }
    };

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.txt_sign_up_terms_use:
                Intent  termsIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.exap.sa/beta/terms-conditions?lang="
                                + Utils.getSelectedLanguage(mActivity)));
                startActivity(termsIntent);
                break;
            case R.id.txt_sign_up_private_policy_use:
                Intent privateIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.exap.sa/beta/privacy-policy?lang="
                                + Utils.getSelectedLanguage(mActivity)));
                startActivity(privateIntent);
                break;
        }
    }

    private void verifyMobNumberDigit() {
        Digits.getSessionManager().clearActiveSession();
        DigitsAuthConfig.Builder digitsAuthConfigBuilder = new DigitsAuthConfig.Builder()
                .withAuthCallBack(callback)
                .withThemeResId(R.style.CustomDigitsTheme);

        Digits.authenticate(digitsAuthConfigBuilder.build());
    }


    AuthCallback callback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
//            Toast.makeText(getActivity().getApplicationContext(),
//                    "Authentication Successful for " + phoneNumber, Toast.LENGTH_SHORT).show();
            String num = phoneNumber.substring(3, phoneNumber.length());
            doCheckMobile(num);

        }

        @Override
        public void failure(DigitsException error) {
            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    };

    private void doCheckMobile(final String phoneNumber) {
        Utils.hideKeyboard(getActivity());

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.CHECK_MOBILE);
            params.put("mobile", phoneNumber);
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Intent intent = new Intent(getContext(), SignUp.class);
                                    intent.putExtra("MOB_NUMBER", phoneNumber);
                                    startActivity(intent);

                                } else {
                                    Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
                                    startActivity(new Intent(getActivity(), SplashScreen.class));
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

