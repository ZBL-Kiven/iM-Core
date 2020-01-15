package com.zj.im.emotionboard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import com.zj.im.R;
import com.zj.im.emotionboard.data.EmoticonPack;
import com.zj.im.emotionboard.interfaces.EmoticonsIndicator;
import com.zj.im.emotionboard.utils.EmoticonsKeyboardUtils;

public class EmoticonsIndicatorView extends LinearLayout implements EmoticonsIndicator {

    private static final int MARGIN_LEFT = 4;
    protected Context mContext;
    protected ArrayList<ImageView> mImageViews;
    protected Drawable mDrawableSelect;
    protected Drawable mDrawableNormal;
    protected LayoutParams mLeftLayoutParams;
    public boolean isShowIndicator = true;

    private EmoticonPack<?> mCurrentPack;
    private int mCurrentPosition;

    public EmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmoticonsIndicatorView, 0, 0);

        try {
            mDrawableSelect = a.getDrawable(R.styleable.EmoticonsIndicatorView_bmpSelect);
            mDrawableNormal = a.getDrawable(R.styleable.EmoticonsIndicatorView_bmpNomal);
        } finally {
            a.recycle();
        }

        if (mDrawableNormal == null) {
            mDrawableNormal = getResources().getDrawable(R.mipmap.indicator_point_normal);
        }
        if (mDrawableSelect == null) {
            mDrawableSelect = getResources().getDrawable(R.mipmap.indicator_point_selected);
        }

        mLeftLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftLayoutParams.leftMargin = EmoticonsKeyboardUtils.dip2px(context, MARGIN_LEFT);
    }

    @Override
    public void playTo(int position, @NonNull EmoticonPack<?> pack) {
        if (!checkPack(pack)) {
            return;
        }
        mCurrentPosition = Math.min(pack.getPageCount(), position);
        updateIndicatorCount(pack.getPageCount());
        for (ImageView iv : mImageViews) {
            iv.setImageDrawable(mDrawableNormal);
        }
        if (mImageViews.size() > mCurrentPosition)
            mImageViews.get(mCurrentPosition).setImageDrawable(mDrawableSelect);
    }

    protected boolean checkPack(EmoticonPack<?> pack) {
        if (pack != null && isShowIndicator) {
            setVisibility(VISIBLE);

            mCurrentPack = pack;
            return true;
        } else {
            setVisibility(GONE);
            return false;
        }
    }

    protected void updateIndicatorCount(int count) {
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        }
        if (count > mImageViews.size()) {
            for (int i = mImageViews.size(); i < count; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageDrawable(i == 0 ? mDrawableSelect : mDrawableNormal);
                this.addView(imageView, mLeftLayoutParams);
                mImageViews.add(imageView);
            }
        }
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
            } else {
                mImageViews.get(i).setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void notifyDataChanged() {
        if (mCurrentPack != null) {
            playTo(mCurrentPosition, mCurrentPack);
        }
    }
}
