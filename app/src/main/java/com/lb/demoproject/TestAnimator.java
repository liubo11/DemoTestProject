package com.lb.demoproject;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.java.TestJava;
import com.lb.widget.GuideArrowView;
import com.lb.widget.MsgDialog;
import com.lb.widget.NewFeaturePopup;
import com.lb.widget.PadPopupToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuBo on 2016-09-26.
 */

public class TestAnimator extends BaseActivity {
    private TextView textView;

    private TextView textView1;

    private int count;

    private View container;
    private GuideArrowView guideArrowView;

    private PadPopupToast pop;
    private String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setPadding(0, 200, 0,0);

        setContentView(ll);

        container = ll;

        pop = new PadPopupToast(TestAnimator.this);
        pop.setDuration(2000);

        textView1 = new TextView(this);
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextSize(30);

        textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(textView, 400, 100);
        ll.addView(textView1, 400, 100);
        ll.addView(guideArrowView = new GuideArrowView(this), 200, 200);
        guideArrowView.setColorFilter(Color.BLACK);
        ((LinearLayout.LayoutParams)textView1.getLayoutParams()).setMargins(0,20, 0, 0);

        ll.setGravity(Gravity.CENTER);

        textView.setBackgroundColor(0xFFFF0000);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alphaAnimator.start(textView, "次数"+count);
                //alphaAnimator.valueAnimator.cancel();
                msg+=count;
                //MsgDialog.showAlertDialog(TestAnimator.this, msg);
                //new NewFeaturePopup(TestAnimator.this).showFullScreenImageView();
                guideArrowView.rotate(GuideArrowView.Gravity.Left);
            }
        });

        textView1.setBackgroundColor(0xFFFF0000);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg += "测试一下";
                //MsgDialog.showAlertDialog(TestAnimator.this, msg);
                //pop.show(null, msg);
                count ++;
                guideArrowView.setScaleX((10 - count) / 10F);
                guideArrowView.setScaleY((10 - count) / 10F);
            }
        });

        ColorStateList colorStateList = getResources().getColorStateList(R.color.test_color_state);

        int enableColor = colorStateList.getColorForState(
                new int[]{-android.R.attr.state_enabled}, 0);
        int selectedColor = colorStateList.getColorForState(
                new int[]{android.R.attr.state_enabled,
                        android.R.attr.state_selected}, 0);
        int pressedColor = colorStateList.getColorForState(
                new int[]{android.R.attr.state_enabled,
                        -android.R.attr.state_selected,
                        android.R.attr.state_pressed}, 0);
        int normalColor = colorStateList.getDefaultColor();
        System.out.println("state e="+Integer.toHexString(enableColor)+" s="+Integer.toHexString(selectedColor)+", p="+Integer.toHexString(pressedColor)+",n="+Integer.toHexString(normalColor));
    }


    private AlphaAnimator alphaAnimator = new AlphaAnimator();

    private class AlphaAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        AlphaAnimator() {
            initAnimator();
        }
        ValueAnimator valueAnimator;
        View a, b;
        // a 0-1, b 1-0
        void start(View a, View b) {
            if (valueAnimator.isRunning()) {
                if (textView != null) {
                    textView.setText(this.text);
                    textView.setAlpha(1);
                } else if (a != null && b != null) {
                    a.setAlpha(1);
                    b.setAlpha(0);
                }
            }

            this.a = a;
            this.b = b;
            valueAnimator.start();
        }
        // a anim 0 - 0.5 a 1-0,change text; anim 0.5-1 a 0-1;
        TextView textView;
        String text;
        void start(TextView a, String text) {
            if (valueAnimator.isRunning()) {
                if (textView != null) {
                    textView.setText(this.text);
                    if (textView != a) {
                        textView.setAlpha(1);
                    }
                } else if (a != null && b != null) {
                    a.setAlpha(1);
                    b.setAlpha(0);
                }
            }

            if (text == null) text = "Null";
            textView = a;
            this.text = text;
            valueAnimator.start();
        }

        void initAnimator() {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400);
            valueAnimator.addUpdateListener(this);
            valueAnimator.addListener(this);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float v = (float) animation.getAnimatedValue();
            if (a != null && b != null) {
                a.setAlpha(v);
                b.setAlpha(1-v);
            } else if (textView != null) {
                if (v > 0.5F) {
                    textView.setText(text);
                    textView.setAlpha(2*v - 1);
                } else {
                    textView.setAlpha(1 - 2*v);
                }
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            a = null;
            b = null;
            textView = null;
            System.out.println("onAnimationEnd");
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
