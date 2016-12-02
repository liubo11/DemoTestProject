package com.lb.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-11-02.
 */

public class GuideArrowView extends View {
    public GuideArrowView(Context context) {
        this(context, null);
    }

    public GuideArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private int mDuration = 1800;

    private Drawable drawable;
    private ValueAnimator animator;
    private int mFirAlpha;
    private int mSecAlpha;
    private void init() {
        drawable = getResources().getDrawable(R.drawable.ic_arrow_test);
        animator = ValueAnimator.ofFloat(0, 6);
        animator.setDuration(mDuration);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                mFirAlpha = formatAlpha((int) (alpha * 255));
                mSecAlpha = formatAlpha((int) ((alpha - 0.5F)* 255));
                invalidate();
            }
        });
        animator.start();
    }
    private int formatAlpha(int alpha) {
        if (alpha < 0) return 0;
        if (alpha > 255) return 255;
        return alpha;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());

        canvas.save();
        canvas.rotate(mAngle, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.translate(0, getMeasuredHeight() / 5);
        drawable.setAlpha(mFirAlpha);
        drawable.draw(canvas);

        canvas.translate(0, -getHeight() * 2/ 5);
        drawable.setAlpha(mSecAlpha);
        drawable.draw(canvas);

        canvas.restore();
    }

    private int mAngle;
    public enum Gravity {
        Left,
        Top,
        Right,
        Bottom
    }
    public void rotate(Gravity gravity) {
        switch (gravity) {
            case Left:
                mAngle = 270;
                break;
            case Top:
                mAngle = 0;
                break;
            case Right:
                mAngle = 90;
                break;
            case Bottom:
                mAngle = 180;
                break;
        }
    }

    public void setColorFilter(int color) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void start() {
        animator.start();
    }

    public void stop() {
        animator.cancel();
    }
}
