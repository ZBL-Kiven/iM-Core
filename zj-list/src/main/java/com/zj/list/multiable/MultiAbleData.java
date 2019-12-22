package com.zj.list.multiable;

public interface MultiAbleData<T> extends Comparable<T> {

    /**
     * Override this method if two data need to be
     * guaranteed unique by some attribute
     */
    boolean equals(Object obj);

    /**
     * Override this method if two data need to be
     * guaranteed unique by some attribute
     */
    int hashCode();

}
