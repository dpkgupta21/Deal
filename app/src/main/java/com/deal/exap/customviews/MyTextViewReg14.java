package com.deal.exap.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class MyTextViewReg14 extends TextView {

    public MyTextViewReg14(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewReg14(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewReg14(Context context) {
        super(context);
        init();
    }

    private void init() {
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Light.ttf");
        setTypeface(tf);
        setTextSize(14);
    }

}