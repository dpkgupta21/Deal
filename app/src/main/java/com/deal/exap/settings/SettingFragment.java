package com.deal.exap.settings;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.customviews.CustomProgressDialog;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg12;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.login.EditProfileActivity;
import com.deal.exap.model.CountriesDTO;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.SessionManager;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private View view;
    private MyButtonViewSemi btn_select_english;
    private MyButtonViewSemi btn_select_arabic;
    private MyTextViewReg12 btn_select_km;
    //private MyTextViewReg12 btn_select_miles;

    //    private ArrayList<String> months;
//    private ArrayList<String> years;
//    private TextView txtMonth;
//    private TextView txtYear;
    private Switch switch_location;
    private Switch switch_push;
    private Switch switch_message;
    private Switch switch_expiry;
    private List<CountriesDTO> countryList;
    private String currencyName;
    private SeekBar seekBar;

    private UserDTO userDTO;

    private Dialog dialog;


    public SettingFragment() {
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

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        ((BaseActivity) getActivity()).resetToolbar(getString(R.string.menu_setting));
        init();

        return view;
    }

    private void init() {
//        months = Utils.getMonths();
//        years = Utils.getYears();
        setClick(R.id.tv_editprofile, view);
        userDTO = DealPreferences.getObjectFromPref(getActivity(), Constant.USER_INFO);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.setting_screen_title));

        btn_select_english = (MyButtonViewSemi) view.findViewById(R.id.btn_select_english);
        btn_select_arabic = (MyButtonViewSemi) view.findViewById(R.id.btn_select_arabic);
        btn_select_km = (MyTextViewReg12) view.findViewById(R.id.btn_select_km);
        //btn_select_miles = (MyTextViewReg12) view.findViewById(R.id.btn_select_miles);

        switch_expiry = (Switch) view.findViewById(R.id.switch_expiry);
        switch_push = (Switch) view.findViewById(R.id.switch_push);
        switch_location = (Switch) view.findViewById(R.id.switch_location);
        switch_message = (Switch) view.findViewById(R.id.switch_message);


        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);

        if (userDTO.getNotificationRange() != null && !userDTO.getNotificationRange().equalsIgnoreCase("")) {
            seekBar.setProgress(Integer.parseInt(userDTO.getNotificationRange()));
        } else {
            seekBar.setProgress(5);

        }

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);


        if (userDTO.is_location_service())
            switch_location.setChecked(true);
        else
            switch_location.setChecked(false);
        if (userDTO.is_push_alert())
            switch_push.setChecked(true);
        else
            switch_push.setChecked(false);
        if (userDTO.is_deal_expiry_alert())
            switch_expiry.setChecked(true);
        else
            switch_expiry.setChecked(false);

        if (userDTO.is_message_alert())
            switch_message.setChecked(true);
        else
            switch_message.setChecked(false);


        switch_expiry.setOnCheckedChangeListener(this);
        switch_push.setOnCheckedChangeListener(this);
        switch_location.setOnCheckedChangeListener(this);
        switch_message.setOnCheckedChangeListener(this);

        selectedButton(DealPreferences.getAPP_LANG(getActivity().getApplicationContext()));
        selectedKMButton(DealPreferences.getDistanceUnit(getActivity().getApplicationContext()));
        setViewText(R.id.btn_select_km,
                HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_KM_ENG, getActivity().getApplicationContext()),
                view);
//        setViewText(R.id.btn_select_miles,
//                HelpMe.getDistanceUnitSign(Constant.DISTANCE_UNIT_MILES_ENG, getActivity().getApplicationContext()),
//                view);
        btn_select_english.setOnClickListener(englishLanguageClick);
        btn_select_arabic.setOnClickListener(arabicLanguageClick);
        //btn_select_km.setOnClickListener(this);
        //btn_select_miles.setOnClickListener(this);


        //setClick(R.id.txt_change_currency, view);
        setClick(R.id.txt_signout, view);
        setClick(R.id.img_twitter_icon, view);
        setClick(R.id.img_instagram_icon, view);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_editprofile:
                getActivity().startActivity(new Intent(getActivity(), EditProfileActivity.class));
                break;
