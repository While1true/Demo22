package com.kxjsj.doctorassistant.Appxx.Communicate;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean.*;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;
import com.kxjsj.doctorassistant.Screen.AdjustUtil;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vange on 2017/9/29.
 */

public class DoctorHome extends BaseTitleActivity {
    /**
     * 显示内容控件
     */
    @BindView(R.id.srecyclerview)
    SRecyclerView srecyclerview;
    private SBaseMutilAdapter sBaseMutilAdapter;

    /**
     * 数据包
     */
    private List<DoctorInfo> datas;

    @Override
    protected int getContentLayoutId() {

        return R.layout.srecyclerview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "initView: Oncreat");
        ButterKnife.bind(this);
        datas = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            datas.add(new DoctorInfo(i+""));
        }
        sBaseMutilAdapter = new SBaseMutilAdapter(datas)
                .addType(R.layout.doctor_info, new SBaseMutilAdapter.ITEMHOLDER<DoctorInfo>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, DoctorInfo item, int position) {
                        holder.setOnClickListener(R.id.ask, view -> askQuestion(item));
                        holder.setOnClickListener(R.id.communicate, view ->
                                ConversationUtils.startChartSingle(DoctorHome.this, position + "", "XX情况咨询"));
                    }

                    @Override
                    public boolean istype(DoctorInfo item, int position) {
                        return position == 0 && OrentionUtils.isPortrait(DoctorHome.this);
                    }
                })
                .addType(R.layout.doctor_info_land, new SBaseMutilAdapter.ITEMHOLDER<DoctorInfo>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, DoctorInfo item, int position) {
                        holder.setOnClickListener(R.id.ask, view -> askQuestion(item));
                        holder.setOnClickListener(R.id.communicate, view ->
                                ConversationUtils.startChartSingle(DoctorHome.this, position + "", "XX情况咨询"));
                    }

                    @Override
                    public boolean istype(DoctorInfo item, int position) {
                        return position == 0 && !OrentionUtils.isPortrait(DoctorHome.this);
                    }
                })
                .addType(R.layout.doctor_answer_item, new SBaseMutilAdapter.ITEMHOLDER<DoctorInfo>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, DoctorInfo item, int position) {

                    }

                    @Override
                    public boolean istype(DoctorInfo item, int position) {
                        return true;
                    }
                }).setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });
        srecyclerview.addDefaultHeaderFooter()
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        srecyclerview.postDelayed(() -> {
                            loadData();
                            sBaseMutilAdapter.showState(SBaseMutilAdapter.SHOW_NOMORE, "无更多内容了");
                            srecyclerview.notifyRefreshComplete();
                        }, 1000);
                    }
                }).setAdapter(new LinearLayoutManager(this), sBaseMutilAdapter)
                .setRefreshing();
    }

    /**
     * 提交留言问题
     *
     * @param item
     */
    private void askQuestion(DoctorInfo item) {
        new MaterialDialog.Builder(this)
                .title("留言提问")
                .content("留言可能不能及时收到回复，请谅解\n您可以试着先看看别人的提问，也许可以找到你想要的答案")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("提交")
                .negativeText("取消")
                .input("请输入您的问题", "", false, (dialog, input) -> {

                }).onPositive((dialog, which) -> {
            String string = dialog.getInputEditText().getText().toString();
            K2JUtils.toast(string, 1);
        }).build()
                .show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /**
         * 为了不重启调整布局，把doctor_info后缀加了个Land
         */
        AdjustUtil.changeTypeValue(this);
        sBaseMutilAdapter.notifyDataSetChanged();
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onConfigurationChanged: ");
    }

    /**
     * 请求网络获取数据
     */
    private void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onDestroy: ");
        srecyclerview.setRefreshingListener(null);
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
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onSaveInstanceState: ");
    }

}
