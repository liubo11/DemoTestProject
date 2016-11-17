package cn.lb.amz.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by LiuBo on 2016-11-17.
 */

public class MagnView extends View {
    public MagnView(Context context) {
        super(context);
        init(context);
    }

    public MagnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    private Paint mPaint;
    private ValueAnimator mMagAnim;
    private float mMgValue;
    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);

        mMagAnim = ValueAnimator.ofFloat(0, 1F);
        mMagAnim.setDuration(600);
        mMagAnim.setInterpolator(new DecelerateInterpolator());
        mMagAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMgValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }
    private int mTrans = 200;
    private int mRadu = 20;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x80FF0000);

        canvas.save();

        float sv = mMgValue * 4;
        float rd = mRadu;
        if (mMgValue > 0.5F) {
            sv = 0.5F * 4;
            rd = mRadu +  (mMgValue - 0.5F) * mRadu * 2;
        }

        mPaint.setStrokeWidth(10 + 10 * sv);
        canvas.drawCircle(downX,downY - mTrans * mMgValue, rd, mPaint);

        canvas.restore();
    }

    private float downX;
    private float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            downX = event.getX();
            downY = event.getY();
            trig();
        }

        return true;
    }


    private void trig() {
        mMagAnim.start();
    }
}
