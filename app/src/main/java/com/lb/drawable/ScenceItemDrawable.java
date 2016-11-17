package com.lb.drawable;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.StateSet;
import android.view.View;

/**
 * Created by LiuBo on 2016-09-22.
 */
public class ScenceItemDrawable extends Drawable {
    private int[] colors;
    private int height;
    private int width;

    private Paint mPaint;

    private RectF rectF;
    private RectF rectTop;
    private RectF rectBottom;
    private RectF rectTemp;
    private int shadowOffset = 1;//dp
    private int strokeWidth = 3;//dp

    private int pressed = android.R.attr.state_pressed;
    private int selected = android.R.attr.state_selected;
    private int curState = 0;

    public ScenceItemDrawable(int [] colors) {
        this.colors = colors;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        rectF = new RectF();
        rectTop = new RectF();
        rectBottom = new RectF();
        rectTemp = new RectF();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public void attachView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        float density = v.getContext().getResources().getDisplayMetrics().density;
        shadowOffset = Math.round(density * shadowOffset);
        strokeWidth = Math.round(density * strokeWidth);
        v.setBackgroundDrawable(this);
    }

    @Override
    public void draw(Canvas canvas) {
        if (width == 0 || height == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();
            rectF.set(0, 0, width, height);

            rectTemp.set(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth);
        }
        if (curState == selected) {
            mPaint.setAlpha(0xff);
            float r = rectF.height() * 0.2f;
            int bg_color = 0x55ffffff;//colors[0] & 0x50ffffff; 白色30%alpha
            mPaint.setColor(bg_color);
            canvas.drawRoundRect(rectF, r, r, mPaint);
        }

        drawItem(canvas, rectTemp, colors);
    }
    private void drawItem(Canvas canvas, RectF itemRectF, int[] colors) {
        float r = itemRectF.height() * 0.2f;
        rectTop.set(itemRectF.left, itemRectF.top ,
                itemRectF.right, itemRectF.bottom- r*1.2f);
        rectBottom.set(rectTop.left, rectTop.bottom,
                rectTop.right, itemRectF.bottom);

        mPaint.setAlpha(0xff);
        mPaint.setShader(buildShader(colors));
        canvas.drawRoundRect(itemRectF, r, r, mPaint);

        canvas.save();
        canvas.clipRect(rectTop);
        mPaint.setShader(null);
        mPaint.setColor(0xbbffffff);
        canvas.drawRoundRect(itemRectF, r, r, mPaint);
        canvas.restore();

        canvas.save();
        canvas.clipRect(rectBottom);
        mPaint.setShadowLayer(1, 0, shadowOffset, 0x26000000);
        canvas.drawRect(rectTop, mPaint);
        mPaint.clearShadowLayer();

        canvas.restore();
    }
    private Shader buildShader(int [] colors) {
        return new LinearGradient(rectF.left, rectF.top,
                rectF.right, rectF.top,colors, null, Shader.TileMode.CLAMP);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }
    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        if (null != state) {
            curState = state[0];
            invalidateSelf();
            return true;
        }
        return false;
    }

    @Override
    public boolean setState(int[] stateSet) {
        int[] changeState = {0};
        if (null != stateSet) {

            for (int i = 0; i < stateSet.length; i++) {
                if (StateSet.stateSetMatches(new int[]{ selected }, stateSet)) {
                    changeState[0] = selected;
                    break;
                }
                if (StateSet.stateSetMatches(new int[]{ pressed }, stateSet)) {
                    changeState[0] = pressed;
                    break;
                }
            }
        }

        return onStateChange(changeState);
    }
}
