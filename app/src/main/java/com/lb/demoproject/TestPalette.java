package com.lb.demoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ListView;

/**
 * Created by LiuBo on 2016-09-18.
 */
public class TestPalette extends BaseActivity {


    private ListView list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_palette);

        list = (ListView) findViewById(R.id.list);
    }


    public void onClick1(View v ) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.p1);
        Palette.Builder builder = Palette.from(bitmap);
        Palette palette  = builder.generate();

    }
}
