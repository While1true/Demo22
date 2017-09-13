package com.kxjsj.doctorassistant.Net.Rx.Component;

import android.support.v4.app.Fragment;

import com.kxjsj.doctorassistant.Net.Rx.RxLifeUtils;

/**
 * Created by vange on 2017/9/12.
 */

public class BaseFragment extends Fragment {


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
