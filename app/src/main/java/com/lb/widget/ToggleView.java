package com.lb.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by LiuBo on 2016-10-20.
 */

public class ToggleView extends LinearLayout {
    public static final int TOGGLE_LEFT = 0;
    public static final int TOGGLE_RIGHT = 1;

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private int mTaggledColor = 0xFFFF0000;
    private int mUntaggledColor = 0xFF808080;
    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredWidth() != 0 && getMeasuredHeight() != 0) {

            if (getChildCount() == 2) {

                View childLeft = getChildAt(0);
                View childRight = getChildAt(1);




            }
        }
    }

    public interface ToggleListener {
        /**
         * idx {@link #TOGGLE_LEFT} {@link #TOGGLE_RIGHT}
         */
        void onToggleListener(View v, int idx);
    }
}
