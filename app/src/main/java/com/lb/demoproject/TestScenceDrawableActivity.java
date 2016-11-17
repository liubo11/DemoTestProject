package com.lb.demoproject;

import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lb.drawable.ScenceItemDrawable;

/**
 * Created by LiuBo on 2016-09-22.
 */
public class TestScenceDrawableActivity extends BaseActivity {
    int[][] barColors = new int[][]{{0xffff0000, 0xffffff00, 0xff00ff00, 0xff00ffff, 0xff0000ff, 0xffff00ff},
        {0xff000000, 0xffff00ff},
        {0xff000000, 0xff84c1ff},
        {0xff331400, 0xff331400},
        {0xfffe0000, 0xfffe0000},
        {0xff90ff9f, 0xff90ff9f},
        {0xff0000fe, 0xff0000fe},
        {0xffb0cfc9, 0xffb0cfc9},
        {0xffff7f00, 0xffff7f00}};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_scence_drawable);

        View v = findViewById(R.id.img);
        ScenceItemDrawable drawable = new ScenceItemDrawable(barColors[0]);
        drawable.attachView(v);
    }

    public void onClick1(View v) {
        v.setSelected(!v.isSelected());
        System.out.println("on click img");
    }
}
