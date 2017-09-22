package com.kxjsj.doctorassistant.RongYun;

import android.content.Context;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by vange on 2017/9/19.
 */

public class ConversationUtils {
    /**
     * 单人会话
     *
     * @param context
     * @param RongUseId
     * @param title
     */
    public static void startChartSingle(Context context, String RongUseId, String title) {
        RongIM.getInstance().startPrivateChat(context, RongUseId, title);
    }

    public static void openChartList(Context context) {
        RongIM.getInstance().startConversationList(context);
    }

    public static void creatGroupChart(String title, List<String> RongIds, RongIMClient.CreateDiscussionCallback callback) {
        /**
         *创建讨论组时，mLists为要添加的讨论组成员，创建者一定不能在 mLists 中
         */
        RongIM.getInstance().getRongIMClient().createDiscussion(title, RongIds, callback);
    }
}
