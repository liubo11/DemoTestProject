package com.lb.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016-08-01.
 */
public class OverViewPager extends ViewPager {
    private static final String TAG = OverViewPager.class.getSimpleName();
    private static final int SF_CLICK_SCOP = 5;

    public OverViewPager(Context context) {
        this(context, null);
    }

    public OverViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Log.d(TAG, "init this view");

        float ds = getResources().getDisplayMetrics().density;
        mClickScop = (int) (SF_CLICK_SCOP * ds + 0.5f);
    }

    private int mClickScop = 5;
    private int mDownX;
    private int mDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = (int) ev.getX();
            mDownY = (int) ev.getY();
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int upX = (int) ev.getX();
            int upY = (int) ev.getY();
            if ((Math.abs(upX - mDownX) < mClickScop) && (Math.abs(upY - mDownY) < mClickScop)) {
                View view = viewOfClickOnScreen(ev);
                if (view != null) {
                    setCurrentItem(indexOfChild(view));
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * @param ev
     * @return
     */
    private View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = getTop();
            int maxX = location[0] + v.getWidth();
            int maxY = getBottom();
            float x = ev.getX();
            float y = ev.getY();
            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }
}

