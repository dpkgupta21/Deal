package com.deal.exap.partner;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.deal.exap.R;
import com.deal.exap.favorite.bean.DataObject;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.partner.adapter.ChatListAdapter;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity {

    private ListView lvChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    private void init() {

        lvChat = (ListView) findViewById(R.id.lv_chat);
        lvChat.setAdapter(new ChatListAdapter(this, getDataSet()));

        setHeader(getString(R.string.discussion_title));
        setLeftClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 10; index++) {
            DataObject obj = new DataObject(getString(R.string.auto),
                    "" + index);
            results.add(index, obj);
        }
        return results;
    }

}
