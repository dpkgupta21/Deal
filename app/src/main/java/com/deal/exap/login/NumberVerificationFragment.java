package com.deal.exap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.misc.CustomAlertDialog;
import com.deal.exap.utility.TJPreferences;

/**
 * A login screen that offers login via email/password.
 */

public class NumberVerificationFragment extends Fragment {


    private View view;

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

        ((MyButtonViewSemi) view.findViewById(R.id.btn_send_code)).setOnClickListener(goToNumberVerificationStage2);

    }

    View.OnClickListener goToNumberVerificationStage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NumberVerificationFragment2 numberVerificationFragment2 = NumberVerificationFragment2.newInstance();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_lay, numberVerificationFragment2);
            ft.addToBackStack(null);
            ft.commit();


        }
    };


}

