package com.lb.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-10-12.
 */

public class FreeTintView extends ImageView {
    public FreeTintView(Context context) {
        this(context, null);
    }

    public FreeTintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreeTintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FreeTintView);
        String attrString = ta.getString(R.styleable.FreeTintView_testString);
        System.out.println("attrString = "+attrString);

        ColorStateList colorStateList = ta.getColorStateList(R.styleable.FreeTintView_testTint);
        ColorStateList colorStateList1 = ta.getColorStateList(R.styleable.FreeTintView_testTintReference);

        ta.recycle();
    }




}
