package com.deal.exap.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditTextViewSemi extends EditText {

    public MyEditTextViewSemi(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditTextViewSemi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditTextViewSemi(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Semibold.ttf");
        setTypeface(tf);
    }

}
