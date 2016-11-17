package com.lb.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by LiuBo on 2016-08-09.
 */
public class RFLightVerticalProgressBar extends View {

    public RFLightVerticalProgressBar(Context context) {
        super(context);
        init(context);
    }
    public RFLightVerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private OnProgressChangeListener listener;
    private int mThumbAnimCancelSpace = 3000;//thumb的取消状态变化的时间
    private int mAutoInVisibleSpace = 5000;//
    private int mTrigSpace = 2;//触发进度调整的间隔
    private float mTrigFac = 0.5f;//手势移动与thumb移动的比例
    private final float mMaxThumScale = 1.6f;//thumb的放大比例
    private float mThumScale = 1;//thumb的原始比例
    private int mStrokeSize = 1;
    private boolean mDrawText = false;

    private int mUnProgressBarColor = 0x33333333;
    private int mProgressBarColor = 0xFF00CC99;
    private int mThumbColor = Color.WHITE;
    private int mThumbStrokeColor = mUnProgressBarColor;
    private int mTextColor = 0x88000000;

    private int mTouchSlop;
    private float mDownY;
    private boolean mMoveAction;
    private boolean isTouchOn;

    public void setProgressChangeListener(OnProgressChangeListener listener) {
        this.listener = listener;
    }

    private boolean isPausedProgress;

