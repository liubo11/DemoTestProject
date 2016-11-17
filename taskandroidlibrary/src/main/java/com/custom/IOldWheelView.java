package com.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2016-05-31.
 */
interface IOldWheelView {
    void setCenterRectBgColor(int color);

    void setItemsColor(int color);

    void setWheelBgColor(int color);

    void setValueColor(int color);

    void setLabelColr(int color);

    void regsterListener(Handler handle, int what);

    void unRegsterListener();

    void SetTimerWheel();


    void setTextSize(int textSize);

    void setItemHeight(int height);

    /**
     * Gets wheel adapter
     *
     * @return the adapter
     */
    WheelAdapter getAdapter();

    /**
     * Sets wheel adapter
     *
     * @param adapter the new wheel adapter
     */
    void setAdapter(WheelAdapter adapter);

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    void setInterpolator(Interpolator interpolator);

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    int getVisibleItems();

    /**
     * Sets count of visible items
     *
     * @param count the new count
     */
    void setVisibleItems(int count);

    /**
     * Gets label
     *
     * @return the label
     */
    String getLabel();

    /**
     * Sets label
     *
     * @param newLabel the label to set
     */
    void setLabel(String newLabel);

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    void addChangingListener(OnWheelChangedListener listener);

    /**
     * Removes wheel changing listener
     *
     * @param listener the listener
     */
    void removeChangingListener(OnWheelChangedListener listener);


    /**
     * Adds wheel scrolling listener
     *
     * @param listener the listener
     */
    void addScrollingListener(OnWheelScrollListener listener);

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    void removeScrollingListener(OnWheelScrollListener listener);


    /**
     * Gets current value
     *
     * @return the current value
     */
    String getCurrentItem();

    int getCurrentItems();

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    void setCurrentItem(int index, boolean animated);

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    void setCurrentItem(int index);

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
     *
     * @return true if wheel is cyclic
     */
    boolean isCyclic();

    /**
     * Set wheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    void setCyclic(boolean isCyclic);


    /**
     * Returns height of wheel item
     *
     * @return the item height
     */
    int getItemHeight();

    /**
     * 用于计算每一项的单个字符的宽度
     */
    void setMeasureChar(String mc);


    void isNeedAdjust(boolean adjust);


    /**
     * Scroll the wheel
     *
     * @param itemsToSkip items to scroll
     * @param time        scrolling duration
     */
    void scroll(int itemsToScroll, int time);
}
