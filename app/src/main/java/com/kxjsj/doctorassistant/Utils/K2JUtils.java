package com.kxjsj.doctorassistant.Utils;

/**
 * Created by vange on 2017/9/28.
 */

/**
 * kotlin项目使用，小范围使用学习
 */
public class K2JUtils {

    /**
     * 关联kotlin的sharepref
     * @param key
     * @param value
     */
    public static void put(String key,Object value){
        SharedPref.INSTANCE.put(key,value);
    }
    /**
     * 关联kotlin的sharepref
     * @param key
     * @param defaultvalue
     */
    public static <T>T get(String key,Object defaultvalue){
       return (T)SharedPref.INSTANCE.get(key,defaultvalue);
    }

    /**
     * 关联kotlin吐司
     * @param charSequence
     * @param during
     */
    public static void toast(CharSequence charSequence,int during){
        MyToast.Companion.showToaste(charSequence,during);
    }
}
