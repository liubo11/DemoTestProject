package com.lb.demoproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;

import com.lb.utils.RecyclerUtils;

/**
 * Created by LiuBo on 2016-10-15.
 */

public class TestNestScroll extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_nested_scroll);

        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.recycler);

        mRecycler.setAdapter(new RecyclerUtils().buildAdapter());
        mRecycler.setLayoutManager(new MyLinearLayoutManager(mRecycler, this));
        mRecycler.addItemDecoration(new MyDecoration());
    }

    private class MyLinearLayoutManager extends GridLayoutManager {
        private RecyclerView recyclerView;
        private static final int spcount = 3;
        public MyLinearLayoutManager(RecyclerView recyclerView, Context context) {
            super(context, spcount);
            this.recyclerView = recyclerView;

            setSpanSizeLookup(new SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return spcount;
                    }
                    if (position < 3) {
                        return spcount;
                    }
                    if (position >= 3 && position < 5) {
                        return 1;
                    }
                    return 3;
                }
            });
        }

        @Override
        public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
            super.measureChildWithMargins(child, widthUsed, heightUsed);

            System.out.println("measureChildWithMargins p="+recyclerView.getChildAdapterPosition(child));

        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
            System.out.println("onLayoutChildren");
        }

        @Override
        public void onLayoutCompleted(RecyclerView.State state) {
            super.onLayoutCompleted(state);
            System.out.println("onLayoutCompleted");
            //if (true) return;

            int height = recyclerView.getMeasuredHeight();
            if (height == 0) return;
            final int count = recyclerView.getChildCount();
            if (count > 0) {
                View first = null;
                int offset = 0;
                for (int i = 0; i < count; i++) {
                    View child = recyclerView.getChildAt(i);
                    int position = recyclerView.getChildAdapterPosition(child);
                    if (position == 0) {
                        first = child;
                    } else if (position == 1) {
                        offset += child.getMeasuredHeight();
                    } else if (position == 2) {
                        offset += child.getMeasuredHeight();
                    }
                }
                if (first != null) {
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) first.getLayoutParams();
                    if (lp != null) {
                        if (lp.height != recyclerView.getMeasuredHeight() - offset) {
                            lp.height = recyclerView.getMeasuredHeight() - offset;
                        }
                    }
                }
            }

        }
    }

    private class MyDecoration extends RecyclerView.ItemDecoration {

        Drawable drawable;

        public MyDecoration() {
            drawable = getResources().getDrawable(R.drawable.trans_ic);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            RecyclerView.Adapter adapter = parent.getAdapter();
            if (adapter.getItemCount() > 0) {
                int childCount = parent.getChildCount();
                int i = 0;
                for (; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int p = parent.getChildAdapterPosition(child);
                    if (p == 0) {
                        if (child.getHeight() == 0) {
                            drawable.setBounds(0,0, parent.getWidth(), 20);
                            drawable.draw(c);
                        }
                        System.out.println("position0 height="+child.getHeight());
                        break;
                    }
                }
                if (childCount != 0 && i == childCount) {
                    drawable.setBounds(0,0, parent.getWidth(), 20);
                    drawable.draw(c);
                }
            }

        }
    }
}
