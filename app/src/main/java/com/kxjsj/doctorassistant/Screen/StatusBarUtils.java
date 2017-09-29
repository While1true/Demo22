package com.kxjsj.doctorassistant.Screen;

import android.content.Context;
import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;

/**
 * Created by vange on 2017/9/29.
 */

public class StatusBarUtils {
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + "--", "getStatusBarHeight: "+result);
        return result;
    }
}
