import android.util.TypedValue
import com.kxjsj.doctorassistant.App
import com.kxjsj.doctorassistant.Utils.K2JUtils
import com.kxjsj.doctorassistant.Utils.MyToast


/**
 * ****************************
 * Kotlin 如此简洁强大？
 * ***********    *************
 * 方法扩展类
 * ****************************
 */

/**
 * 空也可toString
 */
fun Any?.mtoString(): String {
    if (this == null)
        return "null"
    else
        return toString()
}

/**
 * toast
 * 到处吐司？
 */
fun Any?.toast(charSequence: CharSequence, int: Int) {
    MyToast.showToaste(charSequence, int)
}

/**
 * toast
 * 重载
 * 到处吐司？
 */
fun Any?.toast() {
    MyToast.showToaste(mtoString(), 1)
}


/**
 * dp2px
 */
fun Any?.dp2px(dp: Float): Int {
    return (0.5f + TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            App.app.resources.displayMetrics)).toInt()
}

/**
 * 打印log
 */
fun Any.log(charSequence: CharSequence){
    K2JUtils.log(javaClass.simpleName,charSequence)
}