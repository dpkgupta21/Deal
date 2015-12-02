package com.deal.exap.category;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.exap.R;
import com.deal.exap.category.adapter.CategoriesListAdapter;
import com.deal.exap.favorite.adapter.FavoriteListAdapter;
import com.deal.exap.favorite.bean.DataObject;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
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
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_category);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CategoriesListAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        ((CategoriesListAdapter) mAdapter).setOnItemClickListener(new CategoriesListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });

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

}
