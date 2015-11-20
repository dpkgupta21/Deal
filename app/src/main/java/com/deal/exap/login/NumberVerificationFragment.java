package com.deal.exap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.exap.R;
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
        String language = TJPreferences.getAPP_LANG(getActivity());
        if (language.equalsIgnoreCase("ENG")) {
            view = inflater.inflate(R.layout.number_verification_1, container, false);
        } else {
            view = inflater.inflate(R.layout.number_verification_1, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


}

