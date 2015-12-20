package com.deal.exap.settings;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg16;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.login.EditProfileActivity;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.TJPreferences;

import java.util.Locale;


public class SettingFragment extends BaseFragment {

    private View view;
    private MyButtonViewSemi btn_select_english;
    private MyButtonViewSemi btn_select_arabic;


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

        init();

        return view;
    }

    private void init(){
        setClick(R.id.tv_editprofile, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.setting_screen_title));
        ((MyTextViewReg16) view.findViewById(R.id.txt_payment_details)).setOnClickListener(paymentClick);
        btn_select_english=(MyButtonViewSemi) view.findViewById(R.id.btn_select_english);
        btn_select_arabic=(MyButtonViewSemi) view.findViewById(R.id.btn_select_arabic);

        btn_select_english .setOnClickListener(englishLanguageClick);
        btn_select_arabic.setOnClickListener(arabicLanguageClick);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.tv_editprofile:
                getActivity().startActivity(new Intent(getActivity(), EditProfileActivity.class));
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
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());

            btn_select_english.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.white));

            btn_select_english.setTextColor(getResources().getColor(R.color.white));
            btn_select_arabic.setTextColor(getResources().getColor(R.color.black));

            TJPreferences.setAPP_LANG(getActivity().getApplicationContext(), "en");

            Intent i = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
            startActivity(i);
            getActivity().finish();

        }
    };

    View.OnClickListener arabicLanguageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());


            TJPreferences.setAPP_LANG(getActivity().getApplicationContext(), "ar");

            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btn_select_english.setBackgroundColor(getResources().getColor(R.color.white));

            btn_select_arabic.setTextColor(getResources().getColor(R.color.white));
            btn_select_english.setTextColor(getResources().getColor(R.color.black));

            Intent i = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
            startActivity(i);
            getActivity().finish();
        }
    };


    View.OnClickListener paymentClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");

            final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_payment_details);
            getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
    };


    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }

}
