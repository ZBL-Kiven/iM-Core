

package com.zj.preview.downloader.database;

import java.util.List;



public class NoOpsDbHelper implements DbHelper {

    public NoOpsDbHelper() {

    }

    @Override
    public DownloadModel find(int id) {
        return null;
    }

    @Override
    public void insert(DownloadModel model) {

    }

    @Override
    public void update(DownloadModel model) {

    }

    @Override
    public void updateProgress(int id, long downloadedBytes, long lastModifiedAt) {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<DownloadModel> getUnwantedModels(int days) {
        return null;
    }

    @Override
    public void clear() {

    }
}
