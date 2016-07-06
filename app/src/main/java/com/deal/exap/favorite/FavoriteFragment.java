package com.deal.exap.favorite;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.databasemanager.DatabaseHelper;
import com.deal.exap.databasemanager.DatabaseManager;
import com.deal.exap.deal.CategoryDealListActivity;
import com.deal.exap.favorite.adapter.FavoriteListAdapter;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.CategoryDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
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

public class FavoriteFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private List<CategoryDTO> favoriteList;
    private Dao<CategoryDTO, String> favoriteDao;
    private SwipeRefreshLayout mSwipeRefreshLayout;

//    public static FavoriteFragment newInstance() {
//        FavoriteFragment fragment = new FavoriteFragment();
//
//        return fragment;
//    }

    public FavoriteFragment() {
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
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ((BaseActivity) getActivity()).resetToolbar(getString(R.string.menu_favorite));
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setTitleFragment(getString(R.string.favorite_screen_title));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        init();
        getFavoriteList();

        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getFavoriteList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean enable = false;
                if (mRecyclerView != null && mRecyclerView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mRecyclerView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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


    public void getFavoriteList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_FAVORITE_LIST);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    TextView txt_blank = (TextView) view.findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.GONE);
                                    Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                    Type type = new TypeToken<ArrayList<CategoryDTO>>() {
                                    }.getType();
                                    favoriteList = new Gson().fromJson(response.getJSONArray("category").toString(), type);
                                    setFavoriteList();
                                } else {
                                    mRecyclerView.setVisibility(View.GONE);
                                    String msg = response.getString("message");
                                    TextView txt_blank = (TextView) view.findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.VISIBLE);
                                    txt_blank.setText(msg);
                                }
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
            try {
                favoriteList = favoriteDao.queryForAll();
                setFavoriteList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ///Utils.showNoNetworkDialog(getActivity());
        }

    }

    public void setFavoriteList() {
        RecyclerView.Adapter mAdapter = new FavoriteListAdapter(favoriteList, getActivity());
        mRecyclerView.setAdapter(mAdapter);


        ((FavoriteListAdapter) mAdapter).setOnItemClickListener(new FavoriteListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                switch (v.getId()) {
                    case R.id.thumbnail:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("categoryDTO", favoriteList.get(position));

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        //intent.putExtras(bundle);
                        intent.putExtra("categoryId", favoriteList.get(position).getId());
                        intent.putExtra("categoryName", favoriteList.get(position).getName());
                        intent.putExtra("fragmentName", getActivity().getString(R.string.nearby_screen_title));
                        startActivity(intent);

//                        Intent i = new Intent(getActivity(), CategoryDealListActivity.class);
//                        i.putExtras(bundle);
//                        startActivity(i);
                        break;
                    case R.id.img_like:
                        addRemove(favoriteList.get(position).getId());
                        break;

                }
            }

        });


    }

    private void init() {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(getActivity());
        try {
            favoriteDao = db.getCategoryDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRemove(String id) {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.ADD_REMOVE_CATEGORY_FAVORITE);
            params.put("lang", Utils.getSelectedLanguage(getActivity()));
            params.put("category_id", id);
            params.put("status", "0");
            params.put("user_id", Utils.getUserId(getActivity()));
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                               // Toast.makeText(getActivity(), Utils.getWebServiceMessage(response), Toast.LENGTH_SHORT).show();

                                getFavoriteList();
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
}



