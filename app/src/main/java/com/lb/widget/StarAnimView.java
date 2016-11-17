package com.lb.widget;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-10-25.
 * 如果有其他复杂的动画，可以考虑用SurfaceView
 */
public class StarAnimView extends View {
    public StarAnimView(Context context) {
        this(context, null);
    }

    private Drawable mBackgroundDrawable;
    public StarAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundDrawable = getResources().getDrawable(R.drawable.empty_bk_stars);
        startAnim();
    }

    private float mOffsetWidthScale = 0.6396F;
    private float mOffsetHeightScale = 0.2083F;

    private Rect mBundleRect;

    private boolean initDrawableBound() {
        if (getMeasuredHeight() == 0 || getMeasuredWidth() == 0) {
            return false;
        }
        int size = getMeasuredWidth();
        int topOffset = (int) (mOffsetHeightScale * getMeasuredHeight());
        int leftOffset = (int) (mOffsetWidthScale * getMeasuredWidth());

        int newTop = topOffset - size / 2;
        int newLeft = leftOffset - size / 2;

        Rect rect = new Rect();
        rect.set(0, 0, size, size);
        rect.offsetTo(newLeft, newTop);

        mBundleRect = rect;
        return true;
    }

    private float mRotateDeg = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBackgroundDrawable == null || !initDrawableBound() || mBundleRect == null) {
            return;
        }
        canvas.save();
        canvas.rotate(mRotateDeg, mOffsetWidthScale * getMeasuredWidth(), mOffsetHeightScale * getMeasuredHeight());
        mBackgroundDrawable.setBounds(mBundleRect);
        mBackgroundDrawable.draw(canvas);
        canvas.restore();
    }

    private final int ROTATE_ANIM_DURATION = 32 * 1000;//默认32s
    private ObjectAnimator animator;
    public void startAnim() {
        if (animator == null) {
            animator = ObjectAnimator.ofObject(this, "rotate", new FloatEvaluator(), 0F, 359F);
            animator.setDuration(ROTATE_ANIM_DURATION);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(-1);
            postInvalidate();
        }
        animator.start();
    }
    public void stopAnim() {
        if (animator != null ) {
            animator.cancel();
        }
    }

    public void setDuration(int duration) {
        if (duration > 0 && animator != null) {
            animator.setDuration(duration);
        }
    }

    public void setRotate(float rotate) {
        if (rotate == mRotateDeg) {
            return;
        }
        this.mRotateDeg = rotate;
        invalidate();
    }

    public float getRotate() {
        return mRotateDeg;
    }
}
