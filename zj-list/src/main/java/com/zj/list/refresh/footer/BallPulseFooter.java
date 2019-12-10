package com.zj.list.refresh.footer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zj.list.R;
import com.zj.list.refresh.constance.SpinnerStyle;
import com.zj.list.refresh.interfaces.RefreshFooter;
import com.zj.list.refresh.interfaces.RefreshLayoutIn;
import com.zj.list.refresh.internal.InternalAbstract;
import com.zj.list.refresh.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 球脉冲底部加载组件
 * Created by ZJJ  on 2017/5/30.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class BallPulseFooter extends InternalAbstract implements RefreshFooter {

    //<editor-fold desc="属性变量">
    public static final int DEFAULT_SIZE = 50; //dp

    protected boolean mManualNormalColor;
    protected boolean mManualAnimationColor;
//    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;

    protected Paint mPaint;

    protected int mNormalColor = 0xffeeeeee;
    protected int mAnimatingColor = 0xffe75946;

    protected float mCircleSpacing;
    protected float[] mScaleFloats = new float[]{1f, 1f, 1f};


    protected boolean mIsStarted = false;
    protected ArrayList<ValueAnimator> mAnimators;
    protected Map<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners = new HashMap<>();
    //</editor-fold>

    //<editor-fold desc="构造方法">
    public BallPulseFooter(@NonNull Context context) {
        this(context, null);
    }

    public BallPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final View thisView = this;
        thisView.setMinimumHeight(DensityUtil.dp2px(60));

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BallPulseFooter);

        if (ta.hasValue(R.styleable.BallPulseFooter_srlNormalColor)) {
            setNormalColor(ta.getColor(R.styleable.BallPulseFooter_srlNormalColor, 0));
        }
        if (ta.hasValue(R.styleable.BallPulseFooter_srlAnimatingColor)) {
            setAnimatingColor(ta.getColor(R.styleable.BallPulseFooter_srlAnimatingColor, 0));
        }

        mSpinnerStyle = SpinnerStyle.Translate;
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.BallPulseFooter_srlClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        ta.recycle();

        mCircleSpacing = DensityUtil.dp2px(4);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mAnimators = new ArrayList<>();
        final int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            final int index = i;

            ValueAnimator animator = ValueAnimator.ofFloat(1, 0.3f, 1);

            animator.setDuration(750);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setTarget(i);
            animator.setStartDelay(delays[i]);

            mUpdateListeners.put(animator, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mScaleFloats[index] = (float) animation.getAnimatedValue();
                    thisView.postInvalidate();
                }
            });
            mAnimators.add(animator);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimators != null) for (int i = 0; i < mAnimators.size(); i++) {
            mAnimators.get(i).cancel();
            mAnimators.get(i).removeAllListeners();
            mAnimators.get(i).removeAllUpdateListeners();
        }
    }

    //</editor-fold>

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final View thisView = this;
        final int width = thisView.getWidth();
        final int height = thisView.getHeight();
        float radius = (Math.min(width, height) - mCircleSpacing * 2) / 6;
        float x = width / 2f - (radius * 2 + mCircleSpacing);
        float y = height / 2f;
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + mCircleSpacing * i;
            canvas.translate(translateX, y);
            canvas.scale(mScaleFloats[i], mScaleFloats[i]);
            canvas.drawCircle(0, 0, radius, mPaint);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
    }


    //<editor-fold desc="刷新方法 - RefreshFooter">

    @Override
    public void onStartAnimator(@NonNull RefreshLayoutIn layout, int height, int maxDragHeight) {
        if (mIsStarted) return;

        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);

            //when the animator restart , add the updateListener again because they was removed by animator stop .
            ValueAnimator.AnimatorUpdateListener updateListener = mUpdateListeners.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }
            animator.start();
        }
        mIsStarted = true;
        mPaint.setColor(mAnimatingColor);
    }

    @Override
    public int onFinish(@NonNull RefreshLayoutIn layout, boolean success) {
        if (mAnimators != null && mIsStarted) {
            mIsStarted = false;
            mScaleFloats = new float[]{1f, 1f, 1f};
            for (ValueAnimator animator : mAnimators) {
                if (animator != null) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
        mPaint.setColor(mNormalColor);
        return 0;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    public BallPulseFooter setSpinnerStyle(SpinnerStyle mSpinnerStyle) {
        this.mSpinnerStyle = mSpinnerStyle;
        return this;
    }

    public BallPulseFooter setNormalColor(@ColorInt int color) {
        mNormalColor = color;
        mManualNormalColor = true;
        if (!mIsStarted) {
            mPaint.setColor(color);
        }
        return this;
    }

    public BallPulseFooter setAnimatingColor(@ColorInt int color) {
        mAnimatingColor = color;
        mManualAnimationColor = true;
        if (mIsStarted) {
            mPaint.setColor(color);
        }
        return this;
    }

    //</editor-fold>
}