//            case R.id.btn_select_km:
//                DealPreferences.setDistanceUnit(getActivity().getApplicationContext(), Constant.DISTANCE_UNIT_KM_ENG);
//                selectedKMButton(Constant.DISTANCE_UNIT_KM_ENG);
//                break;
//            case R.id.btn_select_miles:
//                DealPreferences.setDistanceUnit(getActivity().getApplicationContext(), Constant.DISTANCE_UNIT_MILES_ENG);
//                selectedKMButton(Constant.DISTANCE_UNIT_MILES_ENG);
//                break;

//            case R.id.txt_change_currency:
//                getCountry();
//                break;

            case R.id.txt_signout:

                UserDTO userDTO = DealPreferences.getObjectFromPref(getActivity(), Constant.USER_INFO);
                userDTO = null;
                DealPreferences.putObjectIntoPref(getActivity(), userDTO, Constant.USER_INFO);
                SessionManager.logoutUser(getActivity().getApplicationContext());
                break;

            case R.id.img_twitter_icon:
                redirectToTwitter();
                break;

            case R.id.img_instagram_icon:
                redirectToInstagram();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    View.OnClickListener englishLanguageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (DealPreferences.getAPP_LANG(getActivity().getApplicationContext()).contains(Constant.LANG_ARABIC_CODE)) {
                HelpMe.setLocale(Constant.LANG_ENGLISH_CODE, getActivity().getApplicationContext());
                selectedButton(Constant.LANG_ENGLISH_CODE);
                DealPreferences.setAPP_LANG(getActivity().getApplicationContext(), Constant.LANG_ENGLISH_CODE);
                syncSetting("language_id", Constant.LANG_ENGLISH_CODE);


            }

        }
    };

    View.OnClickListener arabicLanguageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (DealPreferences.getAPP_LANG(getActivity().getApplicationContext()).contains(Constant.LANG_ENGLISH_CODE)) {
                HelpMe.setLocale(Constant.LANG_ARABIC_CODE, getActivity().getApplicationContext());
                selectedButton(Constant.LANG_ARABIC_CODE);
                DealPreferences.setAPP_LANG(getActivity().getApplicationContext(), Constant.LANG_ARABIC_CODE);
                syncSetting("language_id", Constant.LANG_ARABIC_CODE);

            }
        }
    };


    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }

    private void selectedButton(String STATUS_CODE) {
        if (STATUS_CODE.contains(Constant.LANG_ENGLISH_CODE)) {

            btn_select_english.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.white));

            btn_select_english.setTextColor(getResources().getColor(R.color.white));
            btn_select_arabic.setTextColor(getResources().getColor(R.color.black));

        } else {

            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btn_select_english.setBackgroundColor(getResources().getColor(R.color.white));

            btn_select_arabic.setTextColor(getResources().getColor(R.color.white));
            btn_select_english.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void selectedKMButton(String STATUS_CODE) {
        if (STATUS_CODE.contains(Constant.DISTANCE_UNIT_KM_ENG)) {

//            btn_select_km.setTextColor(getResources().getColor(R.color.btn_color));
//            btn_select_miles.setTextColor(getResources().getColor(R.color.white));

            btn_select_km.setTextColor(getResources().getColor(R.color.app_color));
            //btn_select_miles.setTextColor(getResources().getColor(R.color.btn_dark_gray_color));

        } else {
//            btn_select_miles.setBackgroundColor(getResources().getColor(R.color.btn_color));
//            btn_select_km.setBackgroundColor(getResources().getColor(R.color.white));

            //btn_select_miles.setTextColor(getResources().getColor(R.color.app_color));
            btn_select_km.setTextColor(getResources().getColor(R.color.btn_dark_gray_color));
        }
    }

    private void redirectToTwitter() {
        Uri uri = Uri.parse("https://twitter.com/exap");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void redirectToInstagram() {
        Uri uri = Uri.parse("https://www.instagram.com/exapsa/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

//
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
//
//
//    public void openMonthDialog() {
//        final Dialog dialog = new Dialog(getActivity());
//        // Include dialog.xml file
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_country_code);
//        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ListView listView = (ListView) dialog.findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, months);
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
//        final Dialog dialog = new Dialog(getActivity());
//        // Include dialog.xml file
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_country_code);
//        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ListView listView = (ListView) dialog.findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, years);
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


    private void syncSetting(final String key, final String value) {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.SETTING);
            params.put("key", key);
            params.put("value", value);
            params.put("user_id", Utils.getUserId(getActivity()));
            CustomProgressDialog.showProgDialog(getActivity(), null);
            //final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());

                                //Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_LONG).show();

                                UserDTO userDTO = DealPreferences.getObjectFromPref(getActivity(), Constant.USER_INFO);

                                if (key.equalsIgnoreCase("is_deal_expiry_alert")) {
                                    userDTO.setIs_deal_expiry_alert(value.equals("0") ? false : true);
                                } else if (key.equalsIgnoreCase("is_location_service")) {
                                    userDTO.setIs_location_service(value.equals("0") ? false : true);
                                } else if (key.equalsIgnoreCase("is_message_alert")) {
                                    userDTO.setIs_message_alert(value.equals("0") ? false : true);
                                } else if (key.equalsIgnoreCase("is_push_alert")) {
                                    userDTO.setIs_push_alert(value.equals("0") ? false : true);
                                } else if (key.equalsIgnoreCase("language_id")) {
                                    Intent i = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                                    i.putExtra("fragmentName", getActivity().getString(R.string.setting_screen_title));
                                    startActivity(i);
                                    getActivity().finish();
                                } else if (key.equalsIgnoreCase("country_id")) {
                                    userDTO.setCountry_id(value);
                                    syncSetting("currency", currencyName);
                                    return;
                                } else if (key.equalsIgnoreCase("currency")) {
                                    userDTO.setCurrency(value);
                                } else if (key.equalsIgnoreCase("notification_range")) {
                                    userDTO.setNotificationRange(value);
                                }


                                DealPreferences.putObjectIntoPref(getActivity(), userDTO, Constant.USER_INFO);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            CustomProgressDialog.hideProgressDialog();
            Utils.showNoNetworkDialog(getActivity());
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switch_expiry:
                if (isChecked) {
                    syncSetting("is_deal_expiry_alert", "1");
                } else {
                    syncSetting("is_deal_expiry_alert", "0");

                }

                break;

            case R.id.switch_location:
                if (isChecked) {
                    syncSetting("is_location_service", "1");
                } else {
                    syncSetting("is_location_service", "0");

                }

                break;
            case R.id.switch_message:

                if (isChecked) {
                    syncSetting("is_message_alert", "1");
                } else {
                    syncSetting("is_message_alert", "0");

                }
                break;
            case R.id.switch_push:
                if (isChecked) {
                    syncSetting("is_push_alert", "1");
                } else {
                    syncSetting("is_push_alert", "0");

                }
                break;
        }


    }


