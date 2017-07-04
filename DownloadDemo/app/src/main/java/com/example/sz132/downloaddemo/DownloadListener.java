package com.example.sz132.downloaddemo;

/**
 * Created by sz132 on 2017/7/3.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
