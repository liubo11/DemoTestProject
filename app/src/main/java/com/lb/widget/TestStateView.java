package com.lb.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LiuBo on 2016-10-25.
 */

public class TestStateView extends View {
    public TestStateView(Context context) {
        super(context);
    }

    public TestStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);





    }




    private int mState;
    public void switchState(){
        mState++;
        if (mState > 4) {
            mState = 0;
        }
    }
}
