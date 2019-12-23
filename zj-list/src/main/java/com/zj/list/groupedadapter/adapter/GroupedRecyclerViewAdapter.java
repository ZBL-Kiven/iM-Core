package com.zj.list.groupedadapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.zj.list.R;
import com.zj.list.adapters.BaseRecyclerAdapter;
import com.zj.list.groupedadapter.structure.GroupStructure;
import com.zj.list.holders.BaseViewHolder;

import java.util.ArrayList;

/**
 * Universal grouping list adapter. It can be very convenient to achieve the effect of grouping lists.
 * This class provides a series of methods for updating, deleting, and inserting lists.
 * Users should use the list of these methods to operate, instead of directly using the method of RecyclerView.Adapter.
 * Because when the group list changes, the group structure of the group list needs to be updated in time {@link GroupedRecyclerViewAdapter # mStructures}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class GroupedRecyclerViewAdapter<T> extends BaseRecyclerAdapter<BaseViewHolder, T> {

    public static final int TYPE_HEADER = R.integer.type_header;
    public static final int TYPE_FOOTER = R.integer.type_footer;
    public static final int TYPE_CHILD = R.integer.type_child;

    private OnHeaderClickListener mOnHeaderClickListener;
    private OnFooterClickListener mOnFooterClickListener;
    private OnChildClickListener mOnChildClickListener;

    protected Context mContext;
    //保存分组列表的组结构
    protected ArrayList<GroupStructure> mStructures = new ArrayList<>();
    // update the group structure in time when the data has changed.
    private boolean isDataChanged;
    private int mTempPosition;

    public GroupedRecyclerViewAdapter(Context context) {
        super();
        mContext = context;
        registerAdapterDataObserver(new GroupDataObserver());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        structureChanged();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        return layoutParams instanceof StaggeredGridLayoutManager.LayoutParams;
    }

    private void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position) {
        if (judgeType(position) == TYPE_HEADER || judgeType(position) == TYPE_FOOTER) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)
                    holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                getLayoutId(mTempPosition, viewType), parent, false);
        return new BaseViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, int position) {
        int type = judgeType(position);
        final int groupPosition = getGroupPositionForPosition(position);
        if (type == TYPE_HEADER) {
            if (mOnHeaderClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnHeaderClickListener != null) {
                            ViewParent parent = holder.itemView.getParent();
                            int gPosition = parent instanceof FrameLayout ? groupPosition : getGroupPositionForPosition(holder.getLayoutPosition());
                            if (gPosition >= 0 && gPosition < mStructures.size()) {
                                mOnHeaderClickListener.onHeaderClick(GroupedRecyclerViewAdapter.this, holder, gPosition);
                            }
                        }
                    }
                });
            }
            onBindHeaderViewHolder(holder, groupPosition);
        } else if (type == TYPE_FOOTER) {
            if (mOnFooterClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnFooterClickListener != null) {
                            int gPosition = getGroupPositionForPosition(holder.getLayoutPosition());
                            if (gPosition >= 0 && gPosition < mStructures.size()) {
                                mOnFooterClickListener.onFooterClick(GroupedRecyclerViewAdapter.this, holder, gPosition);
                            }
                        }
                    }
                });
            }
            onBindFooterViewHolder(holder, groupPosition);
        } else if (type == TYPE_CHILD) {
            int childPosition = getChildPositionForPosition(groupPosition, position);
            if (mOnChildClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            int gPosition = getGroupPositionForPosition(holder.getLayoutPosition());
                            int cPosition = getChildPositionForPosition(gPosition, holder.getLayoutPosition());
                            if (gPosition >= 0 && gPosition < mStructures.size() && cPosition >= 0
                                    && cPosition < mStructures.get(gPosition).getChildrenCount()) {
                                mOnChildClickListener.onChildClick(GroupedRecyclerViewAdapter.this,
                                        holder, gPosition, cPosition);
                            }
                        }
                    }
                });
            }
            onBindChildViewHolder(holder, groupPosition, childPosition);
        }
    }

    @Override
    public int getItemCount() {
        if (isDataChanged) {
            structureChanged();
        }
        return count();
    }

    @Override
    public int getItemViewType(int position) {
        mTempPosition = position;
        int groupPosition = getGroupPositionForPosition(position);
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderViewType(groupPosition);
        } else if (type == TYPE_FOOTER) {
            return getFooterViewType(groupPosition);
        } else if (type == TYPE_CHILD) {
            int childPosition = getChildPositionForPosition(groupPosition, position);
            return getChildViewType(groupPosition, childPosition);
        }
        return super.getItemViewType(position);
    }

    public int getHeaderViewType(int groupPosition) {
        return TYPE_HEADER;
    }

    public int getFooterViewType(int groupPosition) {
        return TYPE_FOOTER;
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        return TYPE_CHILD;
    }

    private int getLayoutId(int position, int viewType) {
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderLayout(viewType);
        } else if (type == TYPE_FOOTER) {
            return getFooterLayout(viewType);
        } else if (type == TYPE_CHILD) {
            return getChildLayout(viewType);
        }
        return 0;
    }

    private int count() {
        return countGroupRangeItem(0, mStructures.size());
    }

    /**
     * Determine the type of the item head and tail and children
     */
    public int judgeType(int position) {
        int itemCount = 0;
        int groupCount = mStructures.size();

        for (int i = 0; i < groupCount; i++) {
            GroupStructure structure = mStructures.get(i);
            if (structure.hasHeader()) {
                itemCount += 1;
                if (position < itemCount) {
                    return TYPE_HEADER;
                }
            }

            itemCount += structure.getChildrenCount();
            if (position < itemCount) {
                return TYPE_CHILD;
            }

            if (structure.hasFooter()) {
                itemCount += 1;
                if (position < itemCount) {
                    return TYPE_FOOTER;
                }
            }
        }

        throw new IndexOutOfBoundsException("can't determine the item type of the position." +
                "position = " + position + ",item count = " + getItemCount());
    }

    /**
     * Reset group structure list
     */
    private void structureChanged() {
        mStructures.clear();
        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            mStructures.add(new GroupStructure(hasHeader(i), hasFooter(i), getChildrenCount(i)));
        }
        isDataChanged = false;
    }

    /**
     * Calculate the group where position is based on the subscript (groupPosition)
     */
    public int getGroupPositionForPosition(int position) {
        int count = 0;
        int groupCount = mStructures.size();
        for (int i = 0; i < groupCount; i++) {
            count += countGroupItem(i);
            if (position < count) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculate position in the group according to the subscript (childPosition)
     */
    public int getChildPositionForPosition(int groupPosition, int position) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            int itemCount = countGroupRangeItem(0, groupPosition + 1);
            GroupStructure structure = mStructures.get(groupPosition);
            int p = structure.getChildrenCount() - (itemCount - position)
                    + (structure.hasFooter() ? 1 : 0);
            if (p >= 0) {
                return p;
            }
        }
        return -1;
    }

    /**
     * Get the start index of a group, this index may be the group header, it may be a child (if there is no group header) or the group tail (if the group has only the group tail)
     */
    public int getPositionForGroup(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            return countGroupRangeItem(0, groupPosition);
        } else {
            return -1;
        }

    }

    /**
     * Get the group header index of a group. If the group has no group header, return -1.
     */
    public int getPositionForGroupHeader(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (!structure.hasHeader()) {
                return -1;
            }
            return countGroupRangeItem(0, groupPosition);
        }
        return -1;
    }

    /**
     * Get the group tail index of a group. If the group has no group tail, return -1.
     */
    public int getPositionForGroupFooter(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (!structure.hasFooter()) {
                return -1;
            }
            return countGroupRangeItem(0, groupPosition + 1) - 1;
        }
        return -1;
    }

    /**
     * Gets the subscript of a specified group of items.If not, returns -1.
     */
    public int getPositionForChild(int groupPosition, int childPosition) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.getChildrenCount() > childPosition) {
                int itemCount = countGroupRangeItem(0, groupPosition);
                return itemCount + childPosition + (structure.hasHeader() ? 1 : 0);
            }
        }
        return -1;
    }

    /**
     * Count how many items are in a group (head plus tail plus children)
     */
    public int countGroupItem(int groupPosition) {
        int itemCount = 0;
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                itemCount += 1;
            }
            itemCount += structure.getChildrenCount();
            if (structure.hasFooter()) {
                itemCount += 1;
            }
        }
        return itemCount;
    }

    /**
     * 计算多个组的项的总和
     */
    public int countGroupRangeItem(int start, int count) {
        int itemCount = 0;
        int size = mStructures.size();
        for (int i = start; i < size && i < start + count; i++) {
            itemCount += countGroupItem(i);
        }
        return itemCount;
    }

    /**
     * Notification data list refresh
     */
    public void notifyDataChanged() {
        isDataChanged = true;
        notifyDataSetChanged();
    }

    /**
     * Notify a group of data refresh, including group header, group tail and children
     */
    public void notifyGroupChanged(int groupPosition) {
        int index = getPositionForGroup(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * Use {@link #notifyGroupRangeChanged(int, int)} instead.
     */
    @Deprecated
    public void changeRangeGroup(int groupPosition, int count) {
        notifyGroupRangeChanged(groupPosition, count);
    }

    /**
     * Notify multiple groups of data refresh, including group header, group tail and children
     */
    public void notifyGroupRangeChanged(int groupPosition, int count) {
        int index = getPositionForGroup(groupPosition);
        int itemCount;
        if (groupPosition + count <= mStructures.size()) {
            itemCount = countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount = countGroupRangeItem(groupPosition, mStructures.size());
        }
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * Use {@link #notifyHeaderChanged(int)} instead.
     */
    @Deprecated
    public void changeHeader(int groupPosition) {
        notifyHeaderChanged(groupPosition);
    }

    /**
     * Notification group header refresh
     */
    public void notifyHeaderChanged(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * Use {@link #notifyFooterChanged(int)} instead.
     */
    @Deprecated
    public void changeFooter(int groupPosition) {
        notifyFooterChanged(groupPosition);
    }

    /**
     * Notify group tail refresh
     */
    public void notifyFooterChanged(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * Use {@link #notifyChildChanged(int, int)} instead.
     */
    @Deprecated
    public void changeChild(int groupPosition, int childPosition) {
        notifyChildChanged(groupPosition, childPosition);
    }

    /**
     * Notify a child of a group to refresh
     */
    public void notifyChildChanged(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * Use {@link #notifyChildRangeChanged(int, int, int)} instead.
     */
    @Deprecated
    public void changeRangeChild(int groupPosition, int childPosition, int count) {
        notifyChildRangeChanged(groupPosition, childPosition, count);
    }

    /**
     * Notify multiple children in a group to refresh
     */
    public void notifyChildRangeChanged(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                if (structure.getChildrenCount() >= childPosition + count) {
                    notifyItemRangeChanged(index, count);
                } else {
                    notifyItemRangeChanged(index, structure.getChildrenCount() - childPosition);
                }
            }
        }
    }

    /**
     * Use {@link #notifyChildrenChanged(int)} instead.
     */
    @Deprecated
    public void changeChildren(int groupPosition) {
        notifyChildrenChanged(groupPosition);
    }

    /**
     * notify all children in a group to refresh
     */
    public void notifyChildrenChanged(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                notifyItemRangeChanged(index, structure.getChildrenCount());
            }
        }
    }

    /**
     * Use {@link #notifyDataRemoved()} instead.
     */
    @Deprecated
    public void removeAll() {
        notifyDataRemoved();
    }

    /**
     * notify of all data deletion
     */
    public void notifyDataRemoved() {
        int count = countGroupRangeItem(0, mStructures.size());
        mStructures.clear();
        notifyItemRangeRemoved(0, count);
    }

    /**
     * Use {@link #notifyGroupRemoved(int)} instead.
     */
    @Deprecated
    public void removeGroup(int groupPosition) {
        notifyGroupRemoved(groupPosition);
    }

    /**
     * Notify a group of data, including group header, group tail, and children
     */
    public void notifyGroupRemoved(int groupPosition) {
        int index = getPositionForGroup(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            mStructures.remove(groupPosition);
            notifyItemRangeRemoved(index, itemCount);
        }
    }

    /**
     * Use {@link #notifyGroupRangeRemoved(int, int)} instead.
     */
    @Deprecated
    public void removeRangeGroup(int groupPosition, int count) {
        notifyGroupRangeRemoved(groupPosition, count);
    }

    /**
     * Notify of multiple groups of data deletion, including group header, group tail, and children
     */
    public void notifyGroupRangeRemoved(int groupPosition, int count) {
        int index = getPositionForGroup(groupPosition);
        int itemCount;
        if (groupPosition + count <= mStructures.size()) {
            itemCount = countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount = countGroupRangeItem(groupPosition, mStructures.size());
        }
        if (index >= 0 && itemCount > 0) {
            mStructures.remove(groupPosition);
            notifyItemRangeRemoved(index, itemCount);
        }
    }

    /**
     * Use {@link #notifyHeaderRemoved(int)} instead.
     */
    @Deprecated
    public void removeHeader(int groupPosition) {
        notifyHeaderRemoved(groupPosition);
    }

    /**
     * notification group header deletion
     */
    public void notifyHeaderRemoved(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasHeader(false);
            notifyItemRemoved(index);
        }
    }

    /**
     * Use {@link #notifyFooterRemoved(int)} instead.
     */
    @Deprecated
    public void removeFooter(int groupPosition) {
        notifyFooterRemoved(groupPosition);
    }

    /**
     * notification of group tail deletion
     */
    public void notifyFooterRemoved(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasFooter(false);
            notifyItemRemoved(index);
        }
    }

    /**
     * Use {@link #notifyChildRemoved(int, int)} instead.
     */
    @Deprecated
    public void removeChild(int groupPosition, int childPosition) {
        notifyChildRemoved(groupPosition, childPosition);
    }

    /**
     * notify a child of a group to delete
     */
    public void notifyChildRemoved(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setChildrenCount(structure.getChildrenCount() - 1);
            notifyItemRemoved(index);
        }
    }

    /**
     * Use {@link #notifyChildRangeRemoved(int, int, int)} instead.
     */
    @Deprecated
    public void removeRangeChild(int groupPosition, int childPosition, int count) {
        notifyChildRangeRemoved(groupPosition, childPosition, count);
    }

    /**
     * notify multiple children of a group to delete
     */
    public void notifyChildRangeRemoved(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int childCount = structure.getChildrenCount();
                int removeCount = count;
                if (childCount < childPosition + count) {
                    removeCount = childCount - childPosition;
                }
                structure.setChildrenCount(childCount - removeCount);
                notifyItemRangeRemoved(index, removeCount);
            }
        }
    }

    /**
     * Use {@link #notifyChildrenRemoved(int)} instead.
     */
    @Deprecated
    public void removeChildren(int groupPosition) {
        notifyChildrenRemoved(groupPosition);
    }

    /**
     *notify all children in a group to delete
     */
    public void notifyChildrenRemoved(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int itemCount = structure.getChildrenCount();
                structure.setChildrenCount(0);
                notifyItemRangeRemoved(index, itemCount);
            }
        }
    }

    /**
     * Use {@link #notifyGroupInserted(int)} instead.
     */
    @Deprecated
    public void insertGroup(int groupPosition) {
        notifyGroupInserted(groupPosition);
    }

    /**
     * notify a group of data insertion
     */
    public void notifyGroupInserted(int groupPosition) {
        GroupStructure structure = new GroupStructure(hasHeader(groupPosition),
                hasFooter(groupPosition), getChildrenCount(groupPosition));
        if (groupPosition < mStructures.size()) {
            mStructures.add(groupPosition, structure);
        } else {
            mStructures.add(structure);
            groupPosition = mStructures.size() - 1;
        }

        int index = countGroupRangeItem(0, groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (itemCount > 0) {
            notifyItemRangeInserted(index, itemCount);
        }
    }

    /**
     * Use {@link #notifyGroupRangeInserted(int, int)} instead.
     */
    @Deprecated
    public void insertRangeGroup(int groupPosition, int count) {
        notifyGroupRangeInserted(groupPosition, count);
    }

    /**
     * notify multiple sets of data insertion
     */
    public void notifyGroupRangeInserted(int groupPosition, int count) {
        ArrayList<GroupStructure> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            GroupStructure structure = new GroupStructure(hasHeader(i),
                    hasFooter(i), getChildrenCount(i));
            list.add(structure);
        }

        if (groupPosition < mStructures.size()) {
            mStructures.addAll(groupPosition, list);
        } else {
            mStructures.addAll(list);
            groupPosition = mStructures.size() - list.size();
        }

        int index = countGroupRangeItem(0, groupPosition);
        int itemCount = countGroupRangeItem(groupPosition, count);
        if (itemCount > 0) {
            notifyItemRangeInserted(index, itemCount);
        }
    }

    /**
     * Use {@link #notifyHeaderInserted(int)} instead.
     */
    @Deprecated
    public void insertHeader(int groupPosition) {
        notifyHeaderInserted(groupPosition);
    }

    /**
     * notification group header insertion
     */
    public void notifyHeaderInserted(int groupPosition) {
        if (groupPosition < mStructures.size() && 0 > getPositionForGroupHeader(groupPosition)) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasHeader(true);
            int index = countGroupRangeItem(0, groupPosition);
            notifyItemInserted(index);
        }
    }

    /**
     * Use {@link #notifyFooterInserted(int)} instead.
     */
    @Deprecated
    public void insertFooter(int groupPosition) {
        notifyFooterInserted(groupPosition);
    }

    /**
     *notification group tail insertion
     */
    public void notifyFooterInserted(int groupPosition) {
        if (groupPosition < mStructures.size() && 0 > getPositionForGroupFooter(groupPosition)) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasFooter(true);
            int index = countGroupRangeItem(0, groupPosition + 1);
            notifyItemInserted(index);
        }
    }

    /**
     * Use {@link #notifyChildInserted(int, int)} instead.
     */
    @Deprecated
    public void insertChild(int groupPosition, int childPosition) {
        notifyChildInserted(groupPosition, childPosition);
    }

    /**
     * notify a child to insert into a group
     */
    public void notifyChildInserted(int groupPosition, int childPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            int index = getPositionForChild(groupPosition, childPosition);
            if (index < 0) {
                index = countGroupRangeItem(0, groupPosition);
                index += structure.hasHeader() ? 1 : 0;
                index += structure.getChildrenCount();
            }
            structure.setChildrenCount(structure.getChildrenCount() + 1);
            notifyItemInserted(index);
        }
    }

    /**
     * Use {@link #notifyChildRangeInserted(int, int, int)} instead.
     */
    @Deprecated
    public void insertRangeChild(int groupPosition, int childPosition, int count) {
        notifyChildRangeInserted(groupPosition, childPosition, count);
    }

    /**
     * notify multiple children of a group to insert
     */
    public void notifyChildRangeInserted(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = countGroupRangeItem(0, groupPosition);
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                index++;
            }
            if (childPosition < structure.getChildrenCount()) {
                index += childPosition;
            } else {
                index += structure.getChildrenCount();
            }
            if (count > 0) {
                structure.setChildrenCount(structure.getChildrenCount() + count);
                notifyItemRangeInserted(index, count);
            }
        }
    }

    /**
     * Use {@link #notifyChildrenInserted(int)} instead.
     */
    @Deprecated
    public void insertChildren(int groupPosition) {
        notifyChildrenInserted(groupPosition);
    }

    /**
     * notify all children in a group
     */
    public void notifyChildrenInserted(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = countGroupRangeItem(0, groupPosition);
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                index++;
            }
            int itemCount = getChildrenCount(groupPosition);
            if (itemCount > 0) {
                structure.setChildrenCount(itemCount);
                notifyItemRangeInserted(index, itemCount);
            }
        }
    }

    /**
     * set group header click event
     */
    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mOnHeaderClickListener = listener;
    }

    /**
     * set group click event
     */
    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    /**
     * set child click event
     */
    public void setOnChildClickListener(OnChildClickListener listener) {
        mOnChildClickListener = listener;
    }

    public abstract int getGroupCount();

    public abstract int getChildrenCount(int groupPosition);

    public abstract boolean hasHeader(int groupPosition);

    public abstract boolean hasFooter(int groupPosition);

    public abstract int getHeaderLayout(int viewType);

    public abstract int getFooterLayout(int viewType);

    public abstract int getChildLayout(int viewType);

    public abstract void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition);

    public abstract void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition);

    public abstract void onBindChildViewHolder(BaseViewHolder holder,
                                               int groupPosition, int childPosition);

    class GroupDataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            isDataChanged = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            isDataChanged = true;
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition);
    }

    public interface OnFooterClickListener {
        void onFooterClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition);
    }

    public interface OnChildClickListener {
        void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition);
    }
}