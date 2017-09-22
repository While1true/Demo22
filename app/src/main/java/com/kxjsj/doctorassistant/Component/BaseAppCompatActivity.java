package com.kxjsj.doctorassistant.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kxjsj.doctorassistant.Rx.RxLifeUtils;

/**
 * 包装了toolbarde的activity
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        initView(savedInstanceState);
    }

    /**
     * onCreat下调用
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取内容id
     * @return
     */
    protected abstract int getContentLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
