package com.deal.exap.login;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.TouchEffect;

public class BaseActivity extends AppCompatActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    public static final TouchEffect TOUCH = new TouchEffect();
    public Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseFragment.this));
    }

    public View setTouchNClick(int id) {

        View v = findViewById(id);
        v.setOnClickListener(this);
        v.setOnTouchListener(TOUCH);
        return v;
    }

    public View setClick(int id) {

        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }

    public void setViewEnable(int id, boolean flag) {
        View v = findViewById(id);
        v.setEnabled(flag);
    }

    public void setViewVisibility(int id, int flag) {
        View v = findViewById(id);
        v.setVisibility(flag);
    }

    public void setTextViewText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }


    public void setTextViewHtmlText(int id, String text) {
        TextView tv = ((TextView) findViewById(id));
        tv.setText(Html.fromHtml(text));
    }

    public void setTextViewTextColor(int id, int color) {
        ((TextView) findViewById(id)).setTextColor(color);
    }

    public void setEditText(int id, String text) {
        ((EditText) findViewById(id)).setText(text);
    }

    public String getEditTextText(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    public String getTextViewText(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }

    public String getButtonText(int id) {
        return ((Button) findViewById(id)).getText().toString();
    }

    public void setButtonText(int id, String text) {
        ((Button) findViewById(id)).setText(text);
    }

    public void replaceButtoImageWith(int replaceId, int drawable) {
        ((Button) findViewById(replaceId)).setBackgroundResource(drawable);
    }


    public void chnageBackGround(int id, Drawable drawable) {
        ((Button) findViewById(id)).setBackground(drawable);

    }

    public void setButtonSelected(int id, boolean flag) {
        ((Button) findViewById(id)).setSelected(flag);
    }

    public boolean isButtonSelected(int id) {
        return ((Button) findViewById(id)).isSelected();
    }

    /**
     * Method use to set view selected
     *
     * @param id   resource id of view.
     * @param flag true if view want to selected else false
     */
    public void setViewSelected(int id, boolean flag) {
        View v = findViewById(id);
        v.setSelected(flag);
    }

    /**
     * Method use to set text on TextView, EditText, Button.
     *
     * @param id   resource of TextView, EditText, Button.
     * @param text string you want to set on TextView, EditText, Button
     */
    public void setViewText(int id, String text) {
        View v = ((View) findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            tv.setText(text);
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setText(text);
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            btn.setText(text);
        }

    }

    public void setViewText(View view, int id, String text) {
        View v = ((View) view.findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            tv.setText(text);
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setText(text);
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            btn.setText(text);
        }

    }

    /**
     * Method use to get Text from TextView, EditText, Button.
     *
     * @param id resource id TextView, EditText, Button
     * @return string text from view
     */
    public String getViewText(int id) {
        String text = "";
        View v = ((View) findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            text = tv.getText().toString().trim();
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            text = et.getText().toString().trim();
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            text = btn.getText().toString().trim();
        }
        return text;
    }

    public boolean isCheckboxChecked(int id) {
        CheckBox cb = (CheckBox) findViewById(id);
        return cb.isChecked();
    }

    public void setCheckboxChecked(int id, boolean flag) {
        CheckBox cb = (CheckBox) findViewById(id);
        cb.setChecked(flag);
    }

    public void setOnCheckbox(int id) {
        CheckBox cb = (CheckBox) findViewById(id);
        cb.setOnCheckedChangeListener(this);
    }

    public void setImageResourseBackground(int id, int resId) {
        ImageView iv = (ImageView) findViewById(id);
        iv.setImageResource(resId);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onClick(View view) {

    }

    public void removeToolbar() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            //toolbar.setVisibility(View.GONE);
            toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
//
//        ImageView ivClose = (ImageView) toolbar.findViewById(R.id.iv_close);
//        ivClose.setVisibility(View.INVISIBLE);

            TextView tvHeader = (TextView) toolbar.findViewById(R.id.tv_header);
            tvHeader.setText("");

            try {
                Menu imenu = toolbar.getMenu();
                imenu.findItem(R.id.menu_notification).setVisible(false);
                imenu.removeItem(R.id.menu_notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetToolbar(String header) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
//        if (header.equalsIgnoreCase(getString(R.string.menu_near_by))) {
//            toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_transparent_color));
//        } else if (header.equalsIgnoreCase(getString(R.string.menu_wallet))) {
//            toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_transparent_color));
//        } else if (header.equalsIgnoreCase(getString(R.string.menu_favorite))) {
//            toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_transparent_color));
//        } else if (header.equalsIgnoreCase(getString(R.string.menu_following))) {
//            toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_transparent_color));
//        } else {
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        //  }


        TextView tvHeader = (TextView) toolbar.findViewById(R.id.tv_header);
        tvHeader.setText(header);

    }


    public void setHeader(String header) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tvHeader = (TextView) toolbar.findViewById(R.id.tv_header);
        tvHeader.setText(header);
    }

    public void setRightClick() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        ImageView ivRight = (ImageView) toolbar.findViewById(R.id.iv_close);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(this);
    }


    public void setRighClick(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ImageView ivRight = (ImageView) toolbar.findViewById(R.id.iv_close);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(this);
    }

    public void setLeftClick() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        ImageView ivLeft = (ImageView) toolbar.findViewById(R.id.iv_back);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);
    }

    public void setLeftClick(int drawbleId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        ImageView ivLeft = (ImageView) toolbar.findViewById(R.id.iv_back);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);

        ivLeft.setImageResource(drawbleId);
    }

    public void setHeaderNormal() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
    }

    public void setHeaderTransparent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_transparent_color));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
