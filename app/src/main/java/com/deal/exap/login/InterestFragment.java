package com.deal.exap.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deal.exap.R;


public class InterestFragment extends Fragment {

    private View view;

//    public static InterestFragment newInstance() {
//        InterestFragment fragment = new InterestFragment();
//        return fragment;
//    }

    public InterestFragment() {
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
        view = inflater.inflate(R.layout.fragment_interest, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String interestValues[] = getActivity().getResources().getStringArray(R.array.interset_values);
        int count = (interestValues.length % 3) == 0 ? interestValues.length / 3 : (interestValues.length / 3) + 1;
        int counter = 0;
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear);

        for (int i = 0; i < count; i++) {
            LinearLayout linear_horizontal = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params_horizontal = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linear_horizontal.setWeightSum(8);
            linear_horizontal.setOrientation(LinearLayout.HORIZONTAL);
            linear_horizontal.setLayoutParams(params_horizontal);


            LinearLayout.LayoutParams params_left = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params_left.weight = 2;
            params_left.setMargins(0, 0, 0, 0);
            TextView txt_interest_left = new TextView(getActivity());
            txt_interest_left.setBackgroundResource(R.drawable.txt_outer_border);
            txt_interest_left.setText(interestValues[i + counter]);
            txt_interest_left.setTag(i + counter);
            txt_interest_left.setGravity(Gravity.CENTER);
            txt_interest_left.setPadding(10, 10, 10, 10);
            txt_interest_left.setLayoutParams(params_left);
            linear_horizontal.addView(txt_interest_left);

            // Add space between text view
            LayoutParams params_view = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            params_view.weight = 1;
            View view_transparent = new View(getActivity());
            view_transparent.setLayoutParams(params_view);
            linear_horizontal.addView(view_transparent);


            // Add textview at middle side
            if ((i + counter + 1) < interestValues.length) {
                LayoutParams params_right = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                params_right.weight = 2;
                params_right.setMargins(0, 0, 0, 0);
                TextView txt_category_right = new TextView(getActivity());
                txt_category_right.setBackgroundResource(R.drawable.txt_outer_border);
                txt_category_right.setText(interestValues[i + counter + 1]);
                txt_category_right.setTag(i + counter + 1);
                txt_category_right.setPadding(10, 10, 10, 10);
                txt_category_right.setGravity(Gravity.CENTER);
                txt_category_right.setLayoutParams(params_right);
                linear_horizontal.addView(txt_category_right);
            }
            // Add space between text view
            LayoutParams params_view1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            params_view.weight = 1;
            View view_transparent1 = new View(getActivity());
            view_transparent.setLayoutParams(params_view);
            linear_horizontal.addView(view_transparent1);


            // Add textview at right side
            if ((i + counter + 2) < interestValues.length) {
                LayoutParams params_right = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                params_right.weight = 2;
                params_right.setMargins(0, 0, 0, 0);
                TextView txt_category_right = new TextView(getActivity());
                txt_category_right.setBackgroundResource(R.drawable.txt_outer_border);
                txt_category_right.setText(interestValues[i + counter + 2]);
                txt_category_right.setTag(i + counter + 2);
                txt_category_right.setPadding(10, 10, 10, 10);
                txt_category_right.setGravity(Gravity.CENTER);
                txt_category_right.setLayoutParams(params_right);
                linear_horizontal.addView(txt_category_right);
            }
            // Add all five child in parent
            linear.addView(linear_horizontal);
            counter++;
        }


    }


}
