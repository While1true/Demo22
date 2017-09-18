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

public abstract class BaseTitleFragment extends Fragment {
    protected Toolbar toolbar;
    private TextView tv_title;
    private ImageView iv_menu;
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
        LinearLayout content=view.findViewById(R.id.root);
        toolbar=view.findViewById(R.id.toolbar);
        tv_title = view.findViewById(R.id.title);
        tv_title.setText(setTitle());
        iv_menu = view.findViewById(R.id.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationClicked();
            }
        });
        content.addView(getLayoutInflater().inflate(getLayoutId(),content,true));
        initView();
    }

    /**
     * 设置标题
     * @return
     */
    protected abstract CharSequence setTitle();

    /**
     * 返回按钮
     */
    protected abstract void onNavigationClicked();
    protected abstract void initView();

    /**
     * 内容布局id
     * @return
     */
    protected abstract int getLayoutId();
    /**
     * 右上角菜单
     * @param resId
     * @param listener
     */
    protected void setMenu(int resId, View.OnClickListener listener){
        iv_menu.setVisibility(View.VISIBLE);
        if(resId!=0)
            iv_menu.setImageResource(resId);
        iv_menu.setOnClickListener(listener);
    }


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
