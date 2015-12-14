package com.deal.exap.login;

import android.os.Bundle;
import android.view.View;

import com.deal.exap.R;

public class ProfileActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }


    private void init(){
        setHeader("Edit Profile");
        setLeftClick();
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
