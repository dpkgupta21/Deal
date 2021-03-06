package com.deal.exap.interest;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.category.CategoriesFragment;
import com.deal.exap.customviews.MyTextViewReg16;
import com.deal.exap.databasemanager.DatabaseHelper;
import com.deal.exap.databasemanager.DatabaseManager;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.InterestDTO;
import com.deal.exap.model.UserDTO;
import com.deal.exap.payment.PaymentDetailsActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InterestFragment extends Fragment {

    private static final String TAG = "InterestFragment";
    private View view;
    private List<InterestDTO> interestValues;
    private List<String> interestValuesSelected;
    private Dao<InterestDTO, String> interestDao;
    private Dialog dialog;
    private Activity mActivity;
//    public static InterestFragment newInstance() {
//        InterestFragment fragment = new InterestFragment();
//        return fragment;
//    }


    public InterestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_interest, container, false);
        mActivity = getActivity();
        ((BaseActivity) mActivity).resetToolbar(getString(R.string.menu_interest));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();


        if (DealPreferences.isShowSurveyAfterLogin(getActivity())) {

            getSurveyForm();
        }

        getInterestList();


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }

    private void getSurveyForm() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_SURVEY_FORM);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                if (response.getBoolean("status"))
                                    openSurveyDialog(response.getString("url"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });

            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        }
    }

    private void openSurveyDialog(String url) {
        DealPreferences.setIsShowSurveyAfterLogin(getActivity(), false);

        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        // Include dialog.xml file
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_survey_webview);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        WebView webview = (WebView) dialog.findViewById(R.id.webview_survey);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), "Android");

        //webview.loadData(getHTMLData(), "text/html", "UTF-8");
        //webview.loadDataWithBaseURL(url, getHTMLData(), "text/html", "UTF-8", null);

//        WebSettings settings = webview.getSettings();
//        settings.setJavaScriptEnabled(true);
        webview.loadUrl(url + "/android");

        dialog.show();


    }


    public class MyJavaScriptInterface {
        Activity activity;

        MyJavaScriptInterface(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void showToast() {
            dialog.dismiss();
            if (dialog != null) {
                dialog = null;
            }
        }

        @JavascriptInterface
        public void closeActivity() {
            dialog.dismiss();
            if (dialog != null) {
                dialog = null;
            }
        }


    }

    View.OnClickListener interestSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyTextViewReg16 textViewReg16 = (MyTextViewReg16) v;
            int position = Integer.parseInt(textViewReg16.getTag().toString());
            if (isInterestSelected(interestValues.get(position).getId())) {
                textViewReg16.setBackgroundResource(R.drawable.txt_interest_border_unselect);
                textViewReg16.setTextColor(getResources().getColor(R.color.black));
                interestValuesSelected.remove(interestValues.get(position).getId());

            } else {
                textViewReg16.setBackgroundResource(R.drawable.txt_interest_border_select);
                textViewReg16.setTextColor(getResources().getColor(R.color.white));
                interestValuesSelected.add(interestValues.get(position).getId());

            }

        }
    };

    private void init() {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(getActivity());
        try {
            interestDao = db.getInterestDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_interest, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_check:
                addInterest();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void getInterestList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_INTEREST);
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            params.put("user_id", Utils.getUserId(mActivity));
            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<InterestDTO>>() {
                                }.getType();
                                interestValues = new Gson().fromJson(response.getJSONArray("interests").toString(), type);
                                for (InterestDTO dto : interestValues) {
                                    interestDao.create(dto);
                                }
                                if (response.has("user_interests")) {
                                    type = new TypeToken<ArrayList<String>>() {
                                    }.getType();
                                    interestValuesSelected = new Gson().fromJson(response.getJSONArray("user_interests").toString(), type);
                                }
                                setInterestList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            //Utils.showNoNetworkDialog(getActivity());
            try {
                interestValues = interestDao.queryForAll();
                setInterestList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void addInterest() {
        if (Utils.isOnline(getActivity())) {
            StringBuffer interestIds = new StringBuffer();
            for (String dto : interestValuesSelected) {
                interestIds.append(dto + ",");
            }
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.ADD_INTEREST);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("interest", interestIds.toString());

            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Utils.ShowLog(Constant.TAG, "Response -> " + response.toString());
                                    pdialog.dismiss();
                                    CategoriesFragment categoriesFragment = new CategoriesFragment();
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm
                                            .beginTransaction();
                                    ft.replace(R.id.body_layout, categoriesFragment);
                                    ft.commit();
                                } else {
                                    pdialog.dismiss();
                                    Utils.showDialog(mActivity,
                                            getString(R.string.alert_title_error),
                                            Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                pdialog.dismiss();
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
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }

    }

    public void setInterestList() {
        final FlowLayout layout = (FlowLayout) view.findViewById(R.id.flowLayout);

        if (HelpMe.isArabic(getActivity())) {

        } else {

        }
        if (interestValuesSelected == null)
            interestValuesSelected = new ArrayList<String>();

        for (int i = 0; i < interestValues.size(); i++) {
            final MyTextViewReg16 textView = new MyTextViewReg16(getActivity());
            FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT,
                    FlowLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(param);
            textView.setTextSize(16);

            textView.setGravity(Gravity.CENTER);

            textView.setText(interestValues.get(i).getName());
            textView.setTag(i);
            textView.setClickable(true);
            if (isInterestSelected(interestValues.get(i).getId())) {
                textView.setBackgroundResource(R.drawable.txt_interest_border_select);
                textView.setTextColor(getResources().getColor(R.color.white));
            } else {
                textView.setBackgroundResource(R.drawable.txt_interest_border_unselect);
                textView.setTextColor(getResources().getColor(R.color.black));
            }
            textView.setOnClickListener(interestSelectListener);
            layout.addView(textView, 0);
        }
    }

    public boolean isInterestSelected(String interest) {
        if (interestValuesSelected == null)
            return false;
        for (String dto : interestValuesSelected) {
            if (dto.equals(interest))
                return true;
        }
        return false;
    }
}
