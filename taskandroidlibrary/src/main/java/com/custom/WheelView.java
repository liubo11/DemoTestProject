/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *  
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;


/**
 * Numeric wheel view.
 * 
 * @author Yuri Kanivets
 */
public class WheelView extends View {
	/** Scrolling duration */
	private static final int SCROLLING_DURATION = 400;

	/** Minimum delta for scrolling */
	private static final int MIN_DELTA_FOR_SCROLLING = 1;

	/** Current value & label text color */
	private static final int VALUE_TEXT_COLOR  = 0xFF3394a1;

	/** Items text color */
	private static final int ITEMS_TEXT_COLOR = 0xFF51C4D4;

	/** Top and bottom shadows colors */
	private static final int[] SHADOWS_COLORS = new int[] { 0x6651C4D4,
			0x0051C4D4, 0x0051C4D4 };

	/** Additional items height (is added to standard text item height) */
	private int ADDITIONAL_ITEM_HEIGHT = 15;

	/** Text size */
	private int TEXT_SIZE = 24;
	private float VALUE_SIZE_SCALE = 1.2f;

	/** Top and bottom items offset (to hide that) */
	private static final int ITEM_OFFSET = 3;

	/** Additional width for items layout */
	private static final int ADDITIONAL_ITEMS_SPACE = 8;

	/** Label offset */
	private static final int LABEL_OFFSET = 8;

	/** Left and right padding value */
	private static final int PADDING = 10;

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ITEMS = 5;
	private static final int posDef = -1;

	// Wheel Values
	private WheelAdapter adapter = null;
	public int currentItem = 0;
	
	// Widths
	private int itemsWidth = 0;
	private int labelWidth = 0;

	// Count of visible items
	private int visibleItems = DEF_VISIBLE_ITEMS;
	
	// Item height
	private int itemHeight = 0;

	// Text paints
	private TextPaint itemsPaint;
	private TextPaint valuePaint;

	// Layouts
	private StaticLayout itemsLayout;
	private StaticLayout labelLayout;
	private StaticLayout valueLayout;

	// Label & background
	private String label;
	private Drawable centerDrawable;

	// Shadows drawables
	private GradientDrawable topShadow;
	private GradientDrawable bottomShadow;

	// Scrolling
	private boolean isScrollingPerformed; 
	private int scrollingOffset;

	// Scrolling animation
	private GestureDetector gestureDetector;
	private Scroller scroller;
	private int lastScrollY;
	private int value_color = VALUE_TEXT_COLOR;
	private int item_txt_color = ITEMS_TEXT_COLOR;
	private int[] shadowColors = SHADOWS_COLORS;
	private int wheelHPos = posDef;
	private int labelColor = value_color;
	private int centerRectColor = 0xffffffff;
	private boolean singleBgColor = false;
	// Cyclic
	boolean isCyclic = false;
	
	// Listeners
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
	
	private Handler mHandler;
	private int mHandlerWhat;
	
	// 是否需要调整
	private boolean isNeedAdjust = false;
	
	public void setCenterRectBgColor(int color) {
		centerRectColor = color;
	}
	
	public void setItemsColor(int color) {
		item_txt_color = color;
	}
	
	public void setWheelBgColor(int color) {
		shadowColors = new int[] {color, color, color};
		singleBgColor = true;
	}
	
	public void setValueColor(int color) {
		value_color = color;
	}
	
	public void setLabelColr(int color) {
		labelColor = color;
	}
	
	public void regsterListener(Handler handle, int what) {
		mHandler = handle;
		mHandlerWhat = what;
	}
	
	public void unRegsterListener() {
		mHandler = null;
	}

	private void initXmlView() {
		SetTimerWheel(0);
		setAdapter(new NumberWheelAdapter(0, 23));
		setCyclic(true);
		calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
		int center = getHeight() / 2;
		int offset = getItemHeight() / 2;
		centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
	}

	public void SetTimerWheel(int pos) {
		wheelHPos = pos;

		/*
		 * 设置了整个背景颜色 或者线条绘制的drawable
		 */

		value_color = 0xff000000;
		labelColor = value_color;
		item_txt_color = 0xffff0000;//这里获取颜色
		shadowColors = new int[]{0x00000000, 0x00000000, 0x00000000};
	}
	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * Initializes class data
	 * @param context the context
	 */
	private void initData(Context context) {
		gestureDetector = new GestureDetector(context, gestureListener);
		gestureDetector.setIsLongpressEnabled(false);

		Resources rs = context.getResources();

		/*
		 * 初始化文字大小
		 */

		if(ADDITIONAL_ITEM_HEIGHT == 0){
			ADDITIONAL_ITEM_HEIGHT = 20;
		}
		if(TEXT_SIZE == 0){
			TEXT_SIZE = 25;
		}

		scroller = new Scroller(context);
	}

