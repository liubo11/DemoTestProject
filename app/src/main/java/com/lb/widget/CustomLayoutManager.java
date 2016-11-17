package com.lb.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuBo on 2016-10-20.
 */

public class CustomLayoutManager extends RecyclerView.LayoutManager {


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        //将界面中的item都detach  缓存与scrap中 以便下次取出显示
        detachAndScrapAttachedViews(recycler);

        View first = recycler.getViewForPosition(0);
        measureChildWithMargins(first, 0, 0);
        //测量第一个view getDecoratedXX是获取带有decoration的尺寸
        int itemWidth = getDecoratedMeasuredWidth(first);
        int itemHeight = getDecoratedMeasuredHeight(first);

    }
}
