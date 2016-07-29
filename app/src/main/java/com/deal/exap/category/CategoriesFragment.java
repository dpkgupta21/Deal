package com.deal.exap.category;

import android.app.Activity;
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
import com.deal.exap.category.adapter.CategoriesListAdapter;
import com.deal.exap.customviews.CustomProgressDialog;
import com.deal.exap.databasemanager.DatabaseHelper;
import com.deal.exap.databasemanager.DatabaseManager;
import com.deal.exap.deal.CategoryDealListActivity;
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


public class CategoriesFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private List<CategoryDTO> categoryList;
    private View view;
    private Dao<CategoryDTO, String> categoryDao;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Activity mActivity;


//    public static CategoriesFragment newInstance() {
//        CategoriesFragment fragment = new CategoriesFragment();
//
//        return fragment;
//    }

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen

        view = inflater.inflate(R.layout.fragment_blank, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity= getActivity();
        // set title here
        //((BaseActivity) getActivity()).setHeader("");


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_category);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        init();
        getCategoryList();

        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getCategoryList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean enable = false;
                if (mRecyclerView != null && mRecyclerView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = ((LinearLayoutManager) mRecyclerView.
                            getLayoutManager()).findFirstVisibleItemPosition() == 0;
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
        inflater.inflate(R.menu.menu_categories, menu);

        ((BaseActivity) mActivity).removeToolbar();
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_notification:
                Intent i = new Intent(mActivity.getApplicationContext(), HomeActivity.class);
                i.putExtra("fragmentName", mActivity.getString(R.string.alert_screen_title));
                i.putExtra("isForInbox", true);
                startActivity(i);
                mActivity.finish();

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void setTitleFragment(String strTitle) {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) mActivity).findViewById(R.id.tool_bar);
        TextView txtTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        txtTitle.setText(strTitle);
    }


    public void getCategoryList() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_CATEOGRY_LIST);
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            params.put("user_id", Utils.getUserId(mActivity));
            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                TextView txt_blank = (TextView) view.findViewById(R.id.txt_blank);
                                if (response.getBoolean("status")) {
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    txt_blank.setVisibility(View.GONE);
                                    Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                    Type type = new TypeToken<ArrayList<CategoryDTO>>() {
                                    }.getType();
                                    categoryList = new Gson().fromJson(response.getJSONArray("category").toString(), type);
                                    if (categoryList.size() != 0) {
                                        setCategoryList();
                                    } else {
                                        mRecyclerView.setVisibility(View.GONE);
                                        txt_blank.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    mRecyclerView.setVisibility(View.GONE);
                                    String msg = response.getString("message");
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
                    Utils.showExceptionDialog(mActivity);
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
                categoryList = categoryDao.queryForAll();
                setCategoryList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ///Utils.showNoNetworkDialog(mActivity);
        }

    }

    public void setCategoryList() {
        RecyclerView.Adapter mAdapter = new CategoriesListAdapter(categoryList, mActivity);
        mRecyclerView.setAdapter(mAdapter);


        ((CategoriesListAdapter) mAdapter).setOnItemClickListener(new CategoriesListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                switch (v.getId()) {
                    case R.id.thumbnail:

                        CustomProgressDialog.showProgDialog(mActivity, null);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("categoryDTO", categoryList.get(position));
                        Intent intent = new Intent(mActivity, HomeActivity.class);
                        //intent.putExtras(bundle);
                        intent.putExtra("categoryId", categoryList.get(position).getId());

                        intent.putExtra("categoryName", categoryList.get(position).getName());
                        intent.putExtra("fragmentName", mActivity.getString(R.string.nearby_screen_title));
                        startActivity(intent);

                        CustomProgressDialog.hideProgressDialog();


                        //Intent i = new Intent(mActivity, CategoryDealListActivity.class);
//                        i.putExtras(bundle);
//                        startActivity(i);
                        break;
                    case R.id.img_like:
                        addRemove(categoryList.get(position).getId(), categoryList.get(position).getFavourite());
                        break;

                }


            }
        });


    }

    private void init() {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        DatabaseHelper db = manager.getHelper(mActivity);
        try {
            categoryDao = db.getCategoryDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addRemove(String id, String status) {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.ADD_REMOVE_CATEGORY_FAVORITE);
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            params.put("category_id", id);
            params.put("status", status.equalsIgnoreCase("1") ? "0" : "1");
            params.put("user_id", Utils.getUserId(mActivity));
            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());

                                getCategoryList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(mActivity);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(mActivity);
        }
    }


}
