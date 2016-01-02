package com.deal.exap.interest;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.category.CategoriesFragment;
import com.deal.exap.customviews.MyTextViewReg16;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InterestFragment extends Fragment {

    private View view;
    private List<String> interestValues;
    private List<String> interestValuesSelected;

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
        setHasOptionsMenu(true);

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

        getInterestList();

    }


    View.OnClickListener interestSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyTextViewReg16 textViewReg16 = (MyTextViewReg16) v;
            int position = Integer.parseInt(textViewReg16.getTag().toString());
            if (interestValuesSelected.contains(interestValues.get(position))) {
                textViewReg16.setBackgroundResource(R.drawable.txt_interest_border_unselect);
                textViewReg16.setTextColor(getResources().getColor(R.color.black));
                interestValuesSelected.remove(interestValues.get(position));

            } else {
                textViewReg16.setBackgroundResource(R.drawable.txt_interest_border_select);
                textViewReg16.setTextColor(getResources().getColor(R.color.white));
                interestValuesSelected.add(interestValues.get(position));

            }

        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_interest, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_check:

                CategoriesFragment categoriesFragment = new CategoriesFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm
                        .beginTransaction();
                ft.replace(R.id.body_layout, categoriesFragment);
                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void getInterestList() {
        if(Utils.isOnline(getActivity())){
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_INTEREST);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                pdialog.dismiss();
                                setInterestList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        }
        else{
            Utils.showNoNetworkDialog(getActivity());
        }

    }

    public void setInterestList(){
        interestValues = new ArrayList<>();
        interestValuesSelected = new ArrayList<>();
        String[] interest = getActivity().getResources().getStringArray(R.array.interset_values);
        for (int i = 0; i < interest.length; i++) {
            interestValues.add(interest[i]);
        }

        final FlowLayout layout = (FlowLayout) view.findViewById(R.id.flowLayout);


        for (int i = 0; i < interestValues.size(); i++) {
            final MyTextViewReg16 textView = new MyTextViewReg16(getActivity());
            FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT,
                    FlowLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(param);
            textView.setTextSize(16);
            textView.setBackgroundResource(R.drawable.txt_interest_border_unselect);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setText(interestValues.get(i));
            textView.setTag(i);
            textView.setClickable(true);
            textView.setOnClickListener(interestSelectListener);
            layout.addView(textView, 0);
        }
    }
}
