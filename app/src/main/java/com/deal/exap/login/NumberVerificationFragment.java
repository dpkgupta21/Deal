package com.deal.exap.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyEditTextViewReg;
import com.deal.exap.customviews.MyTextViewReg14;
import com.deal.exap.login.Adapter.CountryCodeAdapter;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */

public class NumberVerificationFragment extends Fragment {


    private static final String TAG = "NumberVerificationFragment";
    private View view;
    private MyTextViewReg14 edtCountryCode;
    //private String mobNumber;
    private Dialog dialogCountryCode;
    private List<Map<String, String>> countryCodeList;

    private String phoneNum;
    private String countryNumber;

    public static NumberVerificationFragment newInstance() {
        NumberVerificationFragment fragment = new NumberVerificationFragment();
        return fragment;
    }

    public NumberVerificationFragment() {
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
        view = inflater.inflate(R.layout.number_verification_1, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DigitsAuthButton digitsButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
        digitsButton.setCallback(callback);
        Digits.authenticate(callback, "+919460940608");
        init();
    }

    private void init() {
        countryCodeList = getCountryCode();
        ((MyButtonViewSemi) view.findViewById(R.id.btn_send_code)).setOnClickListener(goToNumberVerificationStage2);
        edtCountryCode = (MyTextViewReg14) view.findViewById(R.id.edt_contry_code);
        edtCountryCode.setText(getCountryCodeByCountryName("Saudi Arabia"));
        edtCountryCode.setOnClickListener(openDialogForCountry);
    }




    View.OnClickListener goToNumberVerificationStage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            countryNumber = edtCountryCode.getText().toString().trim();
            //((MyEditTextViewReg) view.findViewById(R.id.edt_phone_number)).getText().toString().trim();

            phoneNum = ((MyEditTextViewReg) view.findViewById(R.id.edt_phone_number)).getText().toString().trim();

            doCheckMobile(countryNumber, phoneNum);


//            NumberVerificationFragment2 numberVerificationFragment2 = NumberVerificationFragment2.newInstance();
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm
//                    .beginTransaction();
//            ft.replace(R.id.frame_lay, numberVerificationFragment2);
//            ft.addToBackStack(null);
//            ft.commit();


        }
    };


    View.OnClickListener openDialogForCountry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogCountryCode = new Dialog(getActivity());
            dialogCountryCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCountryCode.setContentView(R.layout.layout_country_code);
            getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ListView listView = (ListView) dialogCountryCode.findViewById(R.id.list);
            CountryCodeAdapter adapter = new CountryCodeAdapter(getActivity(), countryCodeList);
            listView.setAdapter(adapter);
            dialogCountryCode.show();
            listView.setOnItemClickListener(dialogItemClickListener);
        }
    };

    private List<Map<String, String>> getCountryCode() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(Utils.loadJSONFromAsset(getActivity()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Map<String, String> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                map.put("dial_code", jsonObject.getString("dial_code"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    AdapterView.OnItemClickListener dialogItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            edtCountryCode.setText(countryCodeList.get(i).get("dial_code"));
            dialogCountryCode.dismiss();
        }
    };


    private String getCountryCodeByCountryName(String countryName) {
        for (int i = 0; i < countryCodeList.size(); i++) {
            if (countryName.equalsIgnoreCase(countryCodeList.get(i).get("name"))) {
                return countryCodeList.get(i).get("dial_code");
            }

        }
        return "";
    }


    private void doCheckMobile(final String countNumber, final String phoneNumber) {
        Utils.hideKeyboard(getActivity());
        if (validateForm(phoneNumber)) {
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
                                        verifyMobNumberDigit(countNumber + phoneNumber);

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

    public boolean validateForm(String number) {
        if (number.equals("")) {
            Utils.showDialog(getActivity(), getResources().getString(R.string.message),
                    getResources().getString(R.string.please_enter_mobile_number));
            return false;
        }

        return true;
    }

    private void verifyMobNumberDigit(String phoneNumber) {
        Digits.getSessionManager().clearActiveSession();
        DigitsAuthConfig.Builder digitsAuthConfigBuilder = new DigitsAuthConfig.Builder()
                .withAuthCallBack(callback)
                .withPhoneNumber(phoneNumber)
                .withThemeResId(R.style.CustomDigitsTheme);

        Digits.authenticate(digitsAuthConfigBuilder.build());
    }


    AuthCallback callback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
//            Toast.makeText(getActivity().getApplicationContext(),
//                    "Authentication Successful for " + phoneNumber, Toast.LENGTH_SHORT).show();


        }

        @Override
        public void failure(DigitsException error) {
            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    };
}

