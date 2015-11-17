package com.deal.exap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.exap.R;
import com.deal.exap.customviews.MyTextViewReg12;

/**
 * A login screen that offers login via email/password.
 */

public class NumberVerificationFragment2 extends Fragment {

    private View view;

    public static NumberVerificationFragment2 newInstance() {
        NumberVerificationFragment2 fragment = new NumberVerificationFragment2();
        return fragment;
    }

    public NumberVerificationFragment2() {
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
        ((MyTextViewReg12) view.findViewById(R.id.txt_sign_up_click)).setOnClickListener(goToSignupClick);
        super.onActivityCreated(savedInstanceState);


    }


    View.OnClickListener goToSignupClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), SignUp.class);
            startActivity(i);

        }
    };
}

