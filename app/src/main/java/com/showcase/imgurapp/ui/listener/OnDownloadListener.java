package com.showcase.imgurapp.ui.listener;

public interface OnDownloadListener {

    void onDownloadStarted();

    void onDownloadProgressUpdate(int progress);

    void onDownloadComplete(boolean success);

    void onDownloadCancelled();
}
