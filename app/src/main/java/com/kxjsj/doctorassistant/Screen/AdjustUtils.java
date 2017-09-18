package com.kxjsj.doctorassistant.Screen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import java.lang.reflect.Field;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Adjust by 不听话好孩子 on 2017/9/12
 * 集众家之长的适配工具
 * TYPE_FONT 配合  ConstraintLayout addview 使用
 * TYEP_PX 配合xml中 pt格式使用
 * TYPE_DP 直接使用dp，未测试
 */

public class AdjustUtils {
    /**
     * 0:之缩放字体  1：只缩放pt和字体     2 只缩放dp和字体
     */
    public static final int TYPE_FONT = 0, TYEP_PT = 1, TYPE_DP = 2;
    private static int type;
    private static final int DESIGN_WIDTH = 1080;
    private static final int DESIGN_HEIGHT = 1920;
    private static final float DESIGN_SCALE = 3.0f;
    public static float mScale = 0;
    public static float mFontScalePercentage = 0;
    public static float mFontScale = 0;
    public static float mScaleX = 0;
    public static float mScaleY = 0;
    private static Point point;

    public static void Adjust(final Application application, int typea) {
        type = typea;
        /**
         * 同设计尺寸就直接返回
         */
        checkIfNotNeedAdjust(application);
//        if (checkIfNotNeedAdjust(application))
//            return;
        /**
         * 获取相关参数
         */
        getSacleXY(application);

        /**
         * 注册全局调整
         */
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AdjustApplicationDensity(application);
                AdjustApplicationDensity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                AdjustApplicationDensity(application);
                AdjustApplicationDensity(activity);
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
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private static boolean checkIfNotNeedAdjust(Context context) {
        point = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        float density = context.getResources().getDisplayMetrics().density;
        return point.x == DESIGN_WIDTH && density == DESIGN_SCALE;
    }

    /**
     * 计算相关参数
     *
     * @param context
     */
    private static void getSacleXY(Context context) {
        //横屏
        if (point.x > point.y) {
            mScaleX = (point.x * 1.0f / DESIGN_HEIGHT);
            mScaleY = (point.y * 1.0f / DESIGN_WIDTH);
        } else {//竖屏
            mScaleX = (point.x * 1.0f / DESIGN_WIDTH);
            mScaleY = (point.y * 1.0f / DESIGN_HEIGHT);
        }
//        //等宽缩放
//        mScaleY = mScaleX;

        float density = DESIGN_SCALE / context.getResources().getDisplayMetrics().density;
        float scaleDensity = DESIGN_SCALE / context.getResources().getDisplayMetrics().scaledDensity;
        float minScale = Math.min(mScaleX, mScaleY);
        mScale = minScale * density;
        mScaleX *= density;
        mScaleY *= density;
        mFontScalePercentage = minScale * scaleDensity;
        mFontScale = minScale * scaleDensity * context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 改变全局density参数
     *
     * @param context
     */
    private static void AdjustApplicationDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();


        if (type == TYPE_FONT) {
            displayMetrics.scaledDensity = mFontScalePercentage;

            DisplayMetrics metrics = getMetricsOnMiui(resources);
            if (metrics != null)
                metrics.scaledDensity = mFontScale;
        } else if (type == TYPE_DP) {
            displayMetrics.density = mScaleX;
            displayMetrics.scaledDensity = mFontScale;

            DisplayMetrics metrics = getMetricsOnMiui(resources);
            if (metrics != null) {
                metrics.density = mScaleX;
                metrics.scaledDensity = mFontScale;
            }
        } else if (type == TYEP_PT) {
            displayMetrics.xdpi = 72f * point.x / DESIGN_WIDTH;
            displayMetrics.scaledDensity = mFontScale;
            DisplayMetrics metrics = getMetricsOnMiui(resources);
            if (metrics != null) {
                metrics.xdpi = 72f * point.x / DESIGN_WIDTH;
                metrics.scaledDensity = mFontScale;
            }
        }
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


    //--------------------------------------------   layoutAdjust-------------------------------


    /**
     * 配合fontAdjust  type==0时
     *
     * @param child
     * @param params
     */
    public static void adjustConstraintLayout(View child, ViewGroup.LayoutParams params) {
        checkIfNotNeedAdjust(child.getContext());
//        if (checkIfNotNeedAdjust(child.getContext()))
//            return;
        transformSize(child, (ConstraintLayout.LayoutParams) params);
        if (child instanceof ViewGroup && !(child instanceof ConstraintLayout))
            initChildViewGroup((ViewGroup) child, (ViewGroup.MarginLayoutParams) params);
    }

    /**
     * 遍历子类适配
     *
     * @param groups
     */
    private static void initChildViewGroup(ViewGroup groups, ViewGroup.MarginLayoutParams layoutParams) {
        for (int i = 0; i < groups.getChildCount(); i++) {
            View view = groups.getChildAt(i);
            if (!(view instanceof ConstraintLayout)) {

                transformSize(view, layoutParams);
            }
            if (view instanceof ViewGroup)
                initChildViewGroup((ViewGroup) view, (ViewGroup.MarginLayoutParams) view.getLayoutParams());
        }
    }

    /**
     * 调整Layout
     *
     * @param child
     * @param params
     */
    private static void transformSize(View child, ViewGroup.MarginLayoutParams params) {
        if (params.width > 0 && params.height > 0) {//按比列
            params.width *= mScale;
            params.height *= mScale;
        } else {
            //width
            if (params.width > 0) {
                params.width *= mScaleX;
            }
            //height
            if (params.height > 0) {
                params.height *= mScaleY;
            }
        }


        //margin
        params.leftMargin *= mScaleX;
        params.topMargin *= mScaleY;
        params.rightMargin *= mScaleX;
        params.bottomMargin *= mScaleY;
        //padding
        int paddingLeft = (int) (child.getPaddingLeft() * mScaleX);
        int paddingTop = (int) (child.getPaddingTop() * mScaleY);
        int paddingRight = (int) (child.getPaddingRight() * mScaleX);
        int paddingBottom = (int) (child.getPaddingBottom() * mScaleY);
        child.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }


}
