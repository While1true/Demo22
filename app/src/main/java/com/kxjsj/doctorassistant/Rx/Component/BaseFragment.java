package com.kxjsj.doctorassistant.Rx.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.RxLifeUtils;

/**
 * Created by vange on 2017/9/12.
 */

public abstract class BaseFragment extends Fragment {
    protected Toolbar toolbar;
    protected View view;
    protected boolean viewCreated=false;
    protected boolean firstLoad=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container,true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreated=true;
        initView();
    }

    protected abstract void initView();

    /**
     * 内容布局id
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && viewCreated &&firstLoad){
            firstLoad=false;
            loadLazy();
        }
    }

    protected abstract void loadLazy();

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
