package com.deal.exap.settings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.com.exap.sidemenu.ResideMenuSecond;
import com.deal.exap.com.exap.sidemenu.TouchDisableView;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg16;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.login.EditProfileActivity;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.HelpMe;
import com.deal.exap.utility.TJPreferences;
import com.deal.exap.utility.Utils;

import java.util.ArrayList;


public class SettingFragment extends BaseFragment implements GestureDetector.OnGestureListener,
        SeekBar.OnSeekBarChangeListener, View.OnTouchListener{

    private View view;
    private MyButtonViewSemi btn_select_english;
    private MyButtonViewSemi btn_select_arabic;
    private ArrayList<String> months;
    private ArrayList<String> years;
    private TextView txtMonth;
    private TextView txtYear;


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

    private void init() {
        months = Utils.getMonths();
        years = Utils.getYears();
        setClick(R.id.tv_editprofile, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.setting_screen_title));
        ((MyTextViewReg16) view.findViewById(R.id.txt_payment_details)).setOnClickListener(paymentClick);
        btn_select_english = (MyButtonViewSemi) view.findViewById(R.id.btn_select_english);
        btn_select_arabic = (MyButtonViewSemi) view.findViewById(R.id.btn_select_arabic);
        SeekBar seek_bar=(SeekBar)view.findViewById(R.id.seek_bar);
        seek_bar.setOnSeekBarChangeListener(this);
        seek_bar.setOnTouchListener(this);
        selectedButton(TJPreferences.getAPP_LANG(getActivity().getApplicationContext()));

        btn_select_english.setOnClickListener(englishLanguageClick);
        btn_select_arabic.setOnClickListener(arabicLanguageClick);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
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
            if (TJPreferences.getAPP_LANG(getActivity().getApplicationContext()).contains(Constant.LANG_ARABIC_CODE)) {
                HelpMe.setLocale(Constant.LANG_ENGLISH_CODE, getActivity().getApplicationContext());
                selectedButton(Constant.LANG_ENGLISH_CODE);
                TJPreferences.setAPP_LANG(getActivity().getApplicationContext(), Constant.LANG_ENGLISH_CODE);

                Intent i = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            }

        }
    };

    View.OnClickListener arabicLanguageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TJPreferences.getAPP_LANG(getActivity().getApplicationContext()).contains(Constant.LANG_ENGLISH_CODE)) {
                HelpMe.setLocale(Constant.LANG_ARABIC_CODE, getActivity().getApplicationContext());
                selectedButton(Constant.LANG_ARABIC_CODE);
                TJPreferences.setAPP_LANG(getActivity().getApplicationContext(), Constant.LANG_ARABIC_CODE);

                Intent i = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            }
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

            txtMonth = (TextView) dialog.findViewById(R.id.txt_month);
            txtYear = (TextView) dialog.findViewById(R.id.txt_year);

            txtMonth.setText(months.get(0));
            txtYear.setText(years.get(0));

            txtMonth.setOnClickListener(monthDialog);

            txtYear.setOnClickListener(yearDialog);


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

    private void selectedButton(String STATUS_CODE){
        if (STATUS_CODE.contains(Constant.LANG_ENGLISH_CODE)) {

            btn_select_english.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.white));

            btn_select_english.setTextColor(getResources().getColor(R.color.white));
            btn_select_arabic.setTextColor(getResources().getColor(R.color.black));

        }else {

            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.btn_color));
            btn_select_english.setBackgroundColor(getResources().getColor(R.color.white));

            btn_select_arabic.setTextColor(getResources().getColor(R.color.white));
            btn_select_english.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE){

        }
        return true;
    }


    private View.OnClickListener monthDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openMonthDialog();

        }
    };

    private View.OnClickListener yearDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openYearDialog();
        }
    };


    public void openMonthDialog() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, months);
        listView.setAdapter(adapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtMonth.setText(months.get(position));
                dialog.dismiss();
            }
        });


    }

    public void openYearDialog() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
       getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, years);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtYear.setText(years.get(position));
                dialog.dismiss();
            }
        });


    }


}
