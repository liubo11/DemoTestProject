package cn.lb.amz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import cn.lb.amz.R;

/**
 * Created by LiuBo on 2016-11-17.
 */

public class ClockView extends ImageView {

    public ClockView(Context context) {
        super(context);
        init();
    }
    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(0x80000000);
        matrix = new Matrix();
        mRectF = new RectF();
        clockHelper = new DrawClock();

        drawable = getResources().getDrawable(R.drawable.p4);
    }

    private Matrix matrix;
    private Paint mPaint;

    private RectF mRectF;

    private DrawClock clockHelper;

    private Drawable drawable;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        postInvalidate();
        return clockHelper.touch(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!clockHelper.isValid()) {
            clockHelper.initHelper(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()), this);
        }
        clockHelper.draw(canvas);

    }

    private class DrawClock extends LightPanelView.LinearDrawing {
        @Override
        void draw(Canvas canvas) {
            super.draw(canvas);

            canvas.drawColor(Color.WHITE);
            canvas.save();

            mPaint.setColor(Color.RED);

            //canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 100, mPaint);

            drawable.setBounds(getMeasuredWidth() / 2 - 100, getMeasuredHeight() / 2 - 100, getMeasuredWidth() / 2 + 100, getMeasuredHeight() / 2 + 100);
            drawable.draw(canvas);

            float v = value / 100F;
            setPolyMatrix();
            canvas.setMatrix(matrix);
            drawable.draw(canvas);
            //canvas.scale(1F, v, 0, getMeasuredHeight() / 2);
            mPaint.setColor(0x80000000);
            //canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 100, mPaint);

            canvas.restore();
        }

        private void setPolyMatrix() {
            float v = value / 100F;
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            float[] src = new float[] {0, 0,
                    w, 0,
                    0, h,
                    w, h};
            float[] dec;
            if (value < 50) {
                dec = new float[] {0 - formatValue(value), h * v,
                        w + formatValue(value), h * v,
                        formatValue(value), h * (1-v),
                        w - formatValue(value), h * (1-v)};
            } else {
                dec = new float[] {
                        formatValue(value), h * (1-v),
                        w - formatValue(value), h * (1-v),
                        0 - formatValue(value), h * v,
                        w + formatValue(value), h * v
                };
            }

            matrix.setPolyToPoly(src, 0, dec, 0, src.length>> 1);
        }

        private int formatValue(int value) {
            return 4 * (value > 50 ? (100 - value) : value);
        }
    }

}
