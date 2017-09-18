package com.kxjsj.doctorassistant;

import android.os.Bundle;

import com.kxjsj.doctorassistant.Rx.Component.BaseTitleActivity;


public class MainActivity extends BaseTitleActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("drfedf");

    }

}
