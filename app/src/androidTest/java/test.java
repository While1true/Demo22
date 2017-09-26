import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Net.HttpClientUtils;

import static com.kxjsj.doctorassistant.Net.HttpClientUtils.addkeyStore;

/**
 * Created by vange on 2017/9/26.
 */

public class test {
    public static void main(String[] args) {
        addkeyStore(null);
        String url="https://www.baidu.com/baidu?tn=monline_3_dg&ie=utf-8&wd=%E8%8B%B9%E6%9E%9C";
        String s = HttpClientUtils.get(url, null);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG, "main: "+s);
    }
}
