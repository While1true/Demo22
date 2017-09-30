package com.kxjsj.doctorassistant.Appxx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Appxx.Communicate.DoctorHome;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean.Doctor;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vange on 2017/9/19.
 */

public class CommunicateF extends BaseFragment {
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    int spancount;
    private SBaseMutilAdapter baseMutilAdapter;
    private GridLayoutManager manager;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setRetainInstance(true);
        /**
         * 销毁重建的不重新初始化
         */
        if (savedInstanceState != null&&srecyclerview!=null) {
            /**
             * 屏幕旋转后activity重建
             * 只要改变spancount 通知layout更新
             *
             */
            caculateSpanCount();
            manager.setSpanCount(spancount);
            baseMutilAdapter.notifyDataSetChanged();
            return;
        }
    }

    /**
     * 调到医生主页
     * @param bean
     */
    private void go2DoctorHome(Doctor bean) {
        startActivity(new Intent(getContext(), DoctorHome.class));
    }

    private void caculateSpanCount() {
        if (OrentionUtils.isPortrait(getContext()))
            spancount = 3;
        else
            spancount = 4;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.srecyclerview;
    }

    @Override
    protected void loadLazy() {
        caculateSpanCount();

        ButterKnife.bind(this, view);

        List<Doctor> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                Doctor bean1 = new Doctor(0, "xxx科" + (i / 10) + "医师", "共10人");
                list.add(bean1);
            }
            Doctor bean = new Doctor(1, "xxx科" + (i / 10) + "医师", "xxx"+i%10+"医生");
            list.add(bean);
        }


        manager = new GridLayoutManager(getContext(), spancount);
        baseMutilAdapter = new SBaseMutilAdapter(list)
                .addType(R.layout.sickbed_item_title, new SBaseMutilAdapter.ITEMHOLDER<Doctor>() {

                    @Override
                    public void onBind(SimpleViewHolder holder, Doctor item, int position) {
                        holder.setText(R.id.title, item.getTitle());
                    }

                    @Override
                    public boolean istype(Doctor item, int position) {
                        return item.getType() == 0;
                    }

                    @Override
                    protected int gridSpanSize(Doctor item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.doctor, new SBaseMutilAdapter.ITEMHOLDER<Doctor>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, Doctor item, int position) {
                        holder.itemView.setOnClickListener(view -> go2DoctorHome(item));
                        holder.setText(R.id.doctor, item.getName());
                    }

                    @Override
                    public boolean istype(Doctor item, int position) {
                        return item.getType() == 1;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        K2JUtils.toast("cuole", 1);
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setAdapter(manager, baseMutilAdapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        srecyclerview.postDelayed(() -> {
                            baseMutilAdapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "无更多内容了");
                            srecyclerview.notifyRefreshComplete();
                        }, 1000);

                    }
                }).setRefreshing();

    }
}
