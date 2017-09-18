package com.kxjsj.doctorassistant.Rx.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kxjsj.doctorassistant.Rx.RxLifeUtils;

/**
 * Created by vange on 2017/9/12.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        initView(savedInstanceState);
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract int getContentLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
