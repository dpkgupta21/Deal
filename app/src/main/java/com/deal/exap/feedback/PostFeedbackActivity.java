package com.deal.exap.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.deal.exap.R;

public class PostFeedbackActivity extends Activity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        init();
    }

    private void init(){

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        ImageView ivBack = (ImageView) findViewById(R.id.img_close);
        ivBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_close:
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
