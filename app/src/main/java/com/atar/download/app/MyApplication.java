package com.atar.download.app;

import android.app.Application;
import android.application.CommonApplication;

/**
 * @author：atar
 * @date: 2019/10/8
 * @description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CommonApplication.initApplication(this);
    }
}
