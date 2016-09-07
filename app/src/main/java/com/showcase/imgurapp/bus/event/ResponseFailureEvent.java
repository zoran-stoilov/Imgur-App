package com.showcase.imgurapp.bus.event;

public class ResponseFailureEvent {

    private Throwable mThrowable;

    public ResponseFailureEvent(Throwable throwable) {
        this.mThrowable = throwable;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }
}
