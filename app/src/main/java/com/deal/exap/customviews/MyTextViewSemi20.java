package com.deal.exap.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class MyTextViewSemi20 extends TextView {

    public MyTextViewSemi20(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewSemi20(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewSemi20(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Semibold.ttf");
        setTypeface(tf);
        setTextSize(20);
    }

}


