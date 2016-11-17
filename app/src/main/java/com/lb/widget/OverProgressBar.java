package com.lb.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.lb.demoproject.R;

/**
 * 集成了progress和seekbar功能，支持水平和竖直布局，支持反向进度
 * Created by LiuBo on 2016-09-14.
 * @attr ref R.styleable#OverProgressBar_visibility
 * @attr ref R.styleable#OverProgressBar_reverse
 * @attr ref R.styleable#OverProgressBar_thumbColor
 * @attr ref R.styleable#OverProgressBar_progressColor
 * @attr ref R.styleable#OverProgressBar_progressBackgroundColor
 * @attr ref R.styleable#OverProgressBar_thumbSize
 * @attr ref R.styleable#OverProgressBar_progressWidth
 * @attr ref R.styleable#OverProgressBar_progressBackgroundWidth
 */
public class OverProgressBar extends View {
    private static final boolean DEBUG = false;
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    public OverProgressBar(Context context) {
        this(context, null);
    }

    public OverProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //绘制当前位置的指示点
    private Drawable mThumbDrawable;
    //绘制进度条
    private Drawable mProgressDrawable;
    //绘制进度条背景
    private Drawable mBackgroundDrawable;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTextWidth;

    private static final int DEF_COLOR = 0xFF28AAE4;

