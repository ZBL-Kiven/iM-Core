package com.zj.list.multiable;

import android.text.TextUtils;

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

    private LinkedHashMap<String, DataSource<R>> getSource() {
        synchronized (multiAbleDataSource) {
            return multiAbleDataSource;
        }
    }

    private DataSource<R> getOrCreateDs(String name) {
        if (TextUtils.isEmpty(name)) return null;
        if (!getSource().containsKey(name)) {
            int size = getSource().size();
            getSource().put(name, new DataSource<R>(size <= 0, name));
        }
        return getSource().get(name);
    }

    private void notifyCurrentData(boolean isResetData) {
        DataSource<R> ds = getOrCreateDs(curData);
        if (ds != null) {
            List<R> lst = adapter.onBuildData(ds.getData());
            if (lst != null && !lst.isEmpty()) {
                cachedData = lst;
            }
        }
        if (isResetData) adapter.onSourceSet(cachedData, curData);
        else adapter.onDataChanged(cachedData);
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

    boolean isDefaultData(String name) {
        DataSource ds = getOrCreateDs(name);
        return ds != null && ds.isDefault();
    }

    private void changeSource(String name) {
        curData = name;
        notifyCurrentData(true);
    }

    public boolean set(R r, String name, String payloads) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds == null) return false;
        boolean hasExits = ds.contains(r);
        if (hasExits) {
            boolean set = ds.put(r);
            if (name.equals(curData)) {
                adapter.onDataSet(r, payloads);
            }
        }
        return hasExits || add(name, r);
    }

    public boolean add(String name, R r) {
        boolean isFirstPush = checkIsDefault();
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            if (ds.contains(r)) ds.remove(r);
            boolean add = ds.put(r);
            if (isFirstPush) {
                changeSource(name);
            } else if (add && curData.equals(name)) {
                notifyCurrentData(false);
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
                notifyCurrentData(false);
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
                notifyCurrentData(true);
            }
        }
        return false;
    }

    public void removeInAllSource(R r) {
        for (Map.Entry<String, DataSource<R>> entry : getSource().entrySet()) {
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key)) remove(key, r);
        }
    }

    public void remove(String name, R r) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            ds.remove(r);
            if (ds.getName().equals(curData)) {
                notifyCurrentData(false);
            }
        }
    }

    public void removeAll(String name, List<R> r) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            if (ds.getName().equals(curData)) {
                ds.removeAll(r);
                notifyCurrentData(false);
            }
        }
    }

    public void merge(String newName, String... mergedNames) {
        if (TextUtils.isEmpty(newName)) throw new NullPointerException("the merge to source name could not be null!");
        List<String> merges = Arrays.asList(mergedNames);
        merges.remove(newName);
        DataSource<R> newDs = getOrCreateDs(newName);
        if (newDs == null) return;
        for (String s : merges) {
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
            adapter.onDataCleared();
        }
    }

    public void clearAll() {
        adapter.onDataCleared();
        getSource().clear();
    }
}
