package com.deal.exap.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.deal.exap.R;

public class EditProfileActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }


    private void init(){
        setHeader("Edit Profile");
        setLeftClick();

        setClick(R.id.et_age);
        setClick(R.id.et_sex);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_sex:
                showSexDialog();
                break;
            case R.id.et_age:
                showAgeDialog();
                break;
        }
    }

    public void showAgeDialog(){
        final CharSequence[] items = new CharSequence[90];
        for(int i=0;i<items.length;i++){
            items[i] = (i+10)+"";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Age");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.et_age, items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showSexDialog(){
        final CharSequence[] items = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Gender");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.et_sex, items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
