package com.deal.exap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg12;
import com.deal.exap.navigationdrawer.NavigationDrawerActivity;

/**
 * A login screen that offers login via email/password.
 */

public class SignInFragment extends Fragment {

    private View view;

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
        ((TextView) view.findViewById(R.id.txt_forget_password_click)).
                setOnClickListener(goToForgetPasswordPage);

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
            Intent i = new Intent(getContext(), NavigationDrawerActivity.class);
            startActivity(i);

        }
    };


}

