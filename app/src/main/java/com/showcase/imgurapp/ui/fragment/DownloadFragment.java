package com.showcase.imgurapp.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.showcase.imgurapp.Constants;
import com.showcase.imgurapp.ui.listener.OnDownloadListener;

import java.util.Random;

import timber.log.Timber;

public class DownloadFragment extends Fragment {

    private OnDownloadListener mListener;
    private boolean mSuccess = false;
    private DownloadingTask mDownloadingTask;
    private int mProgress = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to keep the AsyncTask uninterrupted on configuration change (i.e. device rotation)
        setRetainInstance(true); // retain fragment instance when the activity is recreated
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListener != null) {
            if (mSuccess) {
                // the downloading has finished - send the info to the activity
                mListener.onDownloadComplete(true);
                mSuccess = false;
            } else if (mDownloadingTask != null && mDownloadingTask.getStatus() == AsyncTask.Status.RUNNING) {
                // the downloading hasn't finished yet - send the progress info to the activity
                mListener.onDownloadProgressUpdate(mProgress);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDownloadListener) {
            mListener = (OnDownloadListener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        cancelDownloadAsyncTask();
        super.onDestroy();
    }

    public void startDownloadAsyncTask(int max) {
        Timber.d("startDownloadAsyncTask");
        if (mDownloadingTask == null || mDownloadingTask.getStatus() != AsyncTask.Status.RUNNING) {
            mDownloadingTask = new DownloadingTask();
            mDownloadingTask.execute(max);
        }
        mProgress = 0;
    }

    public void cancelDownloadAsyncTask() {
        Timber.d("cancelDownloadAsyncTask");
        if (mDownloadingTask != null && mDownloadingTask.getStatus() == AsyncTask.Status.RUNNING) {
            mDownloadingTask.cancel(true);
            if (mListener != null) {
                mListener.onDownloadCancelled();
            }
        }
        mProgress = 0;
    }

    private class DownloadingTask extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            if (mListener != null) {
                mListener.onDownloadStarted();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                Thread.sleep(2000); // keep "Start downloading..." for 2 sec
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            Random random = new Random();
            for (int i = 0; i <= params[0]; i++) {
                try {
                    // update progress on random intervals between 0 and MAX_DOWNLOAD_UPDATE_INTERVAL ms
                    Thread.sleep((long) (Constants.MAX_DOWNLOAD_UPDATE_INTERVAL * random.nextFloat()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
                publishProgress(i);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress = progress[0];
            if (mListener != null) {
                mListener.onDownloadProgressUpdate(mProgress);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Timber.d("onPostExecute %s", result);
            if (mListener != null) {
                mListener.onDownloadComplete(result);
            } else {
                // keep result for onActivityCreated when mListener becomes available
                mSuccess = result;
            }
        }
    }
}
