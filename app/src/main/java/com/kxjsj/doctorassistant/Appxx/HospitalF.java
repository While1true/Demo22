package com.kxjsj.doctorassistant.Appxx;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.R;

/**
 * Created by vange on 2017/9/28.
 */

public class HospitalF extends BaseFragment {
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void loadLazy() {

    }
}