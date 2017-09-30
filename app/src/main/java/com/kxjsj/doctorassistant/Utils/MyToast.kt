package com.kxjsj.doctorassistant.Utils

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

import com.kxjsj.doctorassistant.App
import com.kxjsj.doctorassistant.R
import com.kxjsj.doctorassistant.Screen.AdjustUtil
import com.kxjsj.doctorassistant.Screen.StatusBarUtils
import dp2px

/**
 * Created by vange on 2017/9/27.
 */

/**
 * toast控件
 */
class MyToast private constructor(context: Context) {

    /**
     * toaste文字控件
     */
    private val textView: TextView

    /**
     * 懒加载toast
     */
    private val toast by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Toast(context) }


    init {
        /**
         * 设置顶部且宽度全屏
         */
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)

        textView = TextView(context)

        /**
         * 设置textview左边drawable
         */
        val drawable = context.resources.getDrawable(R.drawable.ic_toastinfo)
        drawable.setBounds(0, 0, dp2px(40f), dp2px(40f))
        textView.setCompoundDrawables(drawable, null, null, null)
        textView.compoundDrawablePadding = dp2px(15f)

        /**
         * textview属性
         */
        textView.setBackgroundColor(0xFF79C4A0.toInt())
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
        textView.setTextColor((0xffffffff).toInt())
        textView.gravity = Gravity.CENTER

        var statusbarHeight=StatusBarUtils.getStatusBarHeight(context)
        val actuallyHeight : Int=(statusbarHeight*AdjustUtil.originalScreenScale/AdjustUtil.screenScale).toInt()

        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actuallyHeight + dp2px(56f))
        textView.setPadding(dp2px(15f), actuallyHeight, dp2px(70f), 0)
        textView.layoutParams = layoutParams

        toast.view = textView
        /**
         * 反射获取getWindowParams 设置盖住状态栏 需要权限抛弃
         */
        try {
            val method = toast.javaClass.getDeclaredMethod("getWindowParams")
            val layoutParams1 = method.invoke(toast) as WindowManager.LayoutParams
            layoutParams1.height = 55 + dp2px(56f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /**
         * 设置可以显示到状态栏上
         */
        toast.view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    }

    /**
     * 同伴对象
     */
    companion object {

        /**
         * 懒加载单例
         */
        private val myToast by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MyToast(App.app) }


        /**
         * 显示toast
         * @param text
         * @param during
         */
        fun showToaste(text: CharSequence, during: Int) {
                myToast.textView.text = text
                myToast.toast.duration = during
                myToast.toast.show()
        }

        /**
         * 在主线程初始化
         * 不然在子线程调用崩溃
         */
        fun init(){myToast.textView.text=""}

        /**
         * 显示toast
         * @param text
         * @param during
         */
        fun showToasteLong(text: CharSequence, during: Int) {
                showToaste(text, during)
            myToast.textView.postDelayed({ myToast.toast.show() }, 3000L)

        }

        /**
         * 取消toast
         */
        fun cancel() {
            myToast.toast.cancel()
        }
    }
}
