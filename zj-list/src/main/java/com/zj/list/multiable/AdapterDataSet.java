package com.zj.list.multiable;

interface AdapterDataSet {

    void onSourceSet(String name);

    void onDataInserted(int position);

    void onDataSet(int position,Object payloads);

    void onDataRemoved(int position);

    void onDataRangeRemoved();

    void onDataCleared();

    void onDataRangeInserted(int start, int end, int count);
}
