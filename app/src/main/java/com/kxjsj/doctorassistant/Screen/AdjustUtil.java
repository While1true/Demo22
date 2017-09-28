package com.kxjsj.doctorassistant.Screen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import com.kxjsj.doctorassistant.Constant.Constance;

import java.lang.reflect.Field;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by vange on 2017/9/27.
 */

/**
 * both PORT默认宽高 LAND高宽
 */
public class AdjustUtil {
    enum Orention {
        LAND, PORT, BOTH
    }

    private static Point point;
    private static Orention type = Orention.LAND;
    private static int DESIGN_WIDTHs = 1920;
    private static int DESIGN_HEIGHTs = 1200;
    private static float DESIGN_SCALEs = 2.0f;
    public static float screenScale = 0;

    /**
     * 平板配置
     * @param app
     */
    public static void adjust(Application app){
        adjust(app,type,DESIGN_WIDTHs,DESIGN_HEIGHTs,DESIGN_SCALEs);
    }

    /**
     *
     * @param app
     * @param orention 方向
     * @param DESIGN_WIDTH 设计稿宽
     * @param DESIGN_HEIGHT 设计稿高
     * @param DESIGN_SCALE 设计设备密度
     */
    public static void adjust(Application app, Orention orention, int DESIGN_WIDTH, int DESIGN_HEIGHT, float DESIGN_SCALE) {
        type = orention;
        DESIGN_WIDTHs = DESIGN_WIDTH;
        DESIGN_HEIGHTs = DESIGN_HEIGHT;
        DESIGN_SCALEs = DESIGN_SCALE;

        doAdjust(app);
    }

    private static void doAdjust(Application context) {
        /**
         * 和设计尺寸一样不调整
         */
        if (checkIfNotNeedAdjust(context))
            return;

        /**
         * 计算缩放
         */
        calculateScale();

        /**
         * 设置缩放
         */
        applyScale(context);

    }

    /**
     * 改变density
     *
     * @param context
     */
    private static void changeTypeValue(Context context) {
        if (type == Orention.BOTH) {
            /**
             * 和设计尺寸一样不调整
             */
            if (checkIfNotNeedAdjust(context))
                return;
            calculateScale();
        }
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "changeTypeValue: "+point.x+"--"+point.y+"--"+screenScale);
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        displayMetrics.density = screenScale;
        displayMetrics.scaledDensity = screenScale;

        DisplayMetrics metrics = getMetricsOnMiui(resources);
        if (metrics != null) {
            metrics.density = screenScale;
            metrics.scaledDensity = screenScale;
        }
    }

    /**
     * 注册监听调整
     *
     * @param context
     */
    private static void applyScale(Application context) {

        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                changeTypeValue(context);
                changeTypeValue(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 计算scale
     */
    private static void calculateScale() {
        if (point.x > point.y) {
            screenScale = DESIGN_SCALEs * point.x / Math.max(DESIGN_WIDTHs, DESIGN_HEIGHTs);
        } else {
            screenScale = DESIGN_SCALEs * point.x / Math.min(DESIGN_WIDTHs, DESIGN_HEIGHTs);
        }

    }

    /**
     * 相同分辨率屏幕不调整
     *
     * @param context
     * @return
     */
    private static boolean checkIfNotNeedAdjust(Context context) {
        point = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        boolean notAdjust = false;
        if (type == Orention.PORT) {
            notAdjust = (DESIGN_WIDTHs == Math.min(point.x, point.y));
        } else if (type == Orention.LAND) {
            notAdjust = (DESIGN_WIDTHs == Math.max(point.x, point.y));
        } else {
            if (point.x > point.y) {
                notAdjust = (DESIGN_HEIGHTs == point.x);
            } else {
                notAdjust = (DESIGN_WIDTHs == point.x);
            }
        }

        float density = context.getResources().getDisplayMetrics().density;

        return notAdjust && density == DESIGN_SCALEs;
    }

    //解决MIUI更改框架导致的MIUI7+Android5.1.1上出现的失效问题
    // (以及极少数基于这部分miui去掉art然后置入xposed的手机)
    private static DisplayMetrics getMetricsOnMiui(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
