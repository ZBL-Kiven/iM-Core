package com.zj.list.refresh.util;

import android.content.res.Resources;

/**
 * Pixel density calculation tool
 */
@SuppressWarnings("unused")
public class DensityUtil {

    private float density;

    public DensityUtil() {
        density = Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     *Conversion from dp units to px (pixels) according to the resolution of the phone
     * @param dpValue virtual pixels
     * @return pixels
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Conversion from px (pixels) to dp according to the resolution of the phone
     * @param pxValue pixels
     * @return virtual pixels
     */
    public static float px2dp(int pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Conversion from dp units to px (pixels) according to the resolution of the phone
     * @param dpValue virtual pixels
     * @return pixels
     */
    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * density);
    }

    /**
     * Conversion from px (pixels) to dp according to the resolution of the phone
     * @param pxValue pixels
     * @return virtual pixels
     */
    public float px2dip(int pxValue) {
        return (pxValue / density);
    }
}  