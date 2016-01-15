package com.deal.exap.following;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.category.CouponListActivity;
import com.deal.exap.databasemanager.DatabaseHelper;
import com.deal.exap.databasemanager.DatabaseManager;
import com.deal.exap.favorite.adapter.FavoriteListAdapter;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.following.adapter.FollowingListAdapter;
import com.deal.exap.misc.MyOnClickListener;
import com.deal.exap.misc.RecyclerTouchListener;
import com.deal.exap.model.FavoriteDTO;
import com.deal.exap.model.FollowingDTO;
import com.deal.exap.partner.ChatActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FollowingFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;

    private List<FollowingDTO> followingList;
    private Dao<FollowingDTO, String> followingDao;

    public FollowingFragment() {
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
        view = inflater.inflate(R.layout.fragment_following, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.following_screen_title));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.following_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        init();
        getFollowingList();


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }


    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }


    public void getFollowingList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_FOLLOWING_LIST);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgeessDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<FollowingDTO>>() {
                                }.getType();
                                followingList = new Gson().fromJson(response.getJSONArray("following").toString(), type);
                                setFollowingList();
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
            pdialog.show();
        } else {
            try {
                followingList = followingDao.queryForAll();
                setFollowingList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ///Utils.showNoNetworkDialog(getActivity());
        }

    }

    public void setFollowingList() {
        mAdapter = new FollowingListAdapter(followingList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new MyOnClickListener() {
            @Override
            public void onRecyclerClick(View view, int position) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }

            @Override
            public void onRecyclerLongClick(View view, int position) {

            }
        }));
    }

    private void init() {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(getActivity());
        try {
            followingDao = db.getFollowingDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