	public void setTextSize(int textSize){
		TEXT_SIZE = textSize;
	}
	
	public void setItemHeight(int height){
		ADDITIONAL_ITEM_HEIGHT = height;
	}
	
	/**
	 * Gets wheel adapter
	 * @return the adapter
	 */
	public WheelAdapter getAdapter() {
		return adapter;
	}
	
	/**
	 * Sets wheel adapter
	 * @param adapter the new wheel adapter
	 */
	public void setAdapter(WheelAdapter adapter) {
		this.adapter = adapter;
		invalidateLayouts();
		invalidate();
	}
	
	/**
	 * Set the the specified scrolling interpolator
	 * @param interpolator the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.forceFinished(true);
		scroller = new Scroller(getContext(), interpolator);
	}
	
	/**
	 * Gets count of visible items
	 * 
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets count of visible items
	 * 
	 * @param count
	 *            the new count
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
		invalidate();
	}

	/**
	 * Gets label
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets label
	 * 
	 * @param newLabel
	 *            the label to set
	 */
	public void setLabel(String newLabel) {
		if (label == null || !label.equals(newLabel)) {
			label = newLabel;
			labelLayout = null;
			invalidate();
		}
	}
	
	/**
	 * Adds wheel changing listener
	 * @param listener the listener 
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * @param listener the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}
	
	/**
	 * Notifies changing listeners
	 * @param oldValue the old wheel value
	 * @param newValue the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * @param listener the listener 
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * @param listener the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Gets current value
	 * 
	 * @return the current value
	 */
	public String getCurrentItem() {
		String str = null;
		if(adapter.getType() == 1)
			str = currentItem + ":00";
		else if(adapter.getType() == 2)
		{
			if(currentItem < 4)
			{
				if(currentItem % 2 == 0)
					str = (currentItem / 2) + ".0";
				else
					str = (currentItem / 2) + ".5";
			}
			else if(currentItem >= 9)
			{
				str = (currentItem -1) + ".0";
			}
			else
				str = (currentItem - 2) + ".0";
		}
		else
			str = currentItem + "";
		return str;
	}

