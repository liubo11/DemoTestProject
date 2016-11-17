package com.custom.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;
/**
 * 示例
 * <pre>
 * HorizontalWheelView hwv = new HorizontalWheelView(this);//注意以下方法的调用顺序
 * hwv.setWrapSelectorWheel(false);//设置是否循环
 * hwv.setNormalTextSize(50);//可以不设置
 * hwv.setMaxValue(dispItems.length - 1);
 * hwv.setValue(x);
 * hwv.setDisplayedValues(dispItems);
 * hwv.setOnValueChangedListener(new OnValueChangeListener());
 * hwv.setLayoutParams(new LayoutParams(-1, 300));//需要设置一个高度
 * hwv.setMiddSignEnable(true);//设置中心标志是否显示
 * </pre>
 * 
 * @see NumberPicker
 */
public class HorizontalWheelView extends CustomWheelView {
	public HorizontalWheelView(Context context) {
		this(context, null);
	}
	public HorizontalWheelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public HorizontalWheelView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, 0);
	}
}
