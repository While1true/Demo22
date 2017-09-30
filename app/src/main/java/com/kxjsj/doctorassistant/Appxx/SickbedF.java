package com.kxjsj.doctorassistant.Appxx;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseFragment;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/9/19.
 */

/**
 * setsetRetainInstance(true)
 * ondestory不会调用
 * initView中view不用重新初始化
 * 由于横竖屏个数要改一下，就通知更新
 * Srecyclerview中onDetachFromWindow销毁了一部分View 所以要重新初始化头布局
 *
 * 控件不懂使用的参考我的简书页
 * **************  http://www.jianshu.com/u/07d24a532308  *******************
 */
public class SickbedF extends BaseFragment {
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

        caculateSpanCount();

        ButterKnife.bind(this, view);

        List<KotlinBean.SickBedBean> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                KotlinBean.SickBedBean bean1 = new KotlinBean.SickBedBean(0, "第" + (i / 10) + "号楼", "第" + i % 10 + "床");
                list.add(bean1);
            }
            KotlinBean.SickBedBean bean = new KotlinBean.SickBedBean(1, "第" + (i / 10) + "号楼", "第" + i % 10 + "床");
            list.add(bean);
        }


        manager = new GridLayoutManager(getContext(), spancount);
        baseMutilAdapter = new SBaseMutilAdapter(list)
                .addType(R.layout.sickbed_item_title, new SBaseMutilAdapter.ITEMHOLDER<KotlinBean.SickBedBean>() {

                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.SickBedBean item, int position) {
                        holder.setText(R.id.title, item.getTitle());
                    }

                    @Override
                    public boolean istype(KotlinBean.SickBedBean item, int position) {
                        return item.getType() == 0;
                    }

                    @Override
                    protected int gridSpanSize(KotlinBean.SickBedBean item, int position) {
                        return manager.getSpanCount();
                    }
                }).addType(R.layout.sickbed_item_bed, new SBaseMutilAdapter.ITEMHOLDER<KotlinBean.SickBedBean>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, KotlinBean.SickBedBean item, int position) {
                        holder.itemView.setOnClickListener(view -> K2JUtils.toast(item.getName(),1));
                        holder.setText(R.id.textView, item.getName());
                    }

                    @Override
                    public boolean istype(KotlinBean.SickBedBean item, int position) {
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onSaveInstanceState: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
    }
}
