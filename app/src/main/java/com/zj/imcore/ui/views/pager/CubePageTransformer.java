package com.zj.imcore.ui.views.pager;

import android.view.View;
import androidx.viewpager.widget.ViewPager;
import org.jetbrains.annotations.NotNull;

/**
 * @author ZJJ on 2019.10.24
 */
@SuppressWarnings({"unused", "WeakerAccess"})
class CubePageTransformer implements ViewPager.PageTransformer {
    
    private float mMaxRotation = 15.0f;

    public CubePageTransformer() {
    }

    public CubePageTransformer(float maxRotation) {
        setMaxRotation(maxRotation);
    }

    @Override
    public void transformPage(@NotNull View view, float position) {
        if (position < -1.0f) {
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            handleRightPage(view, position);
        } else {
            handleInvisiblePage(view, position);
        }
    }

    public void handleInvisiblePage(View view, float position) {
        view.setPivotX( view.getMeasuredWidth());
        view.setPivotY( view.getMeasuredHeight() * 0.5f);
        view.setRotationY( 0);
    }

    public void handleLeftPage(View view, float position) {
        view.setPivotX( view.getMeasuredWidth());
        view.setPivotY( view.getMeasuredHeight() * 0.5f);
        view.setRotationY( mMaxRotation * position);
    }

    public void handleRightPage(View view, float position) {
        view.setPivotX( 0);
        view.setPivotY( view.getMeasuredHeight() * 0.5f);
        view.setRotationY( mMaxRotation * position);
    }

    public void setMaxRotation(float maxRotation) {
        if (maxRotation >= 0.0f && maxRotation <= 90.0f) {
            mMaxRotation = maxRotation;
        }
    }

}
