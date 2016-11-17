package com.galaxywind.clib;

import android.graphics.Bitmap;

/**
 * Created by LiuBo on 2016-10-24.
 */

public class CLib {
    static {
        System.loadLibrary("clib_jni");
    }

    /**
     * 高斯模糊
     * @param output 输入+输出
     * @param radius 半径
     */
    public static native void ClBlurBitmap(Bitmap output, int radius);
}
