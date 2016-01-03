package com.deal.exap.login;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyEditTextViewReg;
import com.deal.exap.customviews.MyTextViewLight14;
import com.deal.exap.customviews.MyTextViewReg14;
import com.deal.exap.login.Adapter.CountryCodeAdapter;
import com.deal.exap.misc.CustomAlertDialog;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.TJPreferences;
import com.deal.exap.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */

public class NumberVerificationFragment extends Fragment {


    private View view;
    private MyTextViewReg14 edtCountryCode;
    private Dialog dialogCountryCode;
    private List<Map<String, String>> countryCodeList;

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

        init();
    }

    private void init()
    {
        countryCodeList = getCountryCode();
        ((MyButtonViewSemi) view.findViewById(R.id.btn_send_code)).setOnClickListener(goToNumberVerificationStage2);
        edtCountryCode = (MyTextViewReg14) view.findViewById(R.id.edt_contry_code);
      edtCountryCode.setText(getCountryCodeByCountryName("Saudi Arabia"));
        edtCountryCode.setOnClickListener(openDialogForCountry);
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


    View.OnClickListener openDialogForCountry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogCountryCode = new Dialog(getActivity());
            dialogCountryCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCountryCode.setContentView(R.layout.layout_country_code);
           getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ListView listView = (ListView) dialogCountryCode.findViewById(R.id.list);
            CountryCodeAdapter adapter = new CountryCodeAdapter(getActivity(), countryCodeList);
            listView.setAdapter(adapter);
            dialogCountryCode.show();
            listView.setOnItemClickListener(dialogItemClickListener);


        }
    };

    private List<Map<String, String>> getCountryCode() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(Utils.loadJSONFromAsset(getActivity()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Map<String, String> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                map.put("dial_code", jsonObject.getString("dial_code"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    AdapterView.OnItemClickListener dialogItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            edtCountryCode.setText(countryCodeList.get(i).get("dial_code"));
            dialogCountryCode.dismiss();
        }
    };


  private String  getCountryCodeByCountryName(String countryName)
    {
        for(int i=0;i<countryCodeList.size();i++)
        {
           if(countryName.equalsIgnoreCase(countryCodeList.get(i).get("name")))
           {
               return countryCodeList.get(i).get("dial_code");
           }

        }



        return "";
    }



}