    private int mProgress;
    /**
     * 设置进度
     */
    public void setProgress(int progress) {
        if (mProgress == progress || progress > mMaxProgress || progress < mMinProgress || isTouchOn) {
            return;
        }
        this.mProgress = progress;
        if (isMeasured()) {
            mCurrentCoorAbsLength = progressToCoorLength(mProgress);
            invalidate();
        }
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * 布局方向
     * @param oritation {@link #HORIZONTAL} {@link #VERTICAL}
     */
    public void setOritation(int oritation) {
        if (oritation == VERTICAL || oritation == HORIZONTAL) {
            this.mOritation = oritation;
        }
    }

    /**
     * 进度方向，默认是左低右高（上低下高）
     * @param reverse true 置反，变为左高右低
     */
    public void setReverse(boolean reverse) {
        this.mReverse = reverse;
    }

    /**
     * 指示器的直径
     * @param size px
     */
    public void setThumbSize(int size) {
        if (size > 0) {
            this.mThumbSize = size;
            mPaint.setTextSize(size * 0.6F);
            mTextWidth = (int) (mPaint.measureText("00%") + 0.5F);
        }
    }

    /**
     * 设置背景进度的宽度
     * @param width px
     */
    public void setProgressBackgroudWidth(int width) {
        this.mProgressBarBackgroundSize = width;
    }

    /**
     * 设置进度条的宽度
     * @param width px
     */
    public void setProgressBarWidth(int width) {
        this.mProgressBarSize = width;
    }

    private Rect mTempRect = new Rect();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initIfNecessary();

        if (mBackgroundDrawable != null) {
            getBackgroundBound(mTempRect);
            mBackgroundDrawable.setBounds(mTempRect);
            mBackgroundDrawable.draw(canvas);
        }

        if (mProgressDrawable != null) {
            getProgressBound(mTempRect);
            mProgressDrawable.setBounds(mTempRect);
            mProgressDrawable.draw(canvas);
        }

        if (mThumbDrawable != null) {
            getThumbBound(mTempRect);
            mThumbDrawable.setBounds(mTempRect);
            mThumbDrawable.draw(canvas);
            drawThumbText(canvas);
        }
    }

    private void drawThumbText(Canvas canvas) {
        getThumbTextBound(mTempRect);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = mTempRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2;

        canvas.drawText(getProgress() + "%", mTempRect.centerX(), baseline, mPaint);
    }

    /**
     * 设置绘制背景的drawable
     */
    public void setProgressBackgroundDrawable(OverDrawable backgroundDrawable) {
        this.mBackgroundDrawable = backgroundDrawable;
        if (backgroundDrawable != null) {
            backgroundDrawable.setCallback(this);
        }
    }

    /**
     * 设置绘制进度条的drawable
     */
    public void setProgressDrawable(OverDrawable progressDrawable) {
        this.mProgressDrawable = progressDrawable;
        if (progressDrawable != null) {
            progressDrawable.setCallback(this);
        }
    }

    /**
     * 设置绘制指示器的drawable
     */
    public void setThumbDrawable(OverDrawable thumbDrawable) {
        this.mThumbDrawable = thumbDrawable;
        if (thumbDrawable != null) {
            thumbDrawable.setCallback(this);
        }
    }

    /**
     * 手势移动与thumb移动的比例
     * @param f 1F代表 手指在屏幕上移动L, thumb移动 L * f
     */
    public void setTrigFac(float f) {
        this.mTrigFac = f;
    }

    /**
     * 设置触发间隔
     * @param s px
     */
    public void setTrigSpace(int s) {
        mTrigSpace = s;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
    }

    private int mTouchSlop;
    private float mTrigFac = 1F;//手势移动与thumb移动的比例
    private int mTrigSpace = 2;//触发进度调整的间隔

    private boolean isTouchOn;
    private boolean mMoveAction;
    private float mLastLocation;
    //FIXME 点击效果未做
    private float mDownLocation;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || mThumbDrawable == null) {
            setPressed(false);
            return false;
        }
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastLocation = getLocation(event);
                mDownLocation = mLastLocation;
                mMoveAction = false;
                isTouchOn = true;
                setPressed(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float t = getLocation(event);
                if (mMoveAction) {
                    float dl = (mLastLocation - t) * getReverseSign();
                    if (Math.abs(dl) >= mTrigSpace) {
                        handleMoveAction(dl, t, false);
                    }
                } else {
                    if (Math.abs(t - mLastLocation) >= mTouchSlop) {
                        mMoveAction = true;
                        mLastLocation = t;
                        setMoveAction(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mMoveAction) {
                    handleMoveAction((mLastLocation - getLocation(event)) * getReverseSign(), getLocation(event), true);
                }
                setMoveAction(false);
                setPressed(false);
                mMoveAction = false;
                isTouchOn = false;
                break;
        }
        invalidate();
        return true;
    }
    protected float getLocation(MotionEvent e) {
        return  isVertical() ? e.getY() : e.getX();
    }

    private void setMoveAction(boolean action) {
        ViewParent parent = getParent();
        if (parent == null) return;
        parent.requestDisallowInterceptTouchEvent(action);
    }

    private void handleMoveAction(float dcoor, float t, boolean finish) {
        int clen = mCurrentCoorAbsLength;
        clen = (int) (clen - dcoor * mTrigFac);
        if (clen < 0) {
            clen = 0;
        } else if (clen > mBarLength) {
            clen = mBarLength;
        } else {
            mLastLocation = t;
        }
        notifyProgressChange(coorLengthToProgress(clen), finish);
        mCurrentCoorAbsLength = clen;
    }

    private void notifyProgressChange(int progress, boolean finish) {
        if (mProgress != progress || finish) {
            if (null != mProgressChangeListener) {
                mProgressChangeListener.progressChangeListener(progress, finish);
                mProgress = progress;
            }
        }
    }

    private boolean mReverse;
    private void init(Context context, AttributeSet attrs) {
        float ds = getResources().getDisplayMetrics().density;
        mProgressBarBackgroundSize = Math.round(ds * mProgressBarBackgroundSize);
        mProgressBarSize = Math.round(ds * mProgressBarSize);
        mThumbSize = Math.round(ds * mThumbSize);
        mTrigSpace = Math.round(ds * mTrigSpace);

        ThumbDrawable thumbDrawable = new ThumbDrawable();
        ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        ProgressBackgroundDrawable progressBackgroundDrawable = new ProgressBackgroundDrawable();

        thumbDrawable.setCallback(this);
        thumbDrawable.setNormalColor(DEF_COLOR);
        progressBarDrawable.setCallback(this);
        progressBarDrawable.setNormalColor(DEF_COLOR);
        progressBackgroundDrawable.setCallback(this);
        progressBackgroundDrawable.setNormalColor(DEF_COLOR);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OverProgressBar);
            mOritation = ta.getInt(R.styleable.OverProgressBar_oritation, 0);
            mReverse = ta.getBoolean(R.styleable.OverProgressBar_reverse, false);
            mThumbSize = ta.getDimensionPixelSize(R.styleable.OverProgressBar_thumbSize, mThumbSize);
            mProgressBarSize = ta.getDimensionPixelSize(R.styleable.OverProgressBar_progressWidth, mProgressBarSize);
            mProgressBarBackgroundSize = ta.getDimensionPixelSize(R.styleable.OverProgressBar_progressBackgroundWidth, mProgressBarBackgroundSize);

            thumbDrawable.setColorStateList(ta.getColorStateList(R.styleable.OverProgressBar_thumbColor));
            thumbDrawable.setNormalColor(ta.getColor(R.styleable.OverProgressBar_thumbColor, DEF_COLOR));

            progressBarDrawable.setColorStateList(ta.getColorStateList(R.styleable.OverProgressBar_progressColor));
            progressBarDrawable.setNormalColor(ta.getColor(R.styleable.OverProgressBar_progressColor, DEF_COLOR));

            progressBackgroundDrawable.setColorStateList(ta.getColorStateList(R.styleable.OverProgressBar_progressBackgroundColor));
            progressBackgroundDrawable.setNormalColor(ta.getColor(R.styleable.OverProgressBar_progressBackgroundColor, DEF_COLOR));

            ta.recycle();
        }

