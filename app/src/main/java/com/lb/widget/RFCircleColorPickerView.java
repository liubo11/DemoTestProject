package com.lb.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class RFCircleColorPickerView extends View {
	private Rect mRectTouchCtrl;
    private PointF mCenterPoint;
	private int mWhiteAreaRadius;

    private Paint mCenterPaint;
	private Paint mRgbPaint;
	private Paint mWhitePaint;

	private Shader mRgbShader;
	private Shader mWhiteAlphaShader;

    private int mWidth;
    private int mHeight;

    private int mCenterCircleSize = 10;
    private int mStrokeSize = 2;
    private int mStrokeColor = Color.WHITE;

    //触发环更改颜色的内圈半径 与圆半径的比
    private float mInnerPositionScale = 0.25F;
    //当环滑动到内圈时，白色是环看不清楚，因此使用暗色
    private int mInnerStrokeColor = 0xFF333333;

    private OnColorChangedListener mColorChangedListener;
    private int mColor;

	public RFCircleColorPickerView(Context context) {
		this(context, null);
	}

	public RFCircleColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
        float dt = context.getResources().getDisplayMetrics().density;

        mCenterCircleSize = Math.round(dt * mCenterCircleSize);
        mStrokeSize = Math.round(dt * mStrokeSize);

		mRectTouchCtrl = new Rect();

        mCenterPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        mCenterPaint.setStyle(Paint.Style.STROKE);
        mCenterPaint.setStrokeWidth(mStrokeSize);
        mCenterPaint.setColor(mStrokeColor);

        mCenterPoint = new PointF();
	}

	private int[] buildHueColorArray() {
		int[] hue = new int[361];
		int count = 0;
		for (int i = hue.length - 1; i >= 0; i--, count++) {
			hue[count] = Color.HSVToColor(new float[]{i, 1F, 1F});
		}
		return hue;
	}

    /**
     * 初始化一些静态数据， 布局相关的
     */
	private void initStaticValue() {
		if (mWidth == 0 || mHeight == 0) {
			int w = getMeasuredWidth();
			int h = getMeasuredHeight();

			if (w > 0 && h > 0) {
				mWidth = w;
				mHeight = h;
				int size = Math.min(w, h);
				mRectTouchCtrl.set((w - size) / 2, (h - size) / 2, (w + size)/2, (h + size) / 2);

				mRgbShader = new SweepGradient(mRectTouchCtrl.centerX(), mRectTouchCtrl.centerY(),
						buildHueColorArray(), null);
				mWhiteAreaRadius = (int) getDrawCircleRadiu();
				mWhiteAlphaShader = new RadialGradient(mRectTouchCtrl.centerX(), mRectTouchCtrl.centerY(),
                        mWhiteAreaRadius, 0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);

				mRgbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				mRgbPaint.setShader(mRgbShader);

				mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				mWhitePaint.setShader(mWhiteAlphaShader);
			}
		}
	}

    private float getDrawCircleRadiu() {
        return Math.min(mWidth * 0.5F, mHeight * 0.5F);
    }

    /**
     * 通过坐标计算得到颜色
     */
	private int coordToColor(float x, float y) {
		int color = 0;

		x = x - mRectTouchCtrl.centerX();
		y = y - mRectTouchCtrl.centerY();

		float degAngle = (float) (Math.toDegrees(getAngle(Math.atan2(y, x))));
		if (degAngle < 90) {
			degAngle += 270;
		} else {
			degAngle -= 90;
		}

        degAngle = formatPos(degAngle);

        //保证全部颜色都选得到
		final int refeRadius = mWhiteAreaRadius - mCenterCircleSize / 2;

		double ra = Math.sqrt(x * x + y * y);
		if (ra > refeRadius) {
			ra = refeRadius;
		}
		color = Color.HSVToColor(new float[]{degAngle, (float) (ra / refeRadius), 1F});

		return color;
	}

	//得到连续的角度变换
	private double getAngle(double angle){
		if(angle < 0){
			angle = (float) (2* Math.PI + angle);
		}

		return 2* Math.PI - angle;
	}

    /**
     * 通过颜色 获取当前坐标点
     * @param point
     */
    private void getCoorByColor(PointF point) {
        final int color = mColor;
        float []hsv = new float[]{0F,0F,0F};
        Color.colorToHSV(color, hsv);
        float ra = hsv[1] * mWhiteAreaRadius;
        float angle = 270 - hsv[0];
        if (angle < 0) {
            angle = 360 + angle;
        }
        double radiansAngle = Math.toRadians(angle);
        point.x = (float) Math.cos(radiansAngle) * ra + mRectTouchCtrl.centerX();
        point.y = (float) Math.sin(radiansAngle)  * ra+ mRectTouchCtrl.centerY();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initStaticValue();

        canvas.save();
        canvas.rotate(-90, mRectTouchCtrl.centerX(), mRectTouchCtrl.centerY());
        canvas.drawPaint(mRgbPaint);
        //canvas.drawCircle(mRectTouchCtrl.centerX(), mRectTouchCtrl.centerY(), getDrawCircleRadiu(), mRgbPaint);
        canvas.restore();
        canvas.drawPaint(mWhitePaint);

        if (isEnabled()) {
            getCoorByColor(mCenterPoint);
            float l = (float) Math.hypot(mCenterPoint.x - mWidth / 2, mCenterPoint.y - mHeight / 2);
            if (l < getDrawCircleRadiu() * mInnerPositionScale) {
                mCenterPaint.setColor(mInnerStrokeColor);
            } else {
                mCenterPaint.setColor(mStrokeColor);
            }
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mCenterCircleSize, mCenterPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }

        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                mColor = coordToColor(x, y);
                break;
            default:
                return false;
        }

        if (mColorChangedListener != null) {
            mColorChangedListener.onColorChanged(mColor, action == MotionEvent.ACTION_UP);
        }

        invalidate();
        return true;
    }

    /**
     * 设置当前颜色
     * <i>颜色的亮度将被忽略<i/>
     */
    public void setRgbValue(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 1F;//忽略亮度
        mColor = Color.HSVToColor(hsv);
        postInvalidate();
    }

    /**
     * 获取当前颜色
     */
    public int getRgbValue() {
        return mColor;
    }

    /**
     * 设置颜色监听器
     * @param listener
     */
    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.mColorChangedListener = listener;
    }
    public interface OnColorChangedListener {
        void onColorChanged(int color, boolean isFinished);
    }

    private float formatPos(float f) {
        return Math.round(f * 10) / 10f;
    }
}