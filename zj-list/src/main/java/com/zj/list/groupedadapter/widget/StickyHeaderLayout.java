package com.zj.list.groupedadapter.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.zj.list.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.zj.list.holders.BaseViewHolder;

import java.lang.reflect.Method;

/**
 * Depiction: Ceiling ceiling layout. Just wrap {@link RecyclerView} with StickyHeaderLayout,
 * And use {@link GroupedRecyclerViewAdapter} to achieve the ceiling function of the list head.
 * StickyHeaderLayout can only wrap RecyclerView, and can only wrap one RecyclerView.
 */
@SuppressWarnings("unused")
public class StickyHeaderLayout extends FrameLayout {

    private Context mContext;
    private RecyclerView mRecyclerView;

    // Ceiling container, used to carry the ceiling layout.
    private FrameLayout mStickyLayout;

    // Save the cache pool of the ceiling layout. It uses the viewType at the head of the list group as the key and ViewHolder as the value to save and recycle the ceiling layout.
    private final SparseArray<BaseViewHolder> mStickyViews = new SparseArray<>();

    // The key used to save the viewType in the ceiling layout.
    private final int VIEW_TAG_TYPE = -101;

    // Used to save the key of ViewHolder in the ceiling layout.
    private final int VIEW_TAG_HOLDER = -102;

    // Record the current ceiling group.
    private int mCurrentStickyGroup = -1;

    // whether ceiling.
    private boolean isSticky = true;

    // whether the adapter refresh monitoring has been registered
    private boolean isRegisterDataObserver = false;

    public StickyHeaderLayout(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public StickyHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public StickyHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0 || !(child instanceof RecyclerView)) {
            // The outside world can only add a RecyclerView to StickyHeaderLayout, and only RecyclerView can be added.
            throw new IllegalArgumentException("StickyHeaderLayout can host only one direct child --> RecyclerView");
        }
        super.addView(child, index, params);
        mRecyclerView = (RecyclerView) child;
        addOnScrollListener();
        addStickyLayout();
    }

    /**
     * Add scroll listen
     */
    private void addOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isSticky) {
                    updateStickyView(false);
                }
            }
        });
    }

    /**
     * Add ceiling container
     */
    private void addStickyLayout() {
        mStickyLayout = new FrameLayout(mContext);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mStickyLayout.setLayoutParams(lp);
        super.addView(mStickyLayout, 1, lp);
    }

    /**
     * Force update ceiling layout.
     */
    public void updateStickyView() {
        updateStickyView(true);
    }

    /**
     * Updated ceiling layout.
     *
     * @param imperative Whether to force an update.
     */
    private void updateStickyView(boolean imperative) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof GroupedRecyclerViewAdapter) {
            GroupedRecyclerViewAdapter gAdapter = (GroupedRecyclerViewAdapter) adapter;
            registerAdapterDataObserver(gAdapter);
            int firstVisibleItem = getFirstVisibleItem();
            int groupPosition = gAdapter.getGroupPositionForPosition(firstVisibleItem);

            // If the head of the current ceiling is not the head of the group we want to ceiling, update the ceiling layout. This will avoid frequent updates to the ceiling layout.
            if (imperative || mCurrentStickyGroup != groupPosition) {
                mCurrentStickyGroup = groupPosition;

                int groupHeaderPosition = gAdapter.getPositionForGroupHeader(groupPosition);
                if (groupHeaderPosition != -1) {
                    int viewType = gAdapter.getItemViewType(groupHeaderPosition);

                    // If the type of the current ceiling layout is the same as what we need, get its ViewHolder directly, otherwise recycle.
                    BaseViewHolder holder = recycleStickyView(viewType);

                    // Identifies whether the holder is taken from the current ceiling layout.
                    boolean flag = holder != null;

                    if (holder == null) {
                        // Get the ceiling layout from the cache pool.
                        holder = getStickyViewByType(viewType);
                    }

                    if (holder == null) {
                        // If the ceiling layout is not obtained from the cache pool, it is created through GroupedRecyclerViewAdapter.
                        holder = gAdapter.onCreateViewHolder(mStickyLayout, viewType);
                        holder.itemView.setTag(VIEW_TAG_TYPE, viewType);
                        holder.itemView.setTag(VIEW_TAG_HOLDER, holder);
                    }

                    // Update the data of the ceiling layout through GroupedRecyclerViewAdapter.
                    // This can ensure that the display effect of the ceiling layout is consistent with the group header in the list.
                    gAdapter.onBindViewHolder(holder, groupHeaderPosition);

                    // If the holder is not taken from the current ceiling layout, you need to add the ceiling layout to the container.
                    if (!flag) {
                        mStickyLayout.addView(holder.itemView);
                    }
                } else {
                    // If the current group has no group header, the ceiling layout is not displayed.
                    // Recycle the old ceiling layout.
                    recycle();
                }
            }

            //This is the case when the ceiling layout has been added to StickyLayout when it is first opened, but the height of StickyLayout is still 0.
            if (mStickyLayout.getChildCount() > 0 && mStickyLayout.getHeight() == 0) {
                mStickyLayout.requestLayout();
            }

            //Set the Y offset of mStickyLayout.
            mStickyLayout.setTranslationY(calculateOffset(gAdapter, firstVisibleItem, groupPosition + 1));
        }
    }

    /**
     * Register adapter to refresh listening
     */
    private void registerAdapterDataObserver(GroupedRecyclerViewAdapter adapter) {
        if (!isRegisterDataObserver) {
            isRegisterDataObserver = true;
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

            });
        }
    }

    private void updateStickyViewDelayed() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStickyView(true);
            }
        }, 64);
    }

    /**
     * Determines whether the ceiling layout needs to be recycled first. If it is to be recycled, the ceiling layout is recycled and returns null.
     * If it is not recycled, it returns the ViewHolder of the ceiling layout.
     * This will avoid frequent additions and removals of the ceiling layout.
     */
    private BaseViewHolder recycleStickyView(int viewType) {
        if (mStickyLayout.getChildCount() > 0) {
            View view = mStickyLayout.getChildAt(0);
            int type = (int) view.getTag(VIEW_TAG_TYPE);
            if (type == viewType) {
                return (BaseViewHolder) view.getTag(VIEW_TAG_HOLDER);
            } else {
                recycle();
            }
        }
        return null;
    }

    /**
     * Recycling and removing ceiling layouts
     */
    private void recycle() {
        if (mStickyLayout.getChildCount() > 0) {
            View view = mStickyLayout.getChildAt(0);
            mStickyViews.put((int) (view.getTag(VIEW_TAG_TYPE)), (BaseViewHolder) view.getTag(VIEW_TAG_HOLDER));
            mStickyLayout.removeAllViews();
        }
    }

    /**
     * Get ceiling layout from cache pool
     *
     * @param viewType viewType of ceiling layout
     */
    private BaseViewHolder getStickyViewByType(int viewType) {
        return mStickyViews.get(viewType);
    }

    /**
     * Calculate the offset of StickyLayout. Because if the head of the next group reaches StickyLayout,
     * StickyLayout will be pushed up until the head of the next group becomes a ceiling layout. Otherwise, two group headers will overlap.
     *
     * @param firstVisibleItem The first item displayed in the current list.
     * @param groupPosition The group index of the next group.
     * @return returns the offset.
     */
    private float calculateOffset(GroupedRecyclerViewAdapter gAdapter, int firstVisibleItem, int groupPosition) {
        int groupHeaderPosition = gAdapter.getPositionForGroupHeader(groupPosition);
        if (groupHeaderPosition != -1) {
            int index = groupHeaderPosition - firstVisibleItem;
            if (mRecyclerView.getChildCount() > index) {
                View view = mRecyclerView.getChildAt(index);
                float off = view.getY() - mStickyLayout.getHeight();
                if (off < 0) {
                    return off;
                }
            }
        }
        return 0;
    }

    /**
     * get the currently displayed item
     */
    private int getFirstVisibleItem() {
        int firstVisibleItem = -1;
        RecyclerView.LayoutManager layout = mRecyclerView.getLayoutManager();
        if (layout != null) {
            if (layout instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof StaggeredGridLayoutManager) {
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layout).getSpanCount()];
                ((StaggeredGridLayoutManager) layout).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = getMin(firstPositions);
            }
        }
        return firstVisibleItem;
    }

    private int getMin(int[] arr) {
        int min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min)
                min = arr[x];
        }
        return min;
    }

    /**
     * whether ceiling
     */
    public boolean isSticky() {
        return isSticky;
    }

    /**
     * Set whether to ceiling.
     */
    public void setSticky(boolean sticky) {
        if (isSticky != sticky) {
            isSticky = sticky;
            if (mStickyLayout != null) {
                if (isSticky) {
                    mStickyLayout.setVisibility(VISIBLE);
                    updateStickyView(false);
                } else {
                    recycle();
                    mStickyLayout.setVisibility(GONE);
                }
            }
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollOffset");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollOffset();
    }


    @Override
    protected int computeVerticalScrollRange() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollRange");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollExtent");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollExtent();
    }

    @Override
    public void scrollBy(int x, int y) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollBy(x, y);
        } else {
            super.scrollBy(x, y);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollTo(x, y);
        } else {
            super.scrollTo(x, y);
        }
    }
}
