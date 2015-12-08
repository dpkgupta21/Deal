package com.deal.exap.termscondition;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.deal.exap.R;

public class TermsConditionActivity extends Activity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        init();
    }

    private void init(){

        ImageView ivBack = (ImageView) findViewById(R.id.img_close);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_close:
                finish();
                break;
        }
    }

}
