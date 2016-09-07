package com.showcase.imgurapp.util;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

    public static final int GL_MAX_TEXTURE_SIZE = 4096; // device specific value - should be set dynamically
    public static int SCREEN_HEIGHT = 0;
    private static int SCREEN_WIDTH = 0;

    public static int getScreenWidth(Context context) {
        if (SCREEN_WIDTH == 0) {
            SCREEN_WIDTH = getScreenSize(context).x;
        }
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight(Context context) {
        if (SCREEN_HEIGHT == 0) {
            SCREEN_HEIGHT = getScreenSize(context).y;
        }
        return SCREEN_HEIGHT;
    }

    private static Point getScreenSize(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
