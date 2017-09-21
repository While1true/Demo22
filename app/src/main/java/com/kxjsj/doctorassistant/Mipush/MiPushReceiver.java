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

import io.rong.push.platform.MiMessageReceiver;

/**
 * Created by vange on 2017/9/21.
 */

public class MiPushReceiver extends MiMessageReceiver {
//传递给融云处理了，我只拿到resid给服务器来进行自己的推送
    String mRegId;

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = arguments.get(0);
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG, "onCommandResult: "+mRegId);
            }
        }
        super.onCommandResult(context, message);
    }
}
