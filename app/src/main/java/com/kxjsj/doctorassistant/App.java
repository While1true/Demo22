package com.kxjsj.doctorassistant;

import android.app.Application;

import com.kxjsj.doctorassistant.Screen.AdjustUtils;


/**
 * Created by vange on 2017/9/6.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();

        AdjustUtils.Adjust(this, AdjustUtils.TYPE_DP);
    }

    private void init() {

    }
}
