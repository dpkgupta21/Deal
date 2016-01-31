package com.deal.exap.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.partner.FollowingPartnerDetails;
import com.deal.exap.model.DealDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WalletFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private ArrayList<DealDTO> walletValues;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public WalletFragment() {
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

        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_wallet);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //setTitleFragment(getString(R.string.wallet_screen_title));

        getWalletList();


        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getWalletList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_wallet, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_notification:


            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void getWalletList() {
        if(Utils.isOnline(getActivity())){
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_WALLET);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("lat", String.valueOf(DealPreferences.getLatitude(getActivity().
                    getApplicationContext())));
            params.put("lng", String.valueOf(DealPreferences.getLongitude(getActivity().
                    getApplicationContext())));
            params.put("user_id",Utils.getUserId(getActivity()));

            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<DealDTO>>(){}.getType();
                                walletValues = new Gson().fromJson(response.getJSONArray("deal").toString(), type);
                                setWalletValues();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
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
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        }
        else{
            Utils.showNoNetworkDialog(getActivity());
        }
    }

    private void setWalletValues() {

        mAdapter = new WalletAdapter(walletValues, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        ((WalletAdapter) mAdapter).setOnItemClickListener(new WalletAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent i;
                switch (v.getId()) {
                    case R.id.thumbnail:
                        i = new Intent(getActivity(), ShowWalletDetails.class);
                        i.putExtra("id", walletValues.get(position).getId());
                        startActivity(i);
                        break;

                    case R.id.img_title:
                        i = new Intent(getActivity(), FollowingPartnerDetails.class);
                        i.putExtra("partnerId", walletValues.get(position).getPartner_id());
                        startActivity(i);

                }


            }
        });
    }

}
