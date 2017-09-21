package com.kxjsj.doctorassistant.Mipush;

import android.content.Context;
import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by vange on 2017/9/21.
 */

public class MiPushReceiver extends PushMessageReceiver {

    String mRegId;

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "onNotificationMessageArrived: "+miPushMessage.getTitle());
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onReceiveRegisterResult: "+mRegId);
                //上传到服务器 用于发送推送消息
                //设置别名
                MiPushClient.setAlias(context,"123","123");
            }
        }
    }
}
