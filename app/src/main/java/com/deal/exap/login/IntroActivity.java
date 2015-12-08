package com.deal.exap.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.deal.exap.R;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();
    }

    private void init(){

        TextView tvSignin = (TextView) findViewById(R.id.tv_siginin);
        tvSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_siginin:
                startActivity(new Intent(this, SplashScreen.class));
                break;
        }
    }
}
