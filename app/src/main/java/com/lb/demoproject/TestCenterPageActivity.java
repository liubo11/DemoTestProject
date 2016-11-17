package com.lb.demoproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016-07-21.
 */
public class TestCenterPageActivity extends BaseActivity {
    private ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_viewpager);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        Log.d("TestPager", "screenW="+dm.widthPixels+",screenH="+dm.heightPixels);

        mViewPager = findSubViewById(R.id.test_vp);

        ArrayList<View> list = new ArrayList<>(4);
        list.add(buildItemView(1, R.drawable.p1));
        list.add(buildItemView(2, R.drawable.p2));
        list.add(buildItemView(3, R.drawable.p3));
        list.add(buildItemView(4, R.drawable.p4));

        MyAdapter adapter = new MyAdapter(list);
        mViewPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        mViewPager.setAdapter(adapter);
        mViewPager.setBackgroundColor(getRandomColor());
        mViewPager.setPageTransformer(false, new Transformer());

        adapter.notifyDataSetChanged();
    }

    private View buildItemView(int position, int img) {
        ImageView v1 = new ImageView(this);
        v1.setScaleType(ImageView.ScaleType.CENTER);
        v1.setImageResource(img);
        v1.setTag(position);
        v1.setLayoutParams(new ViewPager.LayoutParams());
        return v1;
    }

    private Random random = new Random();
    private int getRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.argb(0xa0,red,green,blue);
    }

    private class MyAdapter extends PagerAdapter {

        private List<? extends View> list;

        public MyAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = list.get(position);

            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class Transformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            System.out.println("pos="+view.getTag()+",  sc="+position);



            if (position < -1) {
            } else if (position <= 0) {
                view.setScaleX(1);
                view.setScaleY(1);
                view.setTranslationX(0);
                view.setTranslationY(0);
            } else if (position <= 1) {
                final int pageWidth = view.getWidth();
                final int pageHeight = view.getHeight();
                //view.setTranslationX(-position * 0.45f * pageWidth);

                float sc = (0.5f+ (1-position) * 0.5f);
                float tx = -position * pageWidth;

                tx = (1 - sc) * pageWidth / 2;
                float ty = (1 - sc) * pageHeight / 2;

                view.setScaleX(sc);
                view.setScaleY(sc);
                view.setTranslationX(-tx);
                view.setTranslationY(-ty);

            } else {
            }
        }
    }

    private float format(float f) {
        float fn = f * 3;
        return (float)(Math.round(fn*100)/100);//如果要求精确4位就*10000然后/10000
    }
}
