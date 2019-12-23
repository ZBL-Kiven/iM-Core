package com.zj.list.groupedadapter.structure;

/**
 * This class is used to record the structure of groups in a group list.
 * Record through GroupStructure whether each group has a head, a tail and the number of children. So that it can be easily calculated
 * The length of the list and the position of the head, tail, and children of each group in the list.
 */
public class GroupStructure {

    private boolean hasHeader;
    private boolean hasFooter;
    private int childrenCount;

    public GroupStructure(boolean hasHeader, boolean hasFooter, int childrenCount) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        this.childrenCount = childrenCount;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }
}
