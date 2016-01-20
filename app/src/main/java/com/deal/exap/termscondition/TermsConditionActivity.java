package com.deal.exap.termscondition;

import android.os.Bundle;
import android.view.View;

import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;

public class TermsConditionActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        init();
    }

    private void init() {
        setRightClick();
        String term = getIntent().getStringExtra("dealTerm");
        setHeader("Terms and Condition");
        setTextViewText(R.id.txt_terms, term);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }

}