        mThumbDrawable = thumbDrawable;
        mProgressDrawable = progressBarDrawable;
        mBackgroundDrawable = progressBackgroundDrawable;

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();

        mPaint.setTextAlign(Paint.Align.CENTER);
        setThumbSize(mThumbSize);
    }

    private int mMaxProgress = 100;
    private int mMinProgress = 1;

    //DP
    private int mProgressBarBackgroundSize = 1;
    private int mProgressBarSize = 1;
    private int mThumbSize = 14;

    private int mOritation = HORIZONTAL;
    private boolean isVertical() {
        return mOritation == 0;
    }

    private int mBarLength;
    private void initIfNecessary() {
        if (mBarLength == 0) {
            if (isVertical()) {
                mBarLength = getHeight() - mThumbSize - getPaddingTop() - getPaddingBottom();
            } else {
                mBarLength = getWidth() - mThumbSize - getPaddingLeft() - getPaddingRight();
            }

            mCurrentCoorAbsLength = progressToCoorLength(mProgress);
        }
    }

    private int progressToCoorLength(int progress) {
        return (int) (mBarLength * progress / (mMaxProgress * 1F));
    }
    private int coorLengthToProgress(int len) {
        if (len <= 0 || mBarLength == 0) {
            return mMinProgress;
        }
        int p = mMaxProgress * len / mBarLength;
        if (p < mMinProgress) {
            p = mMinProgress;
        } else if (p > mMaxProgress) {
            p = mMaxProgress;
        }
        return p;
    }

    private int mCurrentCoorAbsLength = 0;
    private void getBackgroundBound(Rect rect) {
        if (isVertical()) {
            int left = getWidth() / 2 - mProgressBarBackgroundSize / 2;
            rect.set(left, getPaddingTop() + getThumbRadiu(), left + mProgressBarBackgroundSize, getHeight() - getPaddingBottom() - getThumbRadiu());
        } else {
            int top = getHeight() / 2 - mProgressBarBackgroundSize / 2;
            rect.set(getPaddingLeft() + getThumbRadiu(), top, getWidth() - getPaddingRight() - getThumbRadiu(), top + mProgressBarBackgroundSize);
        }
    }
    private void getProgressBound(Rect rect) {
        if (isVertical()) {
            int left = getWidth() / 2 - mProgressBarSize / 2;
            if (mReverse) {
                rect.set(left, getCoorOffset() - getThumbRadiu(), left + mProgressBarSize, getHeight() - getPaddingBottom() - getThumbRadiu());
            } else {
                rect.set(left, getPaddingTop() + getThumbRadiu(), left + mProgressBarSize, getCoorOffset() + getThumbRadiu());
            }
        } else {
            int top = getHeight() / 2 - mProgressBarSize / 2;
            if (mReverse) {
                rect.set(getCoorOffset() - getThumbRadiu(), top, getWidth() - getPaddingRight() - getThumbRadiu(), top + mProgressBarSize);
            } else {
                rect.set(getPaddingLeft() + getThumbRadiu(), top, getCoorOffset() + getThumbRadiu(), top + mProgressBarSize);
            }
        }
    }
    private void getThumbBound(Rect rect) {
        int tp = getCoorOffset();
        if (isVertical()) {
            int left = getWidth() / 2 - getThumbRadiu();
            rect.set(left, tp, left + mThumbSize, tp + mThumbSize * getReverseSign());
        } else {
            int top = getHeight() / 2 - getThumbRadiu();
            rect.set(tp, top, tp + mThumbSize * getReverseSign(), top + mThumbSize);
        }
    }
    private void getThumbTextBound(Rect rect) {
        getThumbBound(rect);
        if (isVertical()) {
            rect.offset((int) (mTextWidth*1.1F), 0);
        } else {
            rect.offset(0, (int) (mThumbSize*1F));
            int centerWidth = mTextWidth / 2 + 1;
            if (rect.centerX() - centerWidth < 0) {
                rect.offset(centerWidth - rect.centerX(), 0);
            } else if (rect.centerX() + centerWidth > getMeasuredWidth()) {
                rect.offset(getMeasuredWidth() - rect.centerX() - centerWidth, 0);
            }
        }
    }


    private int getThumbRadiu() {
        return mThumbSize / 2;
    }
    private int getReverseSign() {
        return mReverse ? -1 : 1;
    }

    private int getCoorOffset() {
        if (isVertical()) {
            return mReverse ? (getHeight() - getPaddingBottom() - mCurrentCoorAbsLength)
                    : (getPaddingTop() + mCurrentCoorAbsLength);
        } else {
            return mReverse ? (getWidth() - getPaddingRight() - mCurrentCoorAbsLength)
                    : (getPaddingLeft() + mCurrentCoorAbsLength);
        }
    }

    private boolean isMeasured() {
        return getWidth() != 0;
    }

    private class ThumbDrawable extends OverDrawable {
        public ThumbDrawable() {
            setCallback(OverProgressBar.this);
        }
        @Override
        public void draw(Canvas canvas) {
            updatePaintColor(OverProgressBar.this);
            Rect rect = getBounds();
            if (DEBUG)
                mPaint.setAlpha(90);
            canvas.drawCircle(rect.centerX(), rect.centerY(), Math.abs(rect.width()) / 2, mPaint);
            if (DEBUG) {
                canvas.drawCircle(rect.centerX(), rect.centerY(), 2, mPaint);
            }
        }
    }
    private class ProgressBackgroundDrawable extends OverDrawable {
        public ProgressBackgroundDrawable() {
            setCallback(OverProgressBar.this);
        }
        @Override
        public void draw(Canvas canvas) {
            updatePaintColor(OverProgressBar.this);
            canvas.drawRect(getBounds(), mPaint);
        }
    }
    private class ProgressBarDrawable extends OverDrawable {
        public ProgressBarDrawable() {
            setCallback(OverProgressBar.this);
        }
        @Override
        public void draw(Canvas canvas) {
            updatePaintColor(OverProgressBar.this);
            canvas.drawRect(getBounds(), mPaint);
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        invalidate();
    }

    /**
     * 用于扩展，自定义绘制方式，或者动画效果
     */
    public static abstract class OverDrawable extends Drawable {
        protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        protected int mNormalColor;
        public void setNormalColor(int color) {
            this.mNormalColor = color;
        }

        public void updatePaintColor(View v) {
            mPaint.setColor(getColor(v.getDrawableState()));
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

        private ColorStateList colorList;
        public void setColorStateList(ColorStateList list) {
            this.colorList = list;
        }
        public int getColor(int[] state) {
            if (state == null || colorList == null) {
                return mNormalColor;
            }
            return colorList.getColorForState(state, mNormalColor);
        }
        @Override
        protected boolean onStateChange(int[] state) {
            return true;
        }
        @Override
        public boolean isStateful() {
            return true;
        }
    }

    private OnProgressChangeListener mProgressChangeListener;

    /**
     * 设置监听回调
     */
    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        mProgressChangeListener = listener;
    }
    public interface OnProgressChangeListener {
        void progressChangeListener(int progress, boolean isFinished);
    }
}
