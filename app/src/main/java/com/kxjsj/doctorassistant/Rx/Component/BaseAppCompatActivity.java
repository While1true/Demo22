package com.kxjsj.doctorassistant.Rx.Component;

import android.support.v7.app.AppCompatActivity;

import com.kxjsj.doctorassistant.Rx.RxLifeUtils;

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
