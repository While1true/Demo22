package com.kxjsj.doctorassistant;

import android.app.Application;


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

//        //屏幕适配
//        int designWidth = 375;
//        new RudenessScreenHelper(this, designWidth).activate();
    }

    private void init() {

    }
}
