package com.deal.exap.alert;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.alert.adapter.AlertListAdapter;
import com.deal.exap.alert.adapter.MessageListAdapter;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseFragment;
import com.deal.exap.model.ConversionsDTO;
import com.deal.exap.model.DealDTO;
import com.deal.exap.model.NotificationDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.TJPreferences;
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


public class AlertFragment extends BaseFragment {


    private View view;
    private ListView lvMessage, lvNotification;
    private Button btnMessage, btnNotification;
    private ArrayList<NotificationDTO> notificationList;

    private ArrayList<ConversionsDTO> messageList;
//    public static AlertFragment newInstance() {
//        AlertFragment fragment = new AlertFragment();
//        return fragment;
//    }

    public AlertFragment() {
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
        view = inflater.inflate(R.layout.fragment_alert, container, false);

        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvMessage = (ListView) view.findViewById(R.id.lv_messsage);
        lvNotification = (ListView) view.findViewById(R.id.lv_notification);


        lvNotification.setVisibility(View.VISIBLE);
        lvMessage.setVisibility(View.GONE);

        getNotificationList();


    }

    private void init() {
        setTouchNClick(R.id.btn_notification, view);
        setTouchNClick(R.id.btn_message, view);
        btnNotification = (Button) view.findViewById(R.id.btn_notification);
        btnNotification.setSelected(true);
        btnMessage = (Button) view.findViewById(R.id.btn_message);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_message:
                btnMessage.setSelected(true);
                btnNotification.setSelected(false);
                lvMessage.setVisibility(View.VISIBLE);
                getMessageList();
                lvNotification.setVisibility(View.GONE);
                break;
            case R.id.btn_notification:
                btnMessage.setSelected(false);
                btnNotification.setSelected(true);
                lvMessage.setVisibility(View.GONE);
                getNotificationList();
                lvNotification.setVisibility(View.VISIBLE);
                break;
        }
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 10; index++) {
            DataObject obj = new DataObject("Auto",
                    "" + index);
            results.add(index, obj);
        }
        return results;
    }

    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }

    private void getNotificationList() {


        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_NOTIFICATION);
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<NotificationDTO>>() {
                                }.getType();
                                notificationList = new Gson().fromJson(response.getJSONArray("notifications").toString(), type);
                                setNotificationList();
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
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }


    }

    private void getMessageList() {


        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_MESSAGE);
            params.put("user_id", Utils.getUserId(getActivity()));
            params.put("lang", Utils.getSelectedLanguage(getActivity()));

            final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<ConversionsDTO>>() {
                                }.getType();
                                messageList = new Gson().fromJson(response.getJSONArray("conversions").toString(), type);
                                setLvMessageList();
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
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }

    }

    private void setNotificationList() {

        lvNotification.setAdapter(new AlertListAdapter(getActivity(), notificationList, 0, getActivity()));

    }

    private void setLvMessageList() {
        lvMessage.setAdapter(new MessageListAdapter(getActivity(), messageList, 1, getActivity()));

    }

}
