package com.kxjsj.doctorassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kxjsj.doctorassistant.Rx.Component.BaseTitleActivity;


public class MainActivity extends BaseTitleActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected CharSequence setTitle() {
        return "到年底就";
    }
}
