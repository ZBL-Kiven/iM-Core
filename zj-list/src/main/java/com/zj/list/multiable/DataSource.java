package com.zj.list.multiable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zj.list.utlis.QuickSort;

import java.util.*;

@SuppressWarnings({"unused", "WeakerAccess"})
class DataSource<T extends MultiAbleData<T>> implements Comparable<DataSource> {

    private int weights;
    private boolean isDefault;
    private final HashSet<T> data;
    private final String name;

    DataSource(int weights, boolean isDefault, String name) {
        this.weights = weights;
        this.isDefault = isDefault;
        this.name = name;
        this.data = new HashSet<>();
    }

    int getCount() {
        return data.size();
    }

    int getWeights() {
        return weights;
    }

    boolean isDefault() {
        return isDefault;
    }

    String getName() {
        return name;
    }

    @Nullable
    List<T> getData() {
        List<T> lst = new ArrayList<>(data);
        if (lst.isEmpty()) return lst;
        QuickSort.sort(lst);
        return lst;
    }

    HashSet<T> getOriginalSet() {
        return data;
    }

    int maxPosition() {
        return size() - 1;
    }

    int size() {
        return data.size();
    }

    boolean contains(@NonNull T t) {
        return data.contains(t);
    }

    boolean put(@NonNull T t) {
        return data.add(t);
    }

    boolean putAll(@NonNull Collection<T> t) {
        return data.addAll(t);
    }

    void remove(T t) {
        data.remove(t);
    }

    void removeAll(Collection<T> t) {
        data.removeAll(t);
    }

    boolean set(@NonNull Collection<T> t) {
        data.clear();
        if (!t.isEmpty()) {
            return data.addAll(t);
        }
        return true;
    }

    void clear() {
        data.clear();
    }

    @Override
    public int compareTo(@NonNull DataSource dataSource) {
        int result = 0;
        if (dataSource.weights > weights) result = -1;
        if (dataSource.weights < weights) result = 1;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataSource)) return false;
        return name.equals(((DataSource) obj).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
