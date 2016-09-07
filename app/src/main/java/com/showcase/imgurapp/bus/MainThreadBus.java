package com.showcase.imgurapp.bus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Singleton class providing an instance of Bus that works on any thread, but posts only to the main thread
 */
public class MainThreadBus extends Bus {

    private static final ThreadEnforcer THREAD_ENFORCER = ThreadEnforcer.ANY;
    private static MainThreadBus mainThreadBus;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private MainThreadBus(ThreadEnforcer enforcer) {
        super(enforcer);
    }

    public static MainThreadBus getInstance() {
        if (mainThreadBus == null) {
            mainThreadBus = new MainThreadBus(THREAD_ENFORCER);
        }
        return mainThreadBus;
    }

    public void post(final Object event) {
        // post events only to the main thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            MainThreadBus.super.post(event);
        } else {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}
