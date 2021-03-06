package com.kxjsj.doctorassistant.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.Rx.RxLifeUtils;

/**
 * BaseFragment base
 */

public abstract class BaseFragment extends Fragment {
    protected Toolbar toolbar;
    protected View view;
    protected boolean viewCreated = false;
    protected boolean firstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        viewCreated = true;
    }


    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 内容布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && viewCreated && firstLoad) {
            firstLoad = false;
            loadLazy();
        }
    }

    /**
     * 懒加载
     */
    protected abstract void loadLazy();


    @Override
    public void onDestroy() {
        super.onDestroy();
        view=null;
        toolbar=null;
        RxLifeUtils.getInstance().remove(this);
    }
}
