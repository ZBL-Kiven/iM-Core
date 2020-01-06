package com.zj.list.multiable;

import android.text.TextUtils;
import androidx.annotation.NonNull;

import java.util.*;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MultiDataSource<R extends MultiAbleData<R>, T extends AdapterDataSet<R>> {

    public MultiDataSource(T adapter) {
        this.adapter = adapter;
        this.multiAbleDataSource = new LinkedHashMap<>();
    }

    private final LinkedHashMap<String, DataSource<R>> multiAbleDataSource;
    private final T adapter;
    private String curData;
    private List<R> cachedData;
    private boolean notifyChanged = false;

    private LinkedHashMap<String, DataSource<R>> getSource() {
        synchronized (multiAbleDataSource) {
            return multiAbleDataSource;
        }
    }

    private DataSource<R> getOrCreateDs(String name) {
        if (TextUtils.isEmpty(name)) return null;
        if (!getSource().containsKey(name)) {
            int size = getSource().size();
            getSource().put(name, new DataSource<R>(size, size <= 0, name));
        }
        return getSource().get(name);
    }

    @NonNull
    private List<R> getCurrentData() {
        if (notifyChanged || (cachedData == null || cachedData.isEmpty())) {
            DataSource<R> ds = getOrCreateDs(curData);
            if (ds != null) {
                List<R> lst = adapter.onBuildData(ds.getData());
                if (lst != null && !lst.isEmpty()) {
                    cachedData = lst;
                    notifyChanged = false;
                }
            }
        }
        return cachedData;
    }

    private List<R> getDataList() {
        return null;
    }


    private boolean checkIsDefault() {
        return TextUtils.isEmpty(curData) || getSource().isEmpty();
    }

    public int maxCurDataPosition() {
        DataSource<R> ds = getOrCreateDs(curData);
        if (ds == null) return 0;
        return ds.maxPosition();
    }

    public boolean hasSource(String name) {
        return multiAbleDataSource.containsKey(name);
    }

    public boolean isCurData(String name) {
        return name.equals(curData);
    }

    public int getCount() {
        if (TextUtils.isEmpty(curData)) return 0;
        return getCurrentData().size();
    }

    public R getDataWithPosition(int position) {
        List<R> curData = getCurrentData();
        if (position < 0 || position >= curData.size()) return null;
        return curData.get(position);
    }

    boolean isDefaultData(String name) {
        DataSource ds = getOrCreateDs(name);
        return ds != null && ds.isDefault();
    }

    public void changeSource(String name) {
        curData = name;
        notifyChanged = true;
        adapter.onSourceSet(name);
    }

    public boolean set(R r, String addWhereIfNotExits, String payLoads) {
        boolean hasExits = false;
        for (Map.Entry<String, DataSource<R>> entry : getSource().entrySet()) {
            DataSource<R> ds = getSource().get(entry.getKey());
            if (ds != null) {
                hasExits = ds.contains(r);
                if (hasExits) {
                    notifyChanged = true;
                    boolean set = ds.put(r);
                    if (set && entry.getKey().equals(curData)) {
                        int index = getCurrentData().lastIndexOf(r);
                        adapter.onDataSet(index, payLoads);
                    } else {
                        hasExits = false;
                    }
                }
            }
        }
        return hasExits || add(addWhereIfNotExits, r);
    }

    public boolean add(String name, R r) {
        boolean isFirstPush = checkIsDefault();
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            if(ds.contains(r)) ds.remove(r);
            boolean add = ds.put(r);
            if (isFirstPush) {
                changeSource(name);
            } else if (add && curData.equals(name)) {
                notifyChanged = true;
                adapter.onDataInserted(ds.getCount());
            }
            return add;
        }
        return false;
    }

    public boolean addAll(String name, Collection<R> r) {
        boolean isFirstPush = checkIsDefault();
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            int start = ds.getCount();
            boolean add = ds.putAll(r);
            if (isFirstPush) {
                changeSource(name);
            } else if (add && curData.equals(name)) {
                notifyChanged = true;
                adapter.onDataRangeInserted(start, ds.getCount(), ds.getCount());
            }
            return add;
        }
        return false;
    }

    public boolean setData(String name, List<R> lst) {
        boolean isFirstPush = checkIsDefault();
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            boolean r = ds.set(lst);
            if (isFirstPush) {
                changeSource(name);
            } else if (name.equals(curData)) {
                notifyChanged = true;
                adapter.onSourceSet(name);
            }
        }
        return false;
    }

    public void remove(R r) {
        for (Map.Entry<String, DataSource<R>> entry : getSource().entrySet()) {
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key)) remove(key, r);
        }
    }

    public void remove(String name, R r) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            if (ds.getName().equals(curData)) {
                int index = getCurrentData().indexOf(r);
                ds.remove(r);
                notifyChanged = true;
                adapter.onDataRemoved(index);
            } else {
                ds.remove(r);
            }
        }
    }

    public void removeAll(String name, List<R> r) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            if (ds.getName().equals(curData)) {
                ds.removeAll(r);
                notifyChanged = true;
                adapter.onDataRangeRemoved();
            }
        }
    }

    public void merge(String newName, String... mergedNames) {
        if (TextUtils.isEmpty(newName)) throw new NullPointerException("the merge to source name could not be null!");
        List<String> merges = Arrays.asList(mergedNames);
        boolean hasInnerByNew = false;
        if (hasSource(newName)) {
            merges.remove(newName);
        }
        DataSource<R> newDs = getOrCreateDs(newName);
        if (newDs == null) return;
        for (String s : mergedNames) {
            DataSource<R> mds = getOrCreateDs(s);
            if (mds != null) {
                newDs.putAll(mds.getOriginalSet());
            }
        }
        changeSource(newName);
    }

    public void clear(String name) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            ds.clear();
        }
        getSource().remove(name);
        if (name.equals(curData)) {
            notifyChanged = true;
            adapter.onDataCleared();
        }
    }

    public void clearAll() {
        for (Map.Entry<String, DataSource<R>> entry : getSource().entrySet()) {
            DataSource<R> ds = entry.getValue();
            if (ds != null) {
                ds.clear();
            }
        }
        getSource().clear();
        notifyChanged = true;
        adapter.onDataCleared();
    }
}
