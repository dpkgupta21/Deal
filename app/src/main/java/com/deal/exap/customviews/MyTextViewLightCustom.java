package com.deal.exap.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class MyTextViewLightCustom extends TextView {

    public MyTextViewLightCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewLightCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewLightCustom(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Light.ttf");
        setTypeface(tf);
    }

}


