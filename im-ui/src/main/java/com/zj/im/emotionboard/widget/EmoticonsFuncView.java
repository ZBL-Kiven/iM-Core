package com.zj.im.emotionboard.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import com.zj.im.emotionboard.adpater.EmoticonPacksAdapter;
import com.zj.im.emotionboard.data.Emoticon;
import com.zj.im.emotionboard.data.EmoticonPack;


public class EmoticonsFuncView extends ViewPager {

    private static final String TAG = "EmoticonsFuncView";

    protected EmoticonPacksAdapter mAdapter;
    protected int mCurrentPagePosition;

    public EmoticonsFuncView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(EmoticonPacksAdapter adapter) {
        super.setAdapter(adapter);
        
        mAdapter = adapter;

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position:" + position + " positionOffset:" + positionOffset);
                if (Math.abs(positionOffset) < 0.000001) {
                    checkPageChange(position);
                    mCurrentPagePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected position:" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mEmoticonsFuncListener == null
                || mAdapter.getCount() == 0) {
            return;
        }
        
        EmoticonPack<?> pack = mAdapter.getPackList().get(0);
        mEmoticonsFuncListener.playTo(0, pack);
        mEmoticonsFuncListener.onCurrentEmoticonPackChanged(pack);
    }

    public void setCurrentPageSet(EmoticonPack<? extends Emoticon> pack) {
        if (mAdapter == null || mAdapter.getCount() <= 0) {
            return;
        }
        
        setCurrentItem(mAdapter.getEmoticonPackPosition(pack));
    }

    private void checkPageChange(int position) {
        Log.d(TAG, "checkPageChange");
        if (mAdapter == null) {
            return;
        }
        int start = 0;
        for (EmoticonPack<?> pack : mAdapter.getPackList()) {

            int size = pack.getPageCount();

            int end = start + size - 1;

            if (position <= end) {

                if (mEmoticonsFuncListener != null) {
                    mEmoticonsFuncListener.playTo(position - start, pack);
                }

                if (mEmoticonsFuncListener != null) {
                    mEmoticonsFuncListener.onCurrentEmoticonPackChanged(pack);
                }

                return;
            }
            start += size;
        }
    }

    private EmoticonsFuncListener mEmoticonsFuncListener;

    public void setListener(EmoticonsFuncListener listener) {
        mEmoticonsFuncListener = listener;
    }


    public interface EmoticonsFuncListener {

        void onCurrentEmoticonPackChanged(EmoticonPack<? extends Emoticon> currentPack);

        void playTo(int position, EmoticonPack<? extends Emoticon> pack);
    }
}