	public int getCurrentItems() {
		return currentItem;
	}
	
	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * @param index the item index
	 * @param animated the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return; // throw?
		}
		if (index < 0 || index >= adapter.getItemsCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += adapter.getItemsCount();
				}
				index %= adapter.getItemsCount();
			} else{
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				scroll(index - currentItem, SCROLLING_DURATION);
			} else {
				invalidateLayouts();

				int old = currentItem;
				currentItem = index;

				notifyChangingListeners(old, currentItem);

				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * @param index the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}	
	
	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * @param isCyclic the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		
		invalidate();
		invalidateLayouts();
	}

	/**
	 * Invalidates layouts
	 */
	private void invalidateLayouts() {
		itemsLayout = null;
		valueLayout = null;
		scrollingOffset = 0;
	}

	/**
	 * Initializes resources
	 */
	private void initResourcesIfNecessary() {
		if (itemsPaint == null) {
			itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
					| Paint.FAKE_BOLD_TEXT_FLAG);
			//itemsPaint.density = getResources().getDisplayMetrics().density;
			itemsPaint.setTextSize(TEXT_SIZE);
		}

		if (valuePaint == null) {
			valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
					| Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
			//valuePaint.density = getResources().getDisplayMetrics().density;
			valuePaint.setTextSize(TEXT_SIZE*VALUE_SIZE_SCALE);
			valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
		}

		if (centerDrawable == null) {
			//centerDrawable = getContext().getResources().getDrawable(var_drawable_id);
		}

		if (topShadow == null) {
			topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, shadowColors);
		}

		if (bottomShadow == null) {
			bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, shadowColors);
		}

		//setBackgroundResource(bg_drawable_id);
	}

	/**
	 * Calculates desired height for layout
	 *
	 * @param layout
	 *            the source layout
	 * @return the desired layout height
	 */
	private int getDesiredHeight(Layout layout) {
		if (layout == null) {
			return 0;
		}

		int desired = getItemHeight() * visibleItems - ITEM_OFFSET * 2
				- ADDITIONAL_ITEM_HEIGHT;

		// Check against our minimum height
		desired = Math.max(desired, getSuggestedMinimumHeight());

		return desired;
	}

	/**
	 * Returns text item by index
	 * @param index the item index
	 * @return the item or null
	 */
	private String getTextItem(int index) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return null;
		}
		int count = adapter.getItemsCount();
		if ((index < 0 || index >= count) && !isCyclic) {
			return null;
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return adapter.getItem(index);
	}

	/**
	 * Builds text depending on current value
	 *
	 * @param useCurrentValue
	 * @return the text
	 */
	private String buildText(boolean useCurrentValue) {
		StringBuilder itemsText = new StringBuilder();
		int addItems = visibleItems / 2 + 1;

		for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
			if (useCurrentValue || i != currentItem) {
				String text = getTextItem(i);
				if (text != null) {
					itemsText.append(text);
				}
			}
			if (i < currentItem + addItems) {
				itemsText.append("\n");
			}
		}

		return itemsText.toString();
	}

	/**
	 * Returns the max item length that can be present
	 * @return the max length
	 */
	private int getMaxTextLength() {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return 0;
		}

		int adapterLength = adapter.getMaximumLength();
		if (adapterLength > 0) {
			return adapterLength;
		}

		String maxText = null;
		int addItems = visibleItems / 2;
		for (int i = Math.max(currentItem - addItems, 0);
				i < Math.min(currentItem + visibleItems, adapter.getItemsCount()); i++) {
			String text = adapter.getItem(i);
			if (text != null && (maxText == null || maxText.length() < text.length())) {
				maxText = text;
			}
		}

		return maxText != null ? maxText.length() : 0;
	}

	/**
	 * Returns height of wheel item
	 * @return the item height
	 */
	public int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		} else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
			itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	private String measureChar = "0";
	/**
	 * 用于计算每一项的单个字符的宽度
	 */
	public void setMeasureChar(String mc) {
		measureChar = mc;
	}
	/**
	 * Calculates control width and creates text layouts
	 * @param widthSize the input layout width
	 * @param mode the layout mode
	 * @return the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		int width = widthSize;

		int maxLength = getMaxTextLength();
		if (maxLength > 0) {
			float textWidth = (float) Math.ceil(Layout.getDesiredWidth(measureChar, itemsPaint));
			itemsWidth = (int) (maxLength * textWidth * 2);
		} else {
			itemsWidth = 0;
		}
		itemsWidth += ADDITIONAL_ITEMS_SPACE; // make it some more

		labelWidth = 0;
		if (label != null && label.length() > 0) {
			labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label, valuePaint));
		}

		boolean recalculate = false;
		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
			recalculate = true;
		} else {
			width = itemsWidth + labelWidth + 2 * PADDING;
			if (labelWidth > 0) {
				width += LABEL_OFFSET;
			}

			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
				recalculate = true;
			}
		}

		if (recalculate) {
			// recalculate width
			int pureWidth = width - LABEL_OFFSET - 2 * PADDING;
			if (pureWidth <= 0) {
				itemsWidth = labelWidth = 0;
			}
			if (labelWidth > 0) {
				double newWidthItems = (double) itemsWidth * pureWidth
						/ (itemsWidth + labelWidth);
				itemsWidth = (int) newWidthItems;
				labelWidth = pureWidth - itemsWidth;
			} else {
				itemsWidth = pureWidth + LABEL_OFFSET; // no label
			}
		}

		if (itemsWidth > 0) {
			createLayouts(itemsWidth, labelWidth);
		}

		return width;
	}

	@SuppressLint("NewApi")
	private StaticLayout buildStaticLayout(CharSequence source,
            TextPaint paint, int outerwidth,
            Alignment align,
            float spacingmult, float spacingadd,
            boolean includepad,
            TextUtils.TruncateAt ellipsize, int ellipsizedWidth, int maxLines) {
		try {
			Class cls = Class.forName(StaticLayout.class.getName());
			Class[] paramTypes = { CharSequence.class, int.class, int.class, TextPaint.class, int.class, Alignment.class,
					TextDirectionHeuristic.class, float.class, float.class, boolean.class, TextUtils.TruncateAt.class,
					int.class, int.class };
			Object[] params = { source, 0, source.length(), paint, outerwidth, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd,
					includepad, ellipsize, ellipsizedWidth, maxLines }; // 方法传入的参数

			Constructor con = cls.getConstructor(paramTypes); // 主要就是这句了
			return (StaticLayout) con.newInstance(params); // BatcherBase 为自
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StaticLayout(source, itemsPaint, outerwidth,
				align,
				spacingmult, spacingadd, includepad);
	}

	/**
	 * Creates layouts
	 * @param widthItems width of items layout
	 * @param widthLabel width of label layout
	 */
	private void createLayouts(int widthItems, int widthLabel) {
		if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
			itemsLayout = new StaticLayout(buildText(isScrollingPerformed), itemsPaint, widthItems,
					widthLabel > 0 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_CENTER,
					1, ADDITIONAL_ITEM_HEIGHT, false);
			itemsLayout = buildStaticLayout((CharSequence)buildText(isScrollingPerformed), itemsPaint, widthItems,
					widthLabel > 0 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_CENTER,
					(float)1, (float)ADDITIONAL_ITEM_HEIGHT, false, TextUtils.TruncateAt.END, getMeasuredWidth(), 1);
		} else {
			itemsLayout.increaseWidthTo(widthItems);
		}

		if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
			String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
			valueLayout = new StaticLayout(text != null ? text : "",
					valuePaint, widthItems, widthLabel > 0 ?
							Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_CENTER,
							1, ADDITIONAL_ITEM_HEIGHT, false);
		} else if (isScrollingPerformed) {
			valueLayout = null;
		} else {
			valueLayout.increaseWidthTo(widthItems);
		}

		if (widthLabel > 0) {
			if (labelLayout == null || labelLayout.getWidth() > widthLabel) {
				labelLayout = new StaticLayout(label, valuePaint,
						widthLabel, Alignment.ALIGN_NORMAL, 1,
						ADDITIONAL_ITEM_HEIGHT, false);
			} else {
				labelLayout.increaseWidthTo(widthLabel);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCenterRect(canvas);
		drawShadows(canvas);

		if (itemsLayout == null) {
			if (itemsWidth == 0) {
				calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			} else {
				createLayouts(itemsWidth, labelWidth);
			}
		}

		if (itemsWidth > 0) {
			canvas.save();
			// Skip padding space and hide a part of top and bottom items
			canvas.translate(PADDING, -ITEM_OFFSET);
			drawItems(canvas);
			drawValue(canvas);
			canvas.restore();
		}


	}

	/**
	 * Draws shadows on top and bottom of control
	 * @param canvas the canvas for drawing
	 */
	private void drawShadows(Canvas canvas) {
		if (singleBgColor) {
			topShadow.setBounds(0, 0, getWidth(), 2 * getHeight() / visibleItems);
		} else {
			topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
		}
		topShadow.draw(canvas);

		if (singleBgColor) {
			bottomShadow.setBounds(0, getHeight() - 2 * getHeight() / visibleItems,
					getWidth(), getHeight());
		} else {
			bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems,
					getWidth(), getHeight());
		}

		bottomShadow.draw(canvas);
	}

	/**
	 * Draws value and label layout
	 * @param canvas the canvas for drawing
	 */
	private void drawValue(Canvas canvas) {

		if (wheelHPos != posDef) {
			valuePaint.setTextSize(TEXT_SIZE*VALUE_SIZE_SCALE);
		}

		valuePaint.setColor(value_color);
		valuePaint.drawableState = getDrawableState();

		Rect bounds = new Rect();
		itemsLayout.getLineBounds(visibleItems / 2, bounds);

		// draw label
		if (labelLayout != null) {
			canvas.save();
			canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET, bounds.top);
			valuePaint.setColor(labelColor);
			labelLayout.draw(canvas);
			canvas.restore();
		}

		// draw current value
		if (valueLayout != null) {
			canvas.save();
			canvas.translate(getValueHPos(), bounds.top + scrollingOffset + getadJustOffset()-TEXT_SIZE*(VALUE_SIZE_SCALE-1)/2);
			valuePaint.setColor(value_color);
			valueLayout.draw(canvas);
			canvas.restore();
		}
	}

	// 用于矫正偏移值
	private int getadJustOffset() {
		if (isNeedAdjust) {
			return Math.round(TEXT_SIZE / visibleItems);
		} else {
			return 0;
		}
	}

	public void isNeedAdjust(boolean adjust) {
		this.isNeedAdjust = adjust;
	}

	private float getValueHPos() {
		float hpos = 0;
		float shift = 0;
		if (wheelHPos == 0) {
			hpos = itemsLayout.getWidth() / 2 - shift;
		} else if (wheelHPos == 1) {
			hpos = -itemsLayout.getWidth() / 2 + shift;
		}
		return hpos;
	}

	/**
	 * Draws items
	 * @param canvas the canvas for drawing
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();

		int top = itemsLayout.getLineTop(1);

		canvas.translate(getValueHPos(), - top + scrollingOffset + getadJustOffset());

		itemsPaint.setColor(item_txt_color);
		itemsPaint.drawableState = getDrawableState();
		itemsLayout.draw(canvas);

		canvas.restore();
	}

	/**
	 * Draws rect for current value
	 * @param canvas the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = getItemHeight() / 2;
		if (null != centerDrawable) {
			centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
			centerDrawable.draw(canvas);
		} else {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(0xff000000);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(0, center - offset, getWidth(), center + offset, paint);
		}

		if (centerRectColor != 0xffffffff) {
			centerDrawable.setColorFilter(centerRectColor, PorterDuff.Mode.SRC_ATOP);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return true;
		}
		
			if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
			justify();
		}
		return true;
	}

	/**
	 * Scrolls the wheel
	 * @param delta the scrolling value
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;

		int count = scrollingOffset / getItemHeight();
		int pos = currentItem - count;
		if (isCyclic && adapter.getItemsCount() > 0) {
			// fix position by rotating
			while (pos < 0) {
				pos += adapter.getItemsCount();
			}
			pos %= adapter.getItemsCount();
		} else if (isScrollingPerformed) {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= adapter.getItemsCount()) {
				count = currentItem - adapter.getItemsCount() + 1;
				pos = adapter.getItemsCount() - 1;
			}
		} else {
			// fix position
			pos = Math.max(pos, 0);
			pos = Math.min(pos, adapter.getItemsCount() - 1);
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * getItemHeight();
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}

	// gesture listener
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		public boolean onDown(MotionEvent e) {
			if (isScrollingPerformed) {
				scroller.forceFinished(true);
				clearMessages();
				return true;
			}
			return false;
		}
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			startScrolling();
			doScroll((int)-distanceY);
			return true;
		}
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			lastScrollY = currentItem * getItemHeight() + scrollingOffset;
			int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
			int minY = isCyclic ? -maxY : 0;
			scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY, maxY);
			setNextMessage(MESSAGE_SCROLL);
			return true;
		}
	};

	// Messages
	private final int MESSAGE_SCROLL = 0;
	private final int MESSAGE_JUSTIFY = 1;

	/**
	 * Set next message to queue. Clears queue before.
	 *
	 * @param message the message to set
	 */
	private void setNextMessage(int message) {
		clearMessages();
		animationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue
	 */
	private void clearMessages() {
		animationHandler.removeMessages(MESSAGE_SCROLL);
		animationHandler.removeMessages(MESSAGE_JUSTIFY);
	}

	// animation handler
	private Handler animationHandler = new Handler() {
		public void handleMessage(Message msg) {
			scroller.computeScrollOffset();
			int currY = scroller.getCurrY();
			int delta = lastScrollY - currY;
			lastScrollY = currY;
			if (delta != 0) {
				doScroll(delta);
			}

			// scrolling is not finished when it comes to final Y
			// so, finish it manually
			if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
				currY = scroller.getFinalY();
				scroller.forceFinished(true);
			}
			if (!scroller.isFinished()) {
				animationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}
		}
	};

	/**
	 * Justifies wheel
	 */
	private void justify() {
		if (adapter == null) {
			return;
		}

		lastScrollY = 0;
		int offset = scrollingOffset;
		int itemHeight = getItemHeight();
		boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0;
		if ((isCyclic || needToIncrease) && Math.abs((float) offset) > (float) itemHeight / 2) {
			if (offset < 0)
				offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
			else
				offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
		}
		if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
			scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
			setNextMessage(MESSAGE_JUSTIFY);
		} else {
			finishScrolling();
		}
	}

	/**
	 * Starts scrolling
	 */
	private void startScrolling() {
		if (!isScrollingPerformed) {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}
	}

	/**
	 * Finishes scrolling
	 */
	void finishScrolling() {
		if (isScrollingPerformed) {
			notifyScrollingListenersAboutEnd();
			isScrollingPerformed = false;
		}

		if (mHandler != null) {
			Message msg = new Message();
			msg.what = mHandlerWhat;
			msg.arg1 = currentItem;
			mHandler.sendMessage(msg);
		}
		invalidateLayouts();
		invalidate();
	}


	/**
	 * Scroll the wheel
	 * @param itemsToSkip items to scroll
	 * @param time scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		scroller.forceFinished(true);

		lastScrollY = scrollingOffset;
		int offset = itemsToScroll * getItemHeight();

		scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
		setNextMessage(MESSAGE_SCROLL);

		startScrolling();
	}

}
