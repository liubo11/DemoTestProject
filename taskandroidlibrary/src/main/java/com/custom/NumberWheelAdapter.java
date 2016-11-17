package com.custom;

public class NumberWheelAdapter implements WheelAdapter {
	private int[] data;
	public NumberWheelAdapter(int max) {
		this(0, max);
	}
	
	public NumberWheelAdapter(int min, int max) {
		if (max <= min) {
			throw new IllegalArgumentException("maxNum <= minNum, max="+max+", min="+min);
		}
		data = new int[max - min + 1];
		for (int i = 0, j = min; j <= max; j++, i++) {
			data[i] = j;
		}
	}
	
	@Override
	public int getItemsCount() {
		return data.length;
	}

	@Override
	public String getItem(int index) {
		if (index < 0 || index >= data.length) {
			return null;
		}
		return String.valueOf(data[index]);
	}

	@Override
	public int getMaximumLength() {
		return 0;
	}

	@Override
	public int getType() {
		return 0;
	}

}
