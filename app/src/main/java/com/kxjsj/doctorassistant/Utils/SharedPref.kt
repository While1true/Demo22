package com.kxjsj.doctorassistant.Utils


import com.kxjsj.doctorassistant.App


/**
 *  Created by 不听话的好孩子 on 2017/9/27.
 * SharedPref 存储一些数据
 */
object SharedPref {
    /**
     * 表名
     */
    private val NAME = "sql"

    /**
     * sharedPreferences 懒加载
     */
    private val sharedPreferences by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { App.app.getSharedPreferences(NAME, App.MODE_PRIVATE) }

    /**
     * EDITOR 懒加载
     */
    private val EDITOR by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { App.app.getSharedPreferences(NAME, App.MODE_PRIVATE).edit() }

    /**
     * 存放数据
     */
    fun put(key: String, value: Any) {
        when (value) {
            is Boolean -> EDITOR.putBoolean(key, value).apply()
            is Int -> EDITOR.putInt(key, value).apply()
            is String -> EDITOR.putString(key, value).apply()
            is Float -> EDITOR.putFloat(key, value).apply()
            is Long -> EDITOR.putLong(key, value).apply()
            else -> throw UnknownError("unknow value!")
        }


    }

    /**
     * 获取数据
     */
    fun get(key: String, defaultValue: Any):Any {
        when (defaultValue) {
            is Boolean -> return sharedPreferences.getBoolean(key, defaultValue)
            is Int -> return sharedPreferences.getInt(key, defaultValue)
            is String ->return sharedPreferences.getString(key, defaultValue)
            is Float -> return sharedPreferences.getFloat(key, defaultValue)
            is Long -> return sharedPreferences.getLong(key, defaultValue)
            else -> throw UnknownError("unknow value!")
        }
    }


}

