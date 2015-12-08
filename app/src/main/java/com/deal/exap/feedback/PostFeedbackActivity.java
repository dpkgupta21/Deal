package com.deal.exap.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.deal.exap.R;
import com.deal.exap.customerfeedback.CustomerFeedBackActivity;

public class PostFeedbackActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ((Button) findViewById(R.id.btn_post_comments)).setOnClickListener(callFeedBack);
    }

    View.OnClickListener callFeedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(PostFeedbackActivity.this, CustomerFeedBackActivity.class);
            startActivity(i);
        }
    };

}
