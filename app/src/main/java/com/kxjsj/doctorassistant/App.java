package com.kxjsj.doctorassistant;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.RongYun.RongYunInitialUtils;
import com.kxjsj.doctorassistant.Screen.AdjustUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * Created by vange on 2017/9/6.
 */

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        new Thread(() -> init()).start();

        AdjustUtils.Adjust(this, AdjustUtils.TYPE_DP);


    }

    private void init() {
        RongYunInitialUtils.init(this);
    }

}
