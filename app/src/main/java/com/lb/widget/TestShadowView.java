package com.lb.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-11-24.
 */

public class TestShadowView extends View {
    public TestShadowView(Context context) {
        this(context, null);
    }

    public TestShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    private DrawArcView drawArcView;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return drawArcView.touch(event);
    }

    private class DrawArcView extends LightPanelView.LinearDrawing {
        public DrawArcView() {
            setMinValue(0);
            setMaxValue(360);
        }
        @Override
        public void drawBackground(Canvas canvas) {
            super.drawBackground(canvas);


            mShadowPaint.clearShadowLayer();
            mShadowPaint.setColor(0xFFFF0000);
            RectF rectF = new RectF(10, 10, 300, 300);
            canvas.drawRect(rectF, mShadowPaint);

            mShadowPaint.setShadowLayer(5,0,10,0xFF000000);
            mShadowPaint.setStyle(Paint.Style.FILL);
            mShadowPaint.setColor(0xFF000000);

            float arc = value;
            float start = 270 - arc *0.5F;

            Path path = new Path();
            path.addArc(rectF,  start , arc);
            path.lineTo(rectF.centerX(), rectF.centerY());
            path.close();
            canvas.drawPath(path, mShadowPaint);
            canvas.save();
            canvas.clipPath(path);
            //canvas.drawBitmap(bitmap, null, rectF, mShadowPaint);
            canvas.restore();
            //canvas.drawArc(rectF, 0, 90, true, mShadowPa int);
            //
        }
    }


    private int mShadowColor = 0xff000000; //0x3C000000;
    private Paint mShadowPaint;
    private Drawable mArcDrawable;
    private Drawable mBackgroundDrawable;
    private Path mPath;
    private RectF mRectF;
    private Rect mRectSrc;

    private void init(Context context, AttributeSet attrs) {
        drawArcView = new DrawArcView();

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
        mRectF = new RectF();
        mRectSrc = new Rect();

        mArcDrawable = getResources().getDrawable(R.drawable.home_server_ic_disk_usage);
        mBackgroundDrawable = getResources().getDrawable(R.drawable.home_server_ic_disk_empty);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    //相对于高度
    private final float mShadowOffset = 0.02F;

    private boolean initOrRefreshShadowPath() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width == 0 || height == 0) {
            return false;
        }

        float shadowSize = height * mShadowOffset / 2;
        float layoutShadowSize = shadowSize * 2;
        float arc = drawArcView.getValue() / 100F * 360;
        float start = 270 - arc *0.5F;

        mShadowPaint.setShadowLayer(shadowSize, 0, shadowSize, mShadowColor);
        mRectF.set(layoutShadowSize, layoutShadowSize,
                width - layoutShadowSize, height - layoutShadowSize);

        mPath.reset();
        mPath.addArc(mRectF,  start , arc);
        mPath.lineTo(mRectF.centerX(), mRectF.centerY());
        mPath.close();

        mRectSrc.set((int) mRectF.left, (int) mRectF.top, (int) mRectF.right, (int) mRectF.bottom);
        mBackgroundDrawable.setBounds(mRectSrc);
        mArcDrawable.setBounds(mRectSrc);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!drawArcView.isValid()) {
            drawArcView.initHelper(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()), this);
        }
        if (!initOrRefreshShadowPath()) {
            return;
        }

        mBackgroundDrawable.draw(canvas);

        mShadowPaint.setColor(0xffffffff);
        canvas.drawPath(mPath, mShadowPaint);

        canvas.save();
        canvas.clipPath(mPath);
        mArcDrawable.draw(canvas);
        canvas.restore();


    }
}

