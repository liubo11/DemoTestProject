package com.lb.demoproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lb.cfg.Utils;

/**
 * Created by LiuBo on 2016-09-19.
 */
public class TestImageViewTint extends BaseActivity {


    private SharedPreferences sp;
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_image_view_tint);
        imageView = (ImageView) findViewById(R.id.img);

        imageView.setImageResource(R.drawable.pad_ic_light_rgb);

        sp = getSharedPreferences("test_apply", 0);

    }

    public void onClick1(View v) {
        //imageView.setColorFilter(Utils.getRandomColor());
        imageView.clearColorFilter();
    }
    int count;
    public void onClick2(View v) {
        count++;
        if (count%2==0)
            imageView.setColorFilter(Utils.getAlphaRandomColor(80));
        else
            imageView.setColorFilter(Utils.getAlphaRandomColor(255));
    }
}
