package com.deal.exap.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.deal.exap.R;

public class PostFeedbackActivity extends Activity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ((Button) findViewById(R.id.btn_post_comments)).setOnClickListener(callFeedBack);
        ((Button) findViewById(R.id.btn_cancel)).setOnClickListener(this);
    }

    View.OnClickListener callFeedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
