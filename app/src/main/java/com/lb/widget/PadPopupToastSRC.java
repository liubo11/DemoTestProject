package com.lb.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.demoproject.R;


/**
 * Created by LiuBo on 2016-10-19.
 * 注意屏幕翻转 导致异常 尺寸问题
 */
public class PadPopupToastSRC extends PopupWindow {
    private int mDuration = 2000;

    private Handler mHandler;
    private Runnable mRunnerDismiss;

    private TextView mTextView;
    private int mHeight;
    private int mWidth;

    public PadPopupToastSRC(Context context) {
        super(context, null);
        mHandler = new Handler();
        mRunnerDismiss = new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        };

        mWidth = 1000;
        mHeight = 374;
        setWidth(mWidth);
        setHeight(mHeight);

        mTextView = buildMsgTextView(context);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(mTextView);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        setContentView(relativeLayout);

        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(R.style.PopupAnimation);
    }

    private TextView buildMsgTextView(Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(20);
        gradientDrawable.setColor(0x80000000);

        int padding = 80;

        TextView textView = new TextView(context);
        textView.setBackgroundDrawable(gradientDrawable);
        textView.setTextColor(0xEEFFFFFF);
        textView.setGravity(Gravity.CENTER);
        textView.setText("");
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(3);
        textView.setMinWidth((int) (mHeight * 1.5F));
        textView.setPadding(padding,padding,padding,padding);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,  54);
        textView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        return textView;
    }

    public PadPopupToastSRC setBackground(Drawable drawable) {
        mTextView.setBackgroundDrawable(drawable);
        return this;
    }

    public void show(View v, String msg) {
        setMsg(msg);
        showAtLocation(v, Gravity.CENTER, 0, 0);
        mHandler.removeCallbacks(mRunnerDismiss);
        mHandler.postDelayed(mRunnerDismiss, mDuration);
    }

    public PadPopupToastSRC setMsg(String msg) {
        mTextView.setText(msg == null ? "" : msg);
        return this;
    }
    public PadPopupToastSRC setDuration(int duration) {
        if (duration >=0 ) {
            mDuration = duration;
        }
        return this;
    }

}
