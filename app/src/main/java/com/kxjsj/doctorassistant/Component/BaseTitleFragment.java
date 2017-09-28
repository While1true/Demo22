package com.kxjsj.doctorassistant.Component;

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
import com.kxjsj.doctorassistant.View.IndicateImageView;

/**
 * Created by vange on 2017/9/12.
 */

public abstract class BaseTitleFragment extends Fragment {
    protected Toolbar toolbar;
    private TextView tv_title;
    private ImageView iv_menu;
    protected View view;

    protected boolean viewCreated = false;
    protected boolean firstLoad = true;
    protected IndicateImageView tipTextView;

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
        if(toolbar!=null)
            return;
        LinearLayout content = view.findViewById(R.id.root);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tv_title = view.findViewById(R.id.title);
        iv_menu = view.findViewById(R.id.menu);
        tipTextView = view.findViewById(R.id.message);
        toolbar.setNavigationOnClickListener(view1 -> onNavigationClicked());
        getLayoutInflater().inflate(getLayoutId(), content, true);
        initView(savedInstanceState);
    }

    /**
     * 设置标题
     *
     * @return
     */
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    /**
     * 返回按钮
     */
    protected abstract void onNavigationClicked();

    /**
     * 初始化组件
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 内容布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 右上角菜单
     *
     * @param resId
     * @param listener
     */
    protected void setMenu(int resId, View.OnClickListener listener) {
        iv_menu.setVisibility(View.VISIBLE);
        if (resId != 0)
            iv_menu.setImageResource(resId);
        iv_menu.setOnClickListener(listener);
    }


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
        tv_title=null;
        iv_menu=null;
        RxLifeUtils.getInstance().remove(this);
    }
}
