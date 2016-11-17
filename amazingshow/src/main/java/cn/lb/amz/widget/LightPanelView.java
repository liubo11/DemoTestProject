package cn.lb.amz.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuBo on 2016-09-20.
 */
public class LightPanelView extends View {
    public LightPanelView(Context context) {
        this(context, null);
    }

    public LightPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        mDrawingHelpers = new ArrayList<>();
        mDrawingHelpers.add(new RgbDrawing());
        mDrawingHelpers.add(new WramDrawing());
        mDrawingHelpers.add(new TaperDrawing());
        mDrawingHelpers.add(new WhiteDrawing());
    }

    private int mWidth;
    private int mHeight;
    private Rect mTouchCtrlRect;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initIfNecessary();
        mDrawingHelpers.get(mCurrentIdx).draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDrawingHelpers.get(mCurrentIdx).touch(event);
    }

    /*
     * true init成功
     */
    private boolean initIfNecessary() {
        if (isLayouted()) {
            return true;
        }

        final int w = getMeasuredWidth();
        final int h = getMeasuredHeight();
        if (w == 0 || h == 0) {
            return false;
        }
        mWidth = w;
        mHeight = h;

        //注意padding不能设置太大
        mTouchCtrlRect = new Rect(getPaddingLeft(), getPaddingTop(), mWidth - getPaddingRight(), mHeight - getPaddingBottom());

        for (BaseDrawingHelper helper : mDrawingHelpers) {
            helper.initHelper(mTouchCtrlRect, this);
        }
        setCurValueDirectly(getCurrentValue());
        return true;
    }

    public static int IDX_RGB = 0;
    public static int IDX_WARM = 1;
    public static int IDX_TAPER = 2;
    public static int IDX_WHITE = 3;
    private int mCurrentIdx = 0;
    private List<BaseDrawingHelper> mDrawingHelpers;

    public int getCurrentIdx() {
        return mCurrentIdx;
    }

    public int getCurrentValue() {
        return mDrawingHelpers.get(mCurrentIdx).getValue();
    }

    public void setCurrentIdx(int idx) {
        if (mCurrentIdx == idx) return;
        if (idx >= 0 && idx < mDrawingHelpers.size()) {
            mCurrentIdx = idx;
        }
        invalidate();
    }

    public void setCurrentValue(int v) {
        if (mDrawingHelpers.get(mCurrentIdx).getValue() == v) return;
        mDrawingHelpers.get(mCurrentIdx).setValue(v);
        invalidate();
    }

    /**
     * 直接设置当前value
     */
    public void setCurValueDirectly(int v) {
        mDrawingHelpers.get(mCurrentIdx).setValue(v);
    }

    public void setValue(int idx, int value) {
        if (idx >=0 && idx < mDrawingHelpers.size()) {
            if (mDrawingHelpers.get(idx).getValue() == value) return;
            mDrawingHelpers.get(idx).setValue(value);
            invalidate();
        }
    }

    public void setMinValue(int idx, int min) {
        BaseDrawingHelper helper = mDrawingHelpers.get(idx);
        if (helper instanceof LinearDrawing) {
            ((LinearDrawing)helper).setMinValue(min);
        }
    }
    public void setMaxValue(int idx, int max) {
        BaseDrawingHelper helper = mDrawingHelpers.get(idx);
        if (helper instanceof LinearDrawing) {
            ((LinearDrawing)helper).setMaxValue(max);
        }
    }


    public int addDrawing(BaseDrawingHelper helper) {
        mDrawingHelpers.add(helper);
        if (isLayouted()) {
            helper.initHelper(mTouchCtrlRect, this);
        }
        return mDrawingHelpers.size() - 1;
    }

    public boolean isLayouted() {
        return mWidth != 0 && mHeight != 0;
    }

    public void onValueChangeListener(int idx, ValueChangeListener listener) {
        if (idx >= mDrawingHelpers.size() || idx < 0) {
            return;
        }
        mDrawingHelpers.get(idx).setValueChangeListner(listener);
    }


    private class RgbDrawing extends BaseDrawingHelper {
        private float whiteRadius;

        private Paint mCenterPaint;
        private Paint mRgbPaint;
        private Paint mWhitePaint;

        private int mCenterCircleSize = 10;
        private int mStrokeSize = 2;
        private int mStrokeColor = Color.WHITE;

        //触发环更改颜色的内圈半径 与圆半径的比
        private float mInnerPositionScale = 0.25F;
        //当环滑动到内圈时，白色是环看不清楚，因此使用暗色
        private int mInnerStrokeColor = 0xFF333333;

        @Override
        void initHelper(Rect rect, View v) {
            super.initHelper(rect, v);
            mStrokeSize = Math.round(v.getResources().getDisplayMetrics().density * mStrokeSize);
            mCenterCircleSize = Math.round(v.getResources().getDisplayMetrics().density * mCenterCircleSize);
            whiteRadius = Math.min(rect.width(), rect.height()) / 2;

            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setStyle(Paint.Style.STROKE);
            mCenterPaint.setStrokeWidth(mStrokeSize);
            mCenterPaint.setColor(mStrokeColor);

            mRgbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRgbPaint.setShader(buildRgbShader(rect));
            mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mWhitePaint.setShader(buildWhiteCircleShader(rect, whiteRadius));

            getCoorByRecord(mCenterPoint);
        }

        @Override
        public void drawBackground(Canvas canvas) {
            super.drawBackground(canvas);
            canvas.save();
            canvas.rotate(-90, rect.centerX(), rect.centerY());
            canvas.drawPaint(mRgbPaint);
            canvas.restore();
            canvas.drawPaint(mWhitePaint);
        }

        private PointF mCenterPoint = new PointF();
        @Override
        public void drawIndic(Canvas canvas) {
            super.drawIndic(canvas);
            if (isValid() && attacher.isEnabled()) {
                getCoorByRecord(mCenterPoint);
                float l = (float) Math.hypot(mCenterPoint.x - mWidth / 2, mCenterPoint.y - mHeight / 2);
                if (l < whiteRadius * mInnerPositionScale) {
                    mCenterPaint.setColor(mInnerStrokeColor);
                } else {
                    mCenterPaint.setColor(mStrokeColor);
                }
                canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mCenterCircleSize, mCenterPaint);
            }
        }

        public void getCoorByRecord(PointF pointF) {
            pointF.x = downX;
            pointF.y = downY;
        }

        public void getCoorByColor() {
            final int color = value;
            float []hsv = new float[]{0F,0F,0F};
            Color.colorToHSV(color, hsv);
            float ra = hsv[1] * whiteRadius;
            float angle = 270 - hsv[0];
            if (angle < 0) {
                angle = 360 + angle;
            }
            double radiansAngle = Math.toRadians(angle);
            downX = (int) (Math.cos(radiansAngle) * ra + rect.centerX());
            downY = (int) (Math.sin(radiansAngle)  * ra+ rect.centerY());
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return touchRgbValue(event, whiteRadius);
        }
        private boolean touchRgbValue(MotionEvent event, float r) {
            float x = event.getX();
            float y = event.getY();
            int action = event.getAction();
            int v;

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    return true;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    if (!mTouchActive) {
                        return false;
                    }
                    v = coordToColor(x, y, r);
                    break;
                default:
                    return false;
            }
            notifyListener(v, action == MotionEvent.ACTION_UP);
            value = v;
            attacher.invalidate();
            return true;
        }

        /**
         * 通过坐标计算得到颜色
         */
        int coordToColor(float x, float y, final float refeRadius) {
            int color;

            x = x - rect.centerX();
            y = y - rect.centerY();

            float degAngle = (float) (Math.toDegrees(getAngle(Math.atan2(y, x))));
            if (degAngle < 90) {
                degAngle += 270;
            } else {
                degAngle -= 90;
            }

            degAngle = formatPos(degAngle);

            //保证全部颜色都选得到
            double ra = Math.hypot(x , y);
            if (ra > refeRadius) {
                ra = refeRadius;
            }
            color = Color.HSVToColor(new float[]{degAngle, (float) (ra / refeRadius), 1F});

            return color;
        }
        private float formatPos(float f) {
            return Math.round(f * 10) / 10f;
        }

        @Override
        public void setValue(int v) {
            if (v != value) {
                value = v;
                if (isValid()) {
                    getCoorByColor();
                    attacher.invalidate();
                }
            }
        }
    }

    private class WramDrawing extends LinearDrawing {
        private Paint mPaint;
        private Paint mIndicPaint;
        private int mCenterCircleSize = 10;
        private int mStrokeSize = 4;
        @Override
        void initHelper(Rect rect, View v) {
            super.initHelper(rect, v);
            mStrokeSize = Math.round(v.getResources().getDisplayMetrics().density * mStrokeSize);
            mCenterCircleSize = Math.round(v.getResources().getDisplayMetrics().density * mCenterCircleSize);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(buildLinearShader(rect, COLOR_WARM, COLOR_CLOD));

            mIndicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mIndicPaint.setStrokeWidth(mStrokeSize);
            mIndicPaint.setStyle(Paint.Style.STROKE);
            mIndicPaint.setColor(COLOR_WHITE);

            downX = rect.centerX();
            downY = rect.centerY();
        }

        @Override
        public void drawBackground(Canvas canvas) {
            super.drawBackground(canvas);
            canvas.drawPaint(mPaint);
        }

        @Override
        public void drawIndic(Canvas canvas) {
            super.drawIndic(canvas);
            if (isValid() && attacher.isEnabled()) {
                canvas.drawCircle(downX, downY, mCenterCircleSize, mIndicPaint);
            }
        }

        @Override
        public void setValue(int v) {
            super.setValue(correctRangeValue(v, minValue, maxValue));
            if (isValid()) {
                downY = (int) (totalLength * value * 1F / (maxValue - minValue) + rect.top);
                attacher.invalidate();
            }
        }

        @Override
        protected int coordToValue(int v, float x, float y) {
            float dv = y - rect.top;

            float f = dv / totalLength;
            v = correctRangeValue((int) ((maxValue - minValue) * f + minValue), minValue, maxValue);

            if (isValid()) {
                attacher.invalidate();
            }
            return v;
        }
    }

    protected class TaperDrawing extends LinearDrawing {
        private Paint mPaint;
        @Override
        void initHelper(Rect rect, View v) {
            super.initHelper(rect, v);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        public void drawBackground(Canvas canvas) {
            super.drawBackground(canvas);
            canvas.save();
            canvas.rotate(-90, rect.centerX(), rect.bottom);
            if (isValid() && attacher.isEnabled()) {
                mPaint.setShader(buildTaperColorShader(rect, getLightAngle(value, maxValue - minValue), coldLightColor));
            } else {
                mPaint.setColor(coldLightColor);
                mPaint.setShader(null);
            }
            canvas.drawPaint(mPaint);
            canvas.restore();
        }

        @Override
        protected int coordToValue(int v, float x, float y) {
            if (isValid()) {
                attacher.invalidate();
            }
            return super.coordToValue(v, x, y);
        }

        private int maxTaperAngle = 90;//115;
        private int minTaperAngle = 15;
        private int coldLightColor = 0xFF28AAE5;
        //根据当前的亮度值 得到相应的角度
        private int getLightAngle(int value, int vl) {
            float scale = value / (float) vl;
            return (int) (scale * (maxTaperAngle - minTaperAngle) + minTaperAngle);
        }
    }

    private final int COLOR_WARM = 0xFFFFE487;
    private final int COLOR_WHITE = Color.WHITE;
    private final int COLOR_CLOD = 0xFF3279FD;

    protected class WhiteDrawing extends LinearDrawing {
        private Paint mPaint;
        private int topColor = COLOR_WHITE;
        private int bottomColor = COLOR_CLOD;
        @Override
        void initHelper(Rect rect, View v) {
            super.initHelper(rect, v);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(buildLinearShader(rect, topColor, bottomColor));
        }

        @Override
        public void drawBackground(Canvas canvas) {
            super.drawBackground(canvas);
            canvas.drawPaint(mPaint);
        }
    }

    public static class LinearDrawing extends BaseDrawingHelper {
        protected int trigSpace;
        protected int downCoor;

        protected int minValue;
        protected int maxValue = 100;
        protected int totalLength;

        final public void setMinValue(int min) {
            minValue = min;
        }
        final public void setMaxValue(int max) {
            if (max > minValue) {
                maxValue = max;
            }
        }

        @Override
        void initHelper(Rect rect, View v) {
            super.initHelper(rect, v);
            totalLength = rect.height();

            int range = maxValue - minValue;
            if (range > 0) {
                trigSpace = totalLength / range;
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return touchLinearValue(event);
        }

        protected boolean touchLinearValue(MotionEvent event) {
            float y = event.getY();
            float x = event.getX();
            int action = event.getAction();
            int v = value;
            if (!mTouchActive) {
                return false;
            }
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    downCoor = (int) y;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_MOVE:
                    v = coordToValue(v, x, y);
                    notifyListener(v, action == MotionEvent.ACTION_UP);
                    value = v;
                    break;
                default:
                    return false;
            }
            return true;
        }

        protected int coordToValue(int v, float x, float y) {
            int dv = (int) (downCoor - y);
            if (Math.abs(dv) >= trigSpace) {
                if (dv < 0) {
                    v--;
                } else {
                    v++;
                }
                downCoor = (int) y;
            }
            return correctRangeValue(v, minValue, maxValue);
        }

        @Override
        protected boolean isValid() {
            return super.isValid() && trigSpace != 0;
        }
    }

    public static abstract class BaseDrawingHelper {
        protected Rect rect;
        protected View attacher;
        void initHelper(Rect rect, View v) {
            this.rect = new Rect(rect);
            this.attacher = v;
            downX = rect.centerX();
            downY = rect.centerY();
        }

        protected int value;
        protected ValueChangeListener listner;
        public void setValueChangeListner(ValueChangeListener listner) {
            this.listner = listner;
        }
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }

        public void drawBackground(Canvas canvas) {

        }
        public void drawIndic(Canvas canvas) {

        }

        protected boolean mTouchActive;
        protected boolean mTouchOn;
        protected int downX;
        protected int downY;
        boolean touch(MotionEvent event) {
            if (!(isValid() && attacher.isEnabled())) {
                mTouchActive = false;
                mTouchOn = false;
                return false;
            }
            final int action = event.getAction();
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mTouchActive = rect.contains(x, y);
                    mTouchOn = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mTouchActive) {
                        downX = correctRangeValue(x, rect.left, rect.right);
                        downY = correctRangeValue(y, rect.top, rect.bottom);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mTouchActive && rect.contains(x, y)) {
                        downX = x;
                        downY = y;
                    }
                    mTouchOn = false;
                    break;
            }
            return onTouchEvent(event);
        }
        void draw(Canvas canvas) {
            drawBackground(canvas);
            drawIndic(canvas);
        }
        protected void notifyListener(int v, boolean finish) {
            if (v != value || finish) {
                if (listner != null) {
                    listner.onValueChange(v, finish);
                }
            }
        }
        protected boolean isValid() {
            return attacher != null && rect != null && rect.width() > 0 && rect.height() > 0;
        }

        //得到连续的角度变换
        protected double getAngle(double angle){
            if(angle < 0){
                angle = (float) (2* Math.PI + angle);
            }

            return 2* Math.PI - angle;
        }

        protected int correctRangeValue(int v, int min, int max) {
            if (v < min) {
                return min;
            }
            if (v > max) {
                return max;
            }
            return v;
        }

        public void setValue(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }
    }


    private Shader buildRgbShader(Rect rect) {
        return new SweepGradient(rect.centerX(), rect.centerY(), buildHueColorArray(), null);
    }
    private Shader buildWhiteCircleShader(Rect rect, float radiu) {
        return new RadialGradient(rect.centerX(), rect.centerY(),
                radiu, 0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);
    }

    private Shader buildTaperColorShader(Rect rect, int angle, int color) {
        int[] hue = new int[361];
        float[] hsv = new float[3];

        Color.colorToHSV(color, hsv);//0xff28aae5

        int adColor = Color.HSVToColor(hsv);

        for (int i = 0; i < angle+1; i++) {
            float s = hsv[1] * i / angle;
            hue[i] = Color.HSVToColor(new float[]{hsv[0], s, hsv[2]});
        }

        for (int i = angle+1; i < 361 - angle; i++) {
            hue[i] = adColor;
        }

        for (int i = 361 - angle; i < 361; i++) {
            hue[i] = hue[360-i];
        }

        return new SweepGradient(rect.centerX(), rect.bottom, hue, null);
    }

    private Shader buildLinearShader(Rect rect, int topColor, int bottomColor) {
        return new LinearGradient(0, rect.top, 0, rect.bottom, topColor,
                bottomColor, Shader.TileMode.CLAMP);
    }

    private int[] buildHueColorArray() {
        int[] hue = new int[361];
        int count = 0;
        for (int i = hue.length - 1; i >= 0; i--, count++) {
            hue[count] = Color.HSVToColor(new float[]{i, 1F, 1F});
        }
        return hue;
    }

    public interface ValueChangeListener {
        void onValueChange(int value, boolean isFinished);
    }


    private BlurDrawable mBlurDrawable;
    public BlurDrawable getBlurDrawable() {
        return mBlurDrawable = new BlurDrawable(this);
    }

    public static class BlurDrawable extends Drawable {
        private LightPanelView lightPanelView;
        private Bitmap mBlurBitmap;
        public BlurDrawable(LightPanelView lightPanelView) {
            this.lightPanelView = lightPanelView;
        }
        private View attatcher;
        public void attachView(View v) {
            attatcher = v;
            v.setBackgroundDrawable(this);
        }
        @Override
        public void draw(Canvas canvas) {
            /*if (mBlurBitmap != null) {
                //canvas.drawBitmap(mBlurBitmap, 0, lightPanelView.getMeasuredHeight() - attatcher.getMeasuredHeight(), null);
            } else if (lightPanelView.saveBitmap != null) {
                CLib.ClBlurBitmap(lightPanelView.saveBitmap, 80);
                mBlurBitmap = lightPanelView.saveBitmap;
                //canvas.drawBitmap(mBlurBitmap, 0, 0, null);
            }*/

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setAlpha(150);

            canvas.drawRoundRect(new RectF(0, 0, attatcher.getMeasuredWidth(), attatcher.getMeasuredHeight()), 10, 10, paint);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }
}
