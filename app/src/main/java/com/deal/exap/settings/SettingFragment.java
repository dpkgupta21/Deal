package com.deal.exap.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deal.exap.R;
import com.deal.exap.navigationdrawer.NavigationDrawerActivity;


public class SettingFragment extends Fragment {

    private View view;

//    public static SettingFragment newInstance() {
//        SettingFragment fragment = new SettingFragment();
//
//        return fragment;
//    }

    public SettingFragment() {
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

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((com.deal.exap.customviews.MyTextViewReg16) view.findViewById(R.id.txt_payment_details)).setOnClickListener(paymentClick);


    }


    View.OnClickListener paymentClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), PaymentDetailsActivity.class);
            startActivity(i);

            // CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");

        }
    };
}
