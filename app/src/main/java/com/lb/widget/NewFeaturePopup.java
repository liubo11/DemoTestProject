package com.lb.widget;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-11-02.
 */
public class NewFeaturePopup extends PopupWindow {
    private Activity activity;
    public NewFeaturePopup(Activity context) {
        super(context, null);
        this.activity = context;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int sw = dm.widthPixels;
        int sh = dm.heightPixels;

        setWidth(-1);
        setHeight(-1);
    }

    public void show() {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void showFullScreenImageView() {
        ImageView imgv = buildImageView(activity);
        imgv.setImageResource(R.drawable.img_list_guide);
        imgv.setScaleType(ImageView.ScaleType.FIT_XY);
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFeaturePopup.this.dismiss();
            }
        });
        setContentView(imgv);
        show();
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageView buildImageView(Context context) {
        ImageView imag = new ImageView(context);
        imag.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        return imag;
    }
}
