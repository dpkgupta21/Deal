package com.deal.exap.customerfeedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deal.exap.R;
import com.deal.exap.category.adapter.CategoriesListAdapter;
import com.deal.exap.customerfeedback.adapter.CustomerFeedBackListAdapter;
import com.deal.exap.favorite.bean.DataObject;

import java.util.ArrayList;

public class CustomerFeedBackActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feed_back);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_feedback);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomerFeedBackListAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


    }


    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 10; index++) {
            DataObject obj = new DataObject(getString(R.string.txt_category_name),
                    "" + index);
            results.add(index, obj);
        }
        return results;
    }

}
