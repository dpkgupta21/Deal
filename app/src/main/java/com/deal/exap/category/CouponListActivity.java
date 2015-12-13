package com.deal.exap.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.nearby.RedeemCouponActivity;
import com.deal.exap.nearby.adapter.NearByListAdapter;

import java.util.ArrayList;

public class CouponListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);

        init();
        setList();
    }

    private void init(){
        setHeader("Auto");
        setLeftClick();
        setHeaderNormal();
    }

    public void setList(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_nearby);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NearByListAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);


        ((NearByListAdapter) mAdapter).setOnItemClickListener(new NearByListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent i = new Intent(CouponListActivity.this, RedeemCouponActivity.class);
                startActivity(i);

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
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
}
