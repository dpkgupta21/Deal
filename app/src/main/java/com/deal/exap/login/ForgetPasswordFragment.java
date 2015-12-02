package com.deal.exap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.exap.R;

/**
 * A login screen that offers login via email/password.
 */

public class ForgetPasswordFragment extends Fragment {

    private View view;

    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        return fragment;
    }

    public ForgetPasswordFragment() {
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
        view = inflater.inflate(R.layout.forget_password, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



}

