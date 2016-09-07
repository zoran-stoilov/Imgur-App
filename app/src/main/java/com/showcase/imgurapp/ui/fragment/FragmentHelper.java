package com.showcase.imgurapp.ui.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class FragmentHelper {

    private static final String DOWNLOAD_FRAGMENT = DownloadFragment.class.getName();

    public static void setMainFragment(AppCompatActivity activity, int containerResId) {
        MainFragment fragment = new MainFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(containerResId, fragment)
                .commit();
    }

    /**
     * Create and add the DownloadFragment to the activity state if it's not already added and
     * get the DownloadFragment instance by tag.
     *
     * @param activity The activity this fragment should communicate with.
     * @return Instance of a non-UI DownloadFragment.
     */
    public static DownloadFragment getDownloadFragment(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        DownloadFragment downloadFragment = (DownloadFragment) fragmentManager
                .findFragmentByTag(FragmentHelper.DOWNLOAD_FRAGMENT);
        if (downloadFragment == null) {
            downloadFragment = new DownloadFragment();
            fragmentManager.beginTransaction().add(downloadFragment, FragmentHelper.DOWNLOAD_FRAGMENT).commit();
        }
        return downloadFragment;
    }
}
