package com.lb.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lb.demoproject.R;
import com.lb.drawable.ScenceItemDrawable;
import com.lb.widget.OverProgressBar;

/**
 * Created by LiuBo on 2016-10-15.
 */

public class RecyclerUtils {

    int[][] barColors = new int[][]{{0xffff0000, 0xffffff00, 0xff00ff00, 0xff00ffff, 0xff0000ff, 0xffff00ff},
            {0xff000000, 0xffff00ff},
            {0xff000000, 0xff84c1ff},
            {0xff331400, 0xff331400},
            {0xfffe0000, 0xfffe0000},
            {0xff90ff9f, 0xff90ff9f},
            {0xff0000fe, 0xff0000fe},
            {0xffb0cfc9, 0xffb0cfc9},
            {0xffff7f00, 0xffff7f00}};

    public Adapter buildAdapter() {
        return new Adapter();
    }


    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == R.layout.test_empty_layout) {
                return new EmptyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_empty_layout, parent, false));
            }
            if (viewType == R.layout.test_progress_item) {
                return new Holder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_progress_item, parent, false));
            }
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_scence_drawable, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return R.layout.test_empty_layout;
            }
            if (position < 2) {
                return R.layout.test_progress_item;
            }
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof Holder) {
                ((Holder)holder).bindData(position);
            }
        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }
    public class Holder1 extends RecyclerView.ViewHolder {

        private TextView tvP;
        private OverProgressBar opb;

        private TextView t1;
        private TextView t2;
        private TextView t3;

        public Holder1(View itemView) {
            super(itemView);

            opb = (OverProgressBar) itemView.findViewById(R.id.progress);
            tvP = (TextView) itemView.findViewById(R.id.tv_progress);

            opb.setOnProgressChangeListener(new OverProgressBar.OnProgressChangeListener() {
                @Override
                public void progressChangeListener(int progress, boolean isFinished) {
                    tvP.setText("" + progress + "%");
                }
            });

            t1 = (TextView) itemView.findViewById(R.id.tv1);
            t2 = (TextView) itemView.findViewById(R.id.tv2);
            t3 = (TextView) itemView.findViewById(R.id.tv3);

            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t1.setSelected(!v.isSelected());
                }
            });
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t3.setEnabled(!t3.isEnabled());
                }
            });
        }

    }

    public class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }


    public class Holder extends RecyclerView.ViewHolder {

        private int p;
        private TextView textView;
        public Holder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.title);
            textView.setScaleY(0);


        }
        public void bindData(int position) {
            p = position;
            View v = itemView.findViewById(R.id.img);
            ScenceItemDrawable drawable = new ScenceItemDrawable(barColors[p % 9]);
            drawable.attachView(v);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new TestPopup((Activity) v.getContext()).show(v, "开心了");


                    //textView.setVisibility(textView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    public static class TestPopup extends PopupWindow {
        private int mDuration = 2000;

        private Handler mHandler;
        private Runnable mRunnerDismiss;

        private TextView mTextView;

        public TestPopup(Context context) {
            mHandler = new Handler();
            mRunnerDismiss = new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            };

            setWidth(602);
            setHeight(216);

            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(20);
            gradientDrawable.setColor(0x80000000);

            int padding = 20;



            TextView textView = new TextView(context);
            textView.setBackground(gradientDrawable);
            textView.setTextColor(0xEEFFFFFF);
            textView.setGravity(Gravity.CENTER);
            textView.setText("已完成");
            textView.setPadding(padding,padding,padding,padding);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
            textView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));

            setBackgroundDrawable(new ColorDrawable(0));
            setContentView(textView);

            mTextView = textView;
            setAnimationStyle(R.style.PopupAnimation);
        }
        public void show(View v, String msg) {
            setMsg(msg);
            showAtLocation(v, Gravity.CENTER, 0, 0);
            mHandler.removeCallbacks(mRunnerDismiss);
            mHandler.postDelayed(mRunnerDismiss, mDuration);
        }

        public TestPopup setMsg(String msg) {
            mTextView.setText(msg == null ? "" : msg);
            return this;
        }
        public TestPopup setDuration(int duration) {
            if (duration >=0 ) {
                mDuration = duration;
            }
            return this;
        }
    }

}
