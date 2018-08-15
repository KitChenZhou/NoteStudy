package com.d22395.android.servicebestpra;

/**
 * Created by D22395 on 2017/9/14.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
