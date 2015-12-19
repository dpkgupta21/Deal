package com.deal.exap.misc;

import android.os.Bundle;
import android.view.View;

import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;

public class ImageActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        init();
    }

    private void init(){
        setClick(R.id.iv_back);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
