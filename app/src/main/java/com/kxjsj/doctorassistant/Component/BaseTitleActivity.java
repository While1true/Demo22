package com.kxjsj.doctorassistant.Component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.View.IndicateImageView;

/**
 * Created by vange on 2017/9/12.
 */

public abstract class BaseTitleActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private TextView tv_title;
    private ImageView iv_menu;
    protected IndicateImageView tipTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlebar_layout);
        LinearLayout content = findViewById(R.id.root);
        toolbar = findViewById(R.id.toolbar);
        tipTextView = findViewById(R.id.message);
        toolbar.setTitle("");
        tv_title = findViewById(R.id.title);
        iv_menu = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onNavigationClicked());
        getLayoutInflater().inflate(getContentLayoutId(), content, true);
        initView(savedInstanceState);
    }
    protected abstract int getContentLayoutId();

    /**
     * 初始化组件
     */
    protected abstract void initView( Bundle savedInstanceState);

    /**
     * 中间title
     *
     * @CharSequence title
     */
    public void setTitle(CharSequence title){
        tv_title.setText(title);
    }

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

    /**
     * 右上角menu菜单
     *
     * @param menuid
     * @param listener
     */
    protected void inflateMenu(int menuid, Toolbar.OnMenuItemClickListener listener) {
        toolbar.inflateMenu(menuid);
        toolbar.setOnMenuItemClickListener(listener);
    }

    /**
     * 导航返回键按钮
     */
    protected void onNavigationClicked() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar.setNavigationOnClickListener(null);
        toolbar=null;
        tv_title=null;
        iv_menu=null;
        tipTextView=null;
        RxLifeUtils.getInstance().remove(this);
    }
}
