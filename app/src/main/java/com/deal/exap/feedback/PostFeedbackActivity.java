package com.deal.exap.feedback;

import android.os.Bundle;
import android.view.View;

import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;

public class PostFeedbackActivity extends BaseActivity implements View.OnClickListener{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        init();
    }

    private void init(){

        setTouchNClick(R.id.btn_cancel);
        setTouchNClick(R.id.btn_post_comments);

        setHeader(getString(R.string.post_your_feedback_header));
        setRightClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }
}
