package com.android.imgursample;

import android.app.Application;

/**
 * Created by kaplanf on 11/11/2016.
 */

public class ImgurApplication extends Application {

    private static ImgurApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static ImgurApplication getInstance() {
        if (singleton == null)
            singleton = new ImgurApplication();
        return singleton;
    }
}
