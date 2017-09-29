package com.kxjsj.doctorassistant.RongYun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.R;


import java.util.Locale;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by vange on 2017/9/19.
 */

public class ConversationActivity extends BaseTitleActivity implements RongYunInitialUtils.onRongConnectSuccess {

    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected int getContentLayoutId() {
        return R.layout.conversation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();

        getIntentDate(intent);

        RongYunInitialUtils.isReconnect(this,"9nAvAzspHZGaiOA6rHi9ar3H8LGDW6Uc/HBclMSvZCFle5Xi81v0ZOnVeCXbGGmnoV4CtMgswymwuYXE7/3U6Q==",this);

    }
    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        setTitle(mTargetId);
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
        fragment.setRetainInstance(true);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    @Override
    public void onSuccess(String userid) {
        enterFragment(mConversationType, mTargetId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
