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
    private List<R> cachedData = new ArrayList<>();

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
                List<R> old = new ArrayList<>(cachedData);
                cachedData = lst;
                if (isResetData) adapter.getAdapter().notifyDataSetChanged();
                else syncData(old, lst);
            } else {
                cachedData.clear();
                adapter.getAdapter().notifyDataSetChanged();
            }
        }
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
                int i = cachedData.lastIndexOf(r);
                cachedData.set(i, r);
                adapter.getAdapter().notifyItemChanged(i);
            }
        }
        return hasExits || add(name, r);
    }

    public boolean add(String name, R r) {
        boolean isFirstPush = checkIsDefault();
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
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

    public int getSize() {
        return (cachedData == null) ? 0 : cachedData.size();
    }

    public List<R> getData() {
        return (cachedData == null) ? new ArrayList<R>() : cachedData;
    }

    protected boolean isEqual(R d1, R d2) {
        return d1.isDataEquals(d2);
    }

    private void syncData(List<R> data, List<R> curData) {
        int nlen = data.size();
        int olen = curData.size();
        int previousIndex = 0;
        int step = Math.max(nlen, olen);
        for (int i = 0; i < step; i++) {
            boolean eq = false;
            boolean in = i < olen && i < nlen;
            if (in) {
                eq = isEqual(curData.get(i), data.get(i));
            }
            boolean nextEq = false;
            boolean nextIn = i + 1 < olen && i + 1 < nlen;
            if (nextIn) {
                nextEq = isEqual(curData.get(i + 1), data.get(i + 1));
            }
            if (in) {
                if (nextIn) {
                    if (eq) {
                        previousIndex = i + 1;
                    } else {
                        if (nextEq) {
                            if (i - previousIndex > 0) {
                                adapter.getAdapter().notifyItemRangeChanged(previousIndex, i);
                            } else {
                                adapter.getAdapter().notifyItemChanged(previousIndex);
                            }
                            previousIndex = i + 1;
                        }
                    }
                } else {
                    if (!eq) {
                        if (i - previousIndex > 0) {
                            adapter.getAdapter().notifyItemRangeChanged(previousIndex, i);
                        } else {
                            adapter.getAdapter().notifyItemChanged(previousIndex);
                        }
                        previousIndex = i + 1;
                    }
                }
            } else {
                if (i == step - 1) {
                    if (i >= olen)
                        if (i - previousIndex > 0) {
                            adapter.getAdapter().notifyItemRangeInserted(previousIndex + 1, step);
                        } else {
                            adapter.getAdapter().notifyItemInserted(previousIndex + 1);
                        }
                    if (i >= nlen) {
                        if (i - previousIndex > 0) {
                            adapter.getAdapter().notifyItemRangeRemoved(previousIndex, step);
                            adapter.getAdapter().notifyItemRangeChanged(0, step);
                        } else {
                            adapter.getAdapter().notifyItemRemoved(previousIndex);
                            adapter.getAdapter().notifyItemRangeChanged(0, step);
                        }
                    }
                }
            }
        }
    }

    public void clear(String name) {
        DataSource<R> ds = getOrCreateDs(name);
        if (ds != null) {
            ds.clear();
        }
        getSource().remove(name);
        if (name.equals(curData)) {
            cachedData.clear();
            adapter.getAdapter().notifyDataSetChanged();
        }
    }

    public void clearAll() {
        getSource().clear();
        cachedData.clear();
        adapter.getAdapter().notifyDataSetChanged();
    }
}
