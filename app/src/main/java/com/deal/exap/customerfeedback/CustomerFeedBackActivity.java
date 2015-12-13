package com.deal.exap.customerfeedback;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.deal.exap.R;
import com.deal.exap.customerfeedback.adapter.CustomerFeedBackListAdapter;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseActivity;

import java.util.ArrayList;

public class CustomerFeedBackActivity extends BaseActivity {

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feed_back);

        listView =(ListView)findViewById(R.id.list_view_feedback);
        listView.setAdapter(new CustomerFeedBackListAdapter(CustomerFeedBackActivity.this,getDataSet()));

        init();
    }

    private void init(){
        setRightClick();
        setHeader(getString(R.string.title_custom_feedback));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_close:
                finish();
                break;
        }
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 2; index++) {
            DataObject obj = new DataObject(getString(R.string.txt_category_name),
                    "" + index);
            results.add(index, obj);
        }
        return results;
    }

}
