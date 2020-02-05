package com.zj.ui;

import com.zj.ui.img.AutomationImageCalculateUtils;
import org.junit.Test;

public class UT {

    private int maxW = 700;
    private int maxH = 1500;
    private float minScale = 0.5f;

    @Test
    public void calculateImageSizeTestScenes1() {
        int imgW = 300;
        int imgH = 1000;
        int maxW = this.maxW;
        int maxH = this.maxH;
        Integer[] result = AutomationImageCalculateUtils.INSTANCE.proportionalWH(imgW, imgH, maxW, maxH, minScale);
        if (result[0] == 0 || result[0] > maxW) throw new AssertionError();
        if (result[1] == 0 || result[1] > maxH) throw new AssertionError();
        System.out.println("result is : w = " + result[0] + " h = " + result[1]);
    }

    @Test
    public void calculateImageSizeTestScenes2() {
        int imgW = 1000;
        int imgH = 300;
        int maxW = this.maxW;
        int maxH = this.maxH;
        Integer[] result = AutomationImageCalculateUtils.INSTANCE.proportionalWH(imgW, imgH, maxW, maxH, minScale);
        if (result[0] == 0 || result[0] > maxW) throw new AssertionError();
        if (result[1] == 0 || result[1] > maxH) throw new AssertionError();
        System.out.println("result is : w = " + result[0] + " h = " + result[1]);
    }

    @Test
    public void calculateImageSizeTestScenes3() {
        int imgW = 40;
        int imgH = 100;
        int maxW = this.maxW;
        int maxH = this.maxH;
        Integer[] result = AutomationImageCalculateUtils.INSTANCE.proportionalWH(imgW, imgH, maxW, maxH, minScale);
        if (result[0] == 0 || result[0] > maxW) throw new AssertionError();
        if (result[1] == 0 || result[1] > maxH) throw new AssertionError();
        System.out.println("result is : w = " + result[0] + " h = " + result[1]);
    }

    @Test
    public void calculateImageSizeTestScenes4() {
        int imgW = 2000;
        int imgH = 2200;
        int maxW = this.maxW;
        int maxH = this.maxH;
        Integer[] result = AutomationImageCalculateUtils.INSTANCE.proportionalWH(imgW, imgH, maxW, maxH, minScale);
        if (result[0] == 0 || result[0] > maxW) throw new AssertionError();
        if (result[1] == 0 || result[1] > maxH) throw new AssertionError();
        System.out.println("result is : w = " + result[0] + " h = " + result[1]);
    }

    @Test
    public void calculateImageSizeTestScenes5() {
        int imgW = 600;
        int imgH = 800;
        int maxW = this.maxW;
        int maxH = this.maxH;
        Integer[] result = AutomationImageCalculateUtils.INSTANCE.proportionalWH(imgW, imgH, maxW, maxH, minScale);
        if (result[0] == 0 || result[0] > maxW) throw new AssertionError();
        if (result[1] == 0 || result[1] > maxH) throw new AssertionError();
        System.out.println("result is : w = " + result[0] + " h = " + result[1]);
    }
}
