package com.showcase.imgurapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.showcase.imgurapp.R;
import com.showcase.imgurapp.ui.fragment.DownloadFragment;
import com.showcase.imgurapp.ui.fragment.FragmentHelper;
import com.showcase.imgurapp.ui.listener.OnDownloadListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class DownloadActivity extends AppCompatActivity implements OnDownloadListener {

    private static final String DOWNLOAD_STATUS = DownloadStatus.class.getName();
    private static final String PROGRESS = "progress";

    @BindView(R.id.download_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.download_text)
    TextView mProgressText;
    @BindView(R.id.button_download)
    Button mButtonDownload;
    @BindView(R.id.button_cancel)
    Button mButtonCancel;

    private DownloadFragment mDownloadFragment;
    private DownloadStatus mDownloadStatus = DownloadStatus.NOT_STARTED;
    private int mProgress = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        mDownloadFragment = FragmentHelper.getDownloadFragment(this);
        updateUI();
    }

    /**
     * Necessary only to keep the final download state if the activity is recreated.
     * Progress update and download completion while the activity recreates
     * are already handled in {@link DownloadFragment#onActivityCreated(Bundle)}.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(DOWNLOAD_STATUS, mDownloadStatus.ordinal());
        outState.putInt(PROGRESS, mProgress);
        super.onSaveInstanceState(outState);
    }

    /**
     * Check {@link #onSaveInstanceState(Bundle)} description.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateDownloadStatus(DownloadStatus.values()[savedInstanceState.getInt(DOWNLOAD_STATUS, 0)],
                savedInstanceState.getInt(PROGRESS, 0));
        Timber.d("%s %d", mDownloadStatus.toString(), mProgress);
    }

    @OnClick(R.id.button_download)
    void startDownload() {
        if (mDownloadFragment != null && mDownloadFragment.isAdded()) {
            mDownloadFragment.startDownloadAsyncTask(mProgressBar.getMax());
        }
    }

    @OnClick(R.id.button_cancel)
    void cancelDownload() {
        if (mDownloadFragment != null && mDownloadFragment.isAdded()) {
            mDownloadFragment.cancelDownloadAsyncTask();
        }
    }

    @Override
    public void onDownloadStarted() {
        updateDownloadStatus(DownloadStatus.STARTING, 0);
    }

    @Override
    public void onDownloadProgressUpdate(int progress) {
        updateDownloadStatus(DownloadStatus.DOWNLOADING, progress);
    }

    @Override
    public void onDownloadComplete(boolean success) {
        if (success) {
            updateDownloadStatus(DownloadStatus.COMPLETE, mProgressBar.getMax());
        } else {
            updateDownloadStatus(DownloadStatus.FAILED, 0);
        }
    }

    @Override
    public void onDownloadCancelled() {
        updateDownloadStatus(DownloadStatus.CANCELLED, 0);
    }

    /**
     * Set download status and progress and call {@link #updateUI()}.
     *
     * @param status   Current download status.
     * @param progress Current progress.
     */
    private void updateDownloadStatus(DownloadStatus status, int progress) {
        mDownloadStatus = status;
        mProgress = progress;
        updateUI();
    }

    /**
     * Set progress bar, progress text and buttons state depending on
     * the current {@link #mProgress} and {@link #mDownloadStatus} values.
     */
    private void updateUI() {
        mProgressBar.setProgress(mProgress);
        switch (mDownloadStatus) {
            case NOT_STARTED:
                enableDownload(true);
                mProgressText.setText(getString(R.string.start_download));
                break;
            case STARTING:
                enableDownload(false);
                mProgressText.setText(getString(R.string.starting_download));
                break;
            case DOWNLOADING:
                enableDownload(false);
                mProgressText.setText(String.format(Locale.getDefault(),
                        "%d%s", mProgress, getString(R.string.percent)));
                break;
            case CANCELLED:
                enableDownload(true);
                mProgressText.setText(getString(R.string.download_cancelled));
                break;
            case FAILED:
                enableDownload(true);
                mProgressText.setText(getString(R.string.download_failed));
                break;
            case COMPLETE:
                enableDownload(true);
                mProgressText.setText(getString(R.string.download_complete));
                break;
        }
    }

    /**
     * Set Download and Cancel button state.
     *
     * @param enable - True to enable downloading. False to disable.
     */
    private void enableDownload(boolean enable) {
        if (enable) {
            mButtonDownload.setEnabled(true);
            mButtonCancel.setEnabled(false);
        } else {
            mButtonDownload.setEnabled(false);
            mButtonCancel.setEnabled(true);
        }
    }

    private enum DownloadStatus {
        NOT_STARTED, STARTING, DOWNLOADING, CANCELLED, FAILED, COMPLETE
    }
}
