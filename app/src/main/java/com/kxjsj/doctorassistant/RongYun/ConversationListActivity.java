package com.kxjsj.doctorassistant.RongYun;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by vange on 2017/9/19.
 */

public class ConversationListActivity extends BaseTitleActivity implements RongYunInitialUtils.onRongConnectSuccess {
    @Override
    protected int getContentLayoutId() {
        return R.layout.conversationlist;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("会话列表");
        RongYunInitialUtils.isReconnect(this,"9nAvAzspHZGaiOA6rHi9ar3H8LGDW6Uc/HBclMSvZCFle5Xi81v0ZOnVeCXbGGmnoV4CtMgswymwuYXE7/3U6Q==",this);
    }
    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {

        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }


    @Override
    public void onSuccess(String userid) {
        enterFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onDestroy: ");
    }
}