//    private void getCountry() {
//
//        if (Utils.isOnline(getActivity())) {
//            Map<String, String> params = new HashMap<>();
//            params.put("action", Constant.GET_COUNTRY);
//
//            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
//            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
//                                Type type = new TypeToken<ArrayList<CountriesDTO>>() {
//                                }.getType();
//                                countryList = new Gson().fromJson(response.getJSONArray("countries").toString(), type);
//                                openCurrencyDialog();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            pdialog.dismiss();
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    pdialog.dismiss();
//                    Utils.showExceptionDialog(getActivity());
//                    //       CustomProgressDialog.hideProgressDialog();
//                }
//            });
//            AppController.getInstance().getRequestQueue().add(postReq);
//            postReq.setRetryPolicy(new DefaultRetryPolicy(
//                    30000, 0,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            pdialog.show();
//        } else {
//            Utils.showNoNetworkDialog(getActivity());
//        }
//
//    }


//    private void openCurrencyDialog() {
//
//        dialog = new Dialog(getActivity());
//        // Include dialog.xml file
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_country_code);
//        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ListView listView = (ListView) dialog.findViewById(R.id.list);
//        CountryListAdapter countryListAdapter = new CountryListAdapter(getActivity(), countryList);
//
//
//        //ArrayAdapter<CountriesDTO> adapter = new ArrayAdapter<CountriesDTO>(getActivity(), android.R.layout.simple_list_item_single_choice, countryList);
//        listView.setAdapter(countryListAdapter);
//        dialog.show();
//        listView.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        dialog.dismiss();
//                        if (dialog != null) {
//                            dialog = null;
//                        }
//                        DealPreferences.setCurrencyEng(getActivity(), countryList.get(position).getName());
//                        DealPreferences.setCurrencyAra(getActivity(), countryList.get(position).getName_ara());
//
//                        syncSetting("country_id", countryList.get(position).getId());
//                        currencyName = countryList.get(position).getName();
//
//
//                    }
//                }
//        );
//    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            seekBar.setProgress(progress);
            syncSetting("notification_range", progress + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


}
