package com.lb.demoproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lb.cfg.Utils;
import com.lb.widget.LightPanelView;

/**
 * Created by LiuBo on 2016-09-20.
 */
public class TestCircleColorPicer extends BaseActivity {


    private TextView tv;
    private LightPanelView lightPanelView;
    private LightPanelView lightPanelView2;
    private LightPanelView lightPanelView3;
    private LightPanelView lightPanelView4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_light_panel_view);

        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightPanelView.setCurrentValue(Utils.getRandomColor());
                v.setBackgroundColor(lightPanelView.getCurrentValue() | 0xFF000000);
            }
        });
        lightPanelView = (LightPanelView) findViewById(R.id.lightPanel1);
        lightPanelView.setCurrentIdx(0);
        lightPanelView.onValueChangeListener(0, new LightPanelView.ValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean isFinished) {
                System.out.println("rgb value="+Integer.toHexString(value)+", finished="+isFinished);
                if (value == Color.RED) {
                    System.out.println("rgb red");
                }
                tv.setBackgroundColor(value);
            }
        });

        //lightPanelView.getBlurDrawable().attachView(findViewById(R.id.space));
/*
        lightPanelView2 = (LightPanelView) findViewById(R.id.lightPanel2);
        lightPanelView2.setmCurrentIdx(1);
        lightPanelView2.onValueChangeListener(1, new LightPanelView.ValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean isFinished) {
                System.out.println("1 value="+Integer.toHexString(value)+", finished="+isFinished);
                lightPanelView3.setCurrentValue(value);
            }
        });

        lightPanelView3 = (LightPanelView) findViewById(R.id.lightPanel3);
        lightPanelView3.setmCurrentIdx(2);
        lightPanelView3.onValueChangeListener(2, new LightPanelView.ValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean isFinished) {
                System.out.println("2 value="+Integer.toHexString(value)+", finished="+isFinished);
                lightPanelView4.setCurrentValue(value);
            }
        });

        lightPanelView4 = (LightPanelView) findViewById(R.id.lightPanel4);
        lightPanelView4.setmCurrentIdx(3);
        lightPanelView4.onValueChangeListener(3, new LightPanelView.ValueChangeListener() {
            @Override
            public void onValueChange(int value, boolean isFinished) {
                System.out.println("3 value="+Integer.toHexString(value)+", finished="+isFinished);
            }
        });*/
    }

    public void onClick1(View v) {
        lightPanelView2.setCurrentValue(Utils.getRandom(100));
    }
}
