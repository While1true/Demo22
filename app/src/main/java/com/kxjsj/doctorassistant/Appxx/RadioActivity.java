package com.kxjsj.doctorassistant.Appxx;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.Utils.MyToast;
import com.kxjsj.doctorassistant.View.NoScrollViewPager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by vange on 2017/9/19.
 */

public class RadioActivity extends BaseTitleActivity implements RadioGroup.OnCheckedChangeListener, IUnReadMessageObserver {
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private SickbedF sickbedF;
    private CommunicateF communicateF;
    private HospitalF hospitalF;
    private MineF mineF;
    private int checkedID = R.id.rb_sickbed;
    private String[] titles = {"病床管理", "医护监控", "医患交流", "我的"};
    private Disposable subscribe;

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tipTextView.setVisibility(View.VISIBLE);
        tipTextView.setOnClickListener(v -> ConversationUtils.openChartList(this));
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "initView: ------" + (savedInstanceState == null));
        initial(savedInstanceState);
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this

                , Conversation.ConversationType.CHATROOM,
                Conversation.ConversationType.NONE,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP);

        rgGroup.setOnCheckedChangeListener(this);
        rgGroup.check(checkedID);
        vp.setOffscreenPageLimit(4);
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

    }

    /**
     * 初始化
     */
    private void initial(Bundle bundle) {
        if (bundle != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            sickbedF = (SickbedF) supportFragmentManager.getFragment(bundle, "sickbedF");
            communicateF = (CommunicateF) supportFragmentManager.getFragment(bundle, "communicateF");
            hospitalF = (HospitalF) supportFragmentManager.getFragment(bundle, "hospitalF");
            mineF = (MineF) supportFragmentManager.getFragment(bundle, "mineF");
        }
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "initial: " + (sickbedF == null));
        if (sickbedF == null) {
            sickbedF = new SickbedF();
//            supportFragmentManager.beginTransaction().add(sickbedF,"sickbedF");
        }


        if (communicateF == null) {
            communicateF = new CommunicateF();
//            supportFragmentManager.beginTransaction().add(communicateF,"CommunicateF");
        }

        if (hospitalF == null) {
            hospitalF = new HospitalF();
//            supportFragmentManager.beginTransaction().add(hospitalF,"hospitalF");
        }


        if (mineF == null) {
            mineF = new MineF();
//            supportFragmentManager.beginTransaction().add(mineF,"mineF");
        }

        /**
         * 请求权限
         */
        requestPermission();

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
                break;
            case R.id.rb_mine:
                setTitle(titles[3]);
                vp.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 权限申请
     */
    private void requestPermission() {

        subscribe = new RxPermissions(this)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE
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
    protected void onPause() {
        super.onPause();

    }

    private long lastClick;

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        final boolean isStateSaved = fragmentManager.isStateSaved();
        if (isStateSaved && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            // Older versions will throw an exception from the framework
            return;
        }
        if (isStateSaved || !fragmentManager.popBackStackImmediate()) {
            if (System.currentTimeMillis() - lastClick < 300) {
                finish();
                RongIM.getInstance().disconnect();
            } else {
                lastClick = System.currentTimeMillis();
                K2JUtils.toast("再次点击退出", 1);
            }
        }

    }

    @Override
    protected void onNavigationClicked() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onDestroy: ");
        if (!subscribe.isDisposed())
            subscribe.dispose();
        subscribe = null;
        vp.setAdapter(null);
        vp = null;
        rgGroup.setOnCheckedChangeListener(null);
        rgGroup = null;
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        hospitalF = null;
        sickbedF = null;
        communicateF = null;
        mineF = null;
        super.onDestroy();

        MyToast.Companion.cancel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onCreate: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onRestoreInstanceState: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("checkedID", checkedID);
        getSupportFragmentManager().putFragment(outState, "sickbedF", sickbedF);
        getSupportFragmentManager().putFragment(outState, "hospitalF", hospitalF);
        getSupportFragmentManager().putFragment(outState, "communicateF", communicateF);
        getSupportFragmentManager().putFragment(outState, "mineF", mineF);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onSaveInstanceState: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
    }

    @Override
    public void onCountChanged(int i) {
        tipTextView.setIndicate(i);
    }
}
