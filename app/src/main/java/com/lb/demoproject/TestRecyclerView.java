package com.lb.demoproject;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lb.drawable.ScenceItemDrawable;
import com.lb.utils.RecyclerUtils;
import com.lb.widget.OverProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2016-07-25.
 */
public class TestRecyclerView extends BaseActivity {

    private RecyclerView mRecycler;
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


        setContentView(R.layout.layout_test_recycler);

        mRecycler = findSubViewById(R.id.recycler_v);

        mRecycler.setAdapter(new RecyclerUtils().buildAdapter());
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        mRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }




}
