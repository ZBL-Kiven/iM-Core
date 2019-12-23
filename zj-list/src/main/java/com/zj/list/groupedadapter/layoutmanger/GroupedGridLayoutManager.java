package com.zj.list.groupedadapter.layoutmanger;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.GridLayoutManager;
import com.zj.list.groupedadapter.adapter.GroupedRecyclerViewAdapter;


/**
 * GridLayoutManager for grouped lists.
 * Because if the grouped list is to be used to implement grid layout using GridLayoutManager.
 * Make sure that the head and tail of the group occupy a separate line.
 * Otherwise, the head and tail of the group may be mixed with the children, causing a layout disorder.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class GroupedGridLayoutManager extends GridLayoutManager {

    private GroupedRecyclerViewAdapter mAdapter;

    public GroupedGridLayoutManager(Context context, int spanCount, GroupedRecyclerViewAdapter adapter) {
        super(context, spanCount);
        mAdapter = adapter;
        setSpanSizeLookup();
    }

    public GroupedGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, GroupedRecyclerViewAdapter adapter) {
        super(context, spanCount, orientation, reverseLayout);
        this.mAdapter = adapter;
        setSpanSizeLookup();
    }

    public GroupedGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, GroupedRecyclerViewAdapter adapter) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mAdapter = adapter;
        setSpanSizeLookup();
    }

    private void setSpanSizeLookup() {
        super.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int count = getSpanCount();
                if (mAdapter != null) {
                    int type = mAdapter.judgeType(position);
                    //只对子项做Grid效果
                    if (type == GroupedRecyclerViewAdapter.TYPE_CHILD) {
                        int groupPosition = mAdapter.getGroupPositionForPosition(position);
                        int childPosition =
                                mAdapter.getChildPositionForPosition(groupPosition, position);
                        return getChildSpanSize(groupPosition, childPosition);
                    }
                }

                return count;
            }
        });
    }

    /**
     * Provide this method to make the SpanSize of the child change externally.
     * This method works the same as {@link SpanSizeLookup # getSpanSize (int)}.
     */
    public int getChildSpanSize(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {

    }
}