package com.kxjsj.doctorassistant.Appxx;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationListActivity;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.RongYun.RongYunInitialUtils;
import com.kxjsj.doctorassistant.Rx.BaseObserver;
import com.kxjsj.doctorassistant.View.NoScrollViewPager;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by vange on 2017/9/19.
 */

public class RadioActivity extends BaseTitleActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private SickbedF sickbedF;
    private CommunicateF communicateF;
    private HospitalF hospitalF;
    private MineF mineF;

    String[] titles = {"病床管理", "医护监控", "医患交流", "我的"};

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        tipTextView.setVisibility(View.VISIBLE);
        tipTextView.setOnClickListener(v->ConversationUtils.openChartList(this));


        sickbedF = new SickbedF();
        communicateF = new CommunicateF();
        hospitalF = new HospitalF();
        mineF = new MineF();
        RongIM.connect("R3MgS7AhFNxrhpDztEpoplFxw0DW8w3nZrGY+2LJ4XXkSJIiJ+BLdqkUWbaMjOQlxaJYgGxwsjlPZAoHyx/J+w==", new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onTokenIncorrect: ");
            }

            @Override
            public void onSuccess(String s) {
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onSuccess: ");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onError: ");
            }
        });

        rgGroup.setOnCheckedChangeListener(this);
        rgGroup.check(R.id.rb_sickbed);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return sickbedF;
                    case 1:
                        return hospitalF;
                    case 2:
                        return communicateF;
                    case 3:
                        return mineF;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }

        });

        /**
         * 请求权限
         */
        requestPermission();
        RongIM.getInstance().addUnReadMessageCountChangedObserver(i -> {
            tipTextView.setIndicate(i);
        }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.radio_activity;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.rb_sickbed:
                setTitle(titles[0]);
                vp.setCurrentItem(0, false);
                break;
            case R.id.rb_hospital:
                setTitle(titles[1]);
                vp.setCurrentItem(1, false);
                break;
            case R.id.rb_communicate:
                setTitle(titles[2]);
                vp.setCurrentItem(2, false);
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE,"110","你好");
                break;
            case R.id.rb_mine:
                setTitle(titles[3]);
                vp.setCurrentItem(3, false);
                RongIM.getInstance().startConversationList(this);
                break;
        }
    }

    private void requestPermission(){
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.GET_TASKS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS
                       )
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户已经同意该权限
                        Log.d("RxPermissions", permission.name + " is granted.");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        Log.d("RxPermissions", permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
                        Log.d("RxPermissions", permission.name + " is denied.");
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().disconnect();
    }
}
