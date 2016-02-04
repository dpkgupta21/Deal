package com.deal.exap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;

public class VerificationOptionFragment extends Fragment {


    private static final String TAG = "VerificationOptionFragment";
    private View view;

    public static VerificationOptionFragment newInstance() {
        VerificationOptionFragment fragment = new VerificationOptionFragment();
        return fragment;
    }

    public VerificationOptionFragment() {
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
        view = inflater.inflate(R.layout.verification_option, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        ((MyButtonViewSemi) view.findViewById(R.id.btn_register_from_email_address)).setOnClickListener(callToSignUp);
        ((MyButtonViewSemi) view.findViewById(R.id.btn_register_from_mob_number)).setOnClickListener(numberVerification);
    }

    View.OnClickListener callToSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity().getApplicationContext(), SignUp.class);
            startActivity(intent);

        }
    };


    View.OnClickListener numberVerification = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NumberVerificationFragment numberVerification = NumberVerificationFragment.newInstance();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_lay, numberVerification);
            ft.addToBackStack(null);
            ft.commit();



        }
    };



}