    /**
     * 暂停状态不绘制thumb 和 进度条
     * @param pause
     */
    public void setPausedStatus(boolean pause) {
        isPausedProgress = pause;
        postInvalidate();
    }
    public boolean getPausedStatus() {
        return isPausedProgress;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || isPausedProgress) {
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                mMoveAction = false;
                isTouchOn = true;
                animtorThumbSize(true);
                removeCallbacks(canFocusAnim);
                removeCallbacks(autoInvisible);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                if (mMoveAction) {
                    float dy = (y - mDownY);
                    if (Math.abs(dy) >= mTrigSpace) {
                        mThumbCoorY = (int) (mThumbCoorY - dy * mTrigFac);

                        if (mThumbCoorY < 0) {
                            mThumbCoorY = 0;
                        } else if (mThumbCoorY > mRectProgressAra.height()) {
                            mThumbCoorY = mRectProgressAra.height();
                        } else {
                            mDownY = y;
                            invalidate();
                        }
                        calculatCoorToProgress(false, true);
                    }
                } else {
                    if (Math.abs(y - mDownY) >= mTouchSlop) {
                        mMoveAction = true;
                        mDownY = y;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mMoveAction = false;
                isTouchOn = false;
                calculatCoorToProgress(true, true);
                postDelayed(canFocusAnim, mThumbAnimCancelSpace);
                postDelayed(autoInvisible, mAutoInVisibleSpace);
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    private Runnable canFocusAnim = new Runnable() {

        @Override
        public void run() {
            animtorThumbSize(false);
        }
    };
    private Runnable autoInvisible = new Runnable() {

        @Override
        public void run() {
            if (mAutoDismissEnabled) {
                setProgressVisible(false);
            }
        }
    };

    private ValueAnimator animThumbSize;
    private ValueAnimator animSetProgress;
    //是否放大
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void animtorThumbSize(boolean isAmp) {
        if (Build.VERSION.SDK_INT >= 12) {
            float start = mThumScale;
            float stop = isAmp ? mMaxThumScale : 1f;
            if (null == animThumbSize) {
                animThumbSize = new ValueAnimator();
                animThumbSize.setDuration(200);
                animThumbSize.setInterpolator(new DecelerateInterpolator());
                animThumbSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fac = (Float) animation.getAnimatedValue();
                        mThumScale = fac;
                        invalidate();
                    }
                });
            } else {
                animThumbSize.cancel();
            }
            animThumbSize.setFloatValues(start, stop);
            animThumbSize.start();
        } else {
            mThumScale = isAmp ? mMaxThumScale : 1f;
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void setProgress(int progress) {
        if (isTouchOn || progress > mMaxProgress || progress < mMinProgress) {
            return;
        }
        if (getWidth() == 0) {
            setProgressNoAnim(progress);
            return;
        }
        boolean visible = getVisibility() == View.VISIBLE;
        float destY = mRectProgressAra.height() * getAbsProgress(progress) * 1f / getTotalSteps();
        if (Math.abs(destY - mThumbCoorY) > mTrigSpace && Build.VERSION.SDK_INT >= 12 && visible) {
            float startCoord = mThumbCoorY;
            float stopCoord = destY;
            if (null == animSetProgress) {
                animSetProgress = new ValueAnimator();
                animSetProgress.setDuration(400);
                animSetProgress.setInterpolator(new DecelerateInterpolator());
                animSetProgress.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fac = (Float) animation.getAnimatedValue();
                        mThumbCoorY = fac;
                        if (animation.getAnimatedFraction() == 1f) {
                            calculatCoorToProgress(false, false);
                        }
                        invalidate();
                    }
                });
            } else {
                animSetProgress.cancel();
            }
            animSetProgress.setFloatValues(startCoord, stopCoord);
            animSetProgress.start();
        } else {
            mThumbCoorY = destY;
            mProgress = progress;
            invalidate();
        }
    }
    public void setProgressNoAnim(int progress) {
        if (isTouchOn || progress > mMaxProgress || progress < mMinProgress) {
            return;
        }
        float destY = mRectProgressAra.height() * getAbsProgress(progress) * 1f / getTotalSteps();
        mThumbCoorY = destY;
        mProgress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }
    public void setMaxProgress(int max) {
        this.mMaxProgress = max;
    }
    public void setMinProgress(int min) {
        this.mMinProgress = min;
    }
    //获取最大值和最小值的中间量
    private int getTotalSteps() {
        return mMaxProgress - mMinProgress;
    }
    private int getRelatProgress(int absProgress) {
        return absProgress + mMinProgress;
    }
    public int getAbsProgress(int relatProgress) {
        return relatProgress - mMinProgress;
    }

    public void setThumbColor(int mThumbColor) {
        this.mThumbColor = mThumbColor;
    }

    public void setThumbStrokeColor(int mThumbStrokeColor) {
        this.mThumbStrokeColor = mThumbStrokeColor;
    }

    public void setProgressBarColor(int mProgressBarColor) {
        this.mProgressBarColor = mProgressBarColor;
    }

    public void setUnProgressBarColor(int mUnProgressBarColor) {
        this.mUnProgressBarColor = mUnProgressBarColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    /**
     * 单位px
     */
    public void setStrokeSize(int mStrokeSize) {
        this.mStrokeSize = mStrokeSize;
    }

    //调用监听器
    private void calculatCoorToProgress(boolean isFinished, boolean needNotify) {
        int progress = (int) ( 1f * getTotalSteps() * mThumbCoorY / mRectProgressAra.height());
        progress = getRelatProgress(progress);

        if (progress == mProgress && !isFinished) {
            return;
        }

        mProgress = progress;
        if (needNotify && null != listener) {
            listener.progressChangeListener(mProgress, isFinished);
        }
    }

    private void init(Context context) {
        setProgressRect(context);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    private Paint mBgPaint = new Paint();
    private TextPaint mProTextPaint = new TextPaint();
    private Paint mPaint = new Paint();

    private int mMaxProgress = 100;//最大值100
    private int mMinProgress = 1;//最小值1
    private int mProgress;
    private float mThumbCoorY = 50;

    private RectF mRectProgressBg = new RectF();
    private Rect mRectProText = new Rect();
    private Rect mRectProgressAra = new Rect();
    private Rect mRectThumb = new Rect();


    private int mProHeight;//200dp
    private int mProWidth;//26dp
    private int mProBgStroke = 1;//1px

    private int mTextBaseling;

    private boolean mAutoDismissEnabled;

    private void setProgressRect(Context context) {
        if (mWitdh == 0 || mHeight == 0) {
            return;
        }
        float density = context.getResources().getDisplayMetrics().density;

        mProWidth = (int) (density * 26);
        mProHeight = mHeight;
        mStrokeSize = Math.round(density * mStrokeSize);

        int mThumbRadius = (int) (density * 6);
        int mProgressBgHeight = mProHeight;
        int mProgressAraHeight = mProHeight - mThumbRadius * 4;
        int mProgressAraWidth = Math.round(density * 1);

        int paLeft = mProWidth / 2 - mProgressAraWidth / 2;
        int paTop = (mProgressBgHeight - mProgressAraHeight) / 2;

        mTrigSpace = (int) (density * mTrigSpace);
        mRectProText.set(0, 0, mProWidth, 0);
        mRectProgressBg.set(0, 0, mProWidth, mProHeight);
        mRectProgressAra.set(paLeft, paTop, mProgressAraWidth + paLeft, paTop + mProgressAraHeight);
        mRectThumb.set(0, 0, mThumbRadius, mThumbRadius);

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);

        mProTextPaint.setAntiAlias(true);
        mProTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mProTextPaint.getFontMetricsInt();
        mTextBaseling = (mRectProText.top + mRectProText.bottom - fontMetrics.bottom - fontMetrics.top) / 2;

        mBgPaint.setAntiAlias(true);
        mBgPaint.setStrokeWidth(mProBgStroke);
    }

    private int mWitdh;
    private int mHeight;

    private void initViewSize() {
        if (mWitdh == 0 || mHeight == 0) {
            mWitdh = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            setProgressRect(getContext());
            setProgressNoAnim(mProgress);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initViewSize();
        int transX = (mWitdh - mProWidth) / 2;

        if (transX > 0) {
            canvas.save();
            canvas.translate(transX, 0);
        }

        if (mDrawText) {
            drawText(canvas, getPercentProgress());
        }
        //drawBackgroud(canvas);
        drawProgress(canvas);

        if (transX > 0) {
            canvas.restore();
        }
    }
    private void drawProgress(Canvas canvas) {
        int pointX = mRectProgressAra.width();
        int pointY = (int) mThumbCoorY;
        canvas.save();
        canvas.translate(mRectProgressAra.right, mRectProgressAra.bottom);
        canvas.rotate(180);

        mRectThumb.offsetTo(0, pointY);

        mPaint.setColor(mUnProgressBarColor);
        canvas.drawRect(0, 0, pointX, mRectProgressAra.height(), mPaint);

        if (!isPausedProgress) {
            mPaint.setColor(mProgressBarColor);
            canvas.drawRect(0, 0, pointX, pointY, mPaint);

            mPaint.setColor(mThumbStrokeColor);
            canvas.drawCircle(mRectProgressAra.width() / 2, mRectThumb.top,
                    (mRectThumb.width() + mStrokeSize) * mThumScale, mPaint);

            mPaint.setColor(mThumbColor);
            canvas.drawCircle(mRectProgressAra.width() / 2, mRectThumb.top,
                    mRectThumb.width() * mThumScale, mPaint);
        }

        canvas.restore();
    }

    @SuppressWarnings("unused")
    private void drawBackgroud(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mRectProgressAra, Region.Op.DIFFERENCE);

        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(0x33000000);
        canvas.drawRoundRect(mRectProgressBg, mProWidth / 2, mProWidth / 2, mBgPaint);

        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setColor(0x66000000);
        canvas.drawRoundRect(mRectProgressBg, mProWidth / 2, mProWidth / 2, mBgPaint);

        canvas.restore();
    }

    private void drawText(Canvas canvas, String text) {
        mProTextPaint.setColor(mTextColor);
        canvas.drawText(text, mRectProText.centerX(), mTextBaseling, mProTextPaint);
    }

    private String getPercentProgress() {
        return String.valueOf(mProgress) + "%";
    }

    public void setProgressVisible(boolean visible) {
        setVisibility(visible? View.VISIBLE: View.GONE);
        if (visible) {
            removeCallbacks(autoInvisible);
            postDelayed(autoInvisible, mAutoInVisibleSpace);
        }
    }

    public void setAutoDismiss(boolean enable) {
        mAutoDismissEnabled = enable;
    }

    public interface OnProgressChangeListener {
        void progressChangeListener(int progress, boolean isFinished);
    }
}
