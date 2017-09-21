package com.kxjsj.doctorassistant.RongYun;

import android.content.Context;
import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by vange on 2017/9/20.
 */

public class RongPushReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "onNotificationMessageArrived: "+pushNotificationMessage.getPushTitle()+pushNotificationMessage.getExtra());
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
