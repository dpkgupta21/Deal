package com.deal.exap.following;

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

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.following.adapter.FollowingListAdapter;
import com.deal.exap.partner.ChatActivity;

import java.util.ArrayList;


public class FollowingFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
//    public static FollowingFragment newInstance(String param1, String param2) {
//        FollowingFragment fragment = new FollowingFragment();
//
//        return fragment;
//    }

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
        view= inflater.inflate(R.layout.fragment_following, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleFragment(getString(R.string.following_screen_title));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.following_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FollowingListAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        ((FollowingListAdapter) mAdapter).setOnItemClickListener(new FollowingListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
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

}
