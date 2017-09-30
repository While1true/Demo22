package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.kxjsj.doctorassistant.Component.BaseDialogFragment;

/**
 * Created by vange on 2017/9/30.
 */

public class LoginDialog extends BaseDialogFragment {
    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public LoginDialog show(FragmentManager manager) {
        return (LoginDialog) super.show(manager);
    }
}
