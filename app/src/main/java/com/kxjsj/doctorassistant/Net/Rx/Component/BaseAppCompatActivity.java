package com.kxjsj.doctorassistant.Net.Rx.Component;

import android.support.v7.app.AppCompatActivity;

import com.kxjsj.doctorassistant.Net.Rx.RxLifeUtils;

/**
 * Created by vange on 2017/9/12.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
