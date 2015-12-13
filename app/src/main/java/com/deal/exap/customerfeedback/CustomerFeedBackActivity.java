package com.deal.exap.customerfeedback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customerfeedback.adapter.CustomerFeedBackListAdapter;
import com.deal.exap.favorite.bean.DataObject;

import java.util.ArrayList;

public class CustomerFeedBackActivity extends AppCompatActivity {

    private ListView listView;
    private Toolbar mToolbar;
    private TextView mToolBarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feed_back);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBarTitle = (TextView) mToolbar.findViewById(R.id.txt_title);
        setSupportActionBar(mToolbar);

        mToolBarTitle.setText(getString(R.string.customer_feedback_title));


        listView =(ListView)findViewById(R.id.list_view_feedback);
        listView.setAdapter(new CustomerFeedBackListAdapter(CustomerFeedBackActivity.this,getDataSet()));


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
