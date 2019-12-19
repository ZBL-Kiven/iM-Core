

package com.zj.preview.downloader.httpclient;

import androidx.annotation.NonNull;
import com.zj.preview.downloader.request.DownloadRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;



public interface HttpClient extends Cloneable {

    @NonNull
    HttpClient clone();

    void connect(DownloadRequest request) throws IOException;

    int getResponseCode() throws IOException;

    InputStream getInputStream() throws IOException;

    long getContentLength();

    String getResponseHeader(String name);

    void close();

    Map<String, List<String>> getHeaderFields();

    InputStream getErrorStream() throws IOException;

}
