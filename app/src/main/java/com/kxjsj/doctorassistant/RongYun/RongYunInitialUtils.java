package com.kxjsj.doctorassistant.RongYun;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.xiaomi.mipush.sdk.MiPushClient;

import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;

/**
 * Created by vange on 2017/9/19.
 */

public class RongYunInitialUtils {
    public static void init(Application application) {
        /**
         * miAppId
         * miAppKey
         */
        MiPushClient.registerPush(application,"2882303761517618970", "5841761810970");
        RongPushClient.registerMiPush(application, "2882303761517618970", "5841761810970");
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (application.getApplicationInfo().packageName.equals(getCurProcessName(application))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(application);
            /**
             * 登录状态监听
             */
            RongIM.setConnectionStatusListener(connectionStatus -> {

                switch (connectionStatus){

                    case CONNECTED://连接成功。

                        break;
                    case DISCONNECTED://断开连接。

                        break;
                    case CONNECTING://连接中。

                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。

                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线

                        break;
                }
            });
            /**
             * 收到消息的处理。
             *
             * @param message 收到的消息实体。
             * @param left    剩余未拉取消息数目。
             * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
             */
            RongIM.setOnReceiveMessageListener((message, i) -> {
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onReceived: " + message.getExtra() + i);
                return false;
            });
            /**
             * 聊天消息长按等操作
             */
            RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
                @Override
                public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                    return false;
                }

                @Override
                public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                    return false;
                }

                @Override
                public boolean onMessageClick(Context context, View view, Message message) {
                    return false;
                }

                @Override
                public boolean onMessageLinkClick(Context context, String s) {
                    return false;
                }

                @Override
                public boolean onMessageLongClick(Context context, View view, Message message) {
                    return false;
                }
            });

            /**
             * 用户信息要异步获取
             * 还要获取/**
             * 刷新用户缓存数据。
             *RongIM.getInstance().refreshUserInfoCache(new UserInfo("userId", "啊明", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
             */
            RongIM.setUserInfoProvider(s -> new UserInfo(s, "你好", Uri.parse("https://www.baidu.com/img/bd_logo1.png")), true);
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    private static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public static void connect(String token, final onRongConnectSuccess listener) {
        /**
         * IMKit SDK调用第二步,建立与服务器的连接
         */
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {

                Log.d("LoginActivity", "--onTokenIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override
            public void onSuccess(String userid) {
                listener.onSuccess(userid);
                Log.d("LoginActivity", "--onSuccess" + userid);

            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             *                  http://www.rongcloud.cn/docs/android.html#常见错误码
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

                Log.d("LoginActivity", "--onError" + errorCode);
            }
        });
    }

    /**
     * 判断消息是否是 push 消息
     */
    public static void isReconnect(Activity activity, String token, RongYunInitialUtils.onRongConnectSuccess listener) {

        Intent intent = activity.getIntent();


        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                RongYunInitialUtils.connect(token, listener);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    RongYunInitialUtils.connect(token, listener);
                } else {
                    listener.onSuccess(null);
                }
            }
        }
    }

    public interface onRongConnectSuccess {
        void onSuccess(String userid);
    }
}
