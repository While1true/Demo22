package com.kxjsj.doctorassistant.Appxx;

import android.os.Bundle;
import android.view.View;

import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Utils.MyToast;

/**
 * Created by vange on 2017/9/19.
 */

public class SickbedF extends BaseFragment {
    @Override
    protected void initView(Bundle savedInstanceState) {
        view.findViewById(R.id.ccc).setOnClickListener(view -> MyToast.Companion.showToasteLong("sdsshjjjjjjjjjjjjjjjjjjjj", 1));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void loadLazy() {

    }

}
