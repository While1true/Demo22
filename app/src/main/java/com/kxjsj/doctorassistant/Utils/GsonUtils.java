package com.kxjsj.doctorassistant.Utils;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vange on 2017/9/28.
 */

/**
 * 还未测试谨慎使用
 */
public class GsonUtils {


    private static class GsonHolder {
        private static Gson gson = new Gson();
    }

    private GsonUtils() {
    }

    /**
     * Json转化为list
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parse2List(String json, Class<T> clazz) {
        Type type = new MyListParameterizedType(clazz);
        return GsonHolder.gson.fromJson(json, type);
    }

    /**
     * Json转化为map
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> parse2Map(String json, Class<T> clazz) {
        Type type = new MyMapParameterizedType(clazz);
        return GsonHolder.gson.fromJson(json, type);
    }

    /**
     * String 2 Bean
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse2Bean(String json, Class<T> clazz) {
        return GsonHolder.gson.fromJson(json, clazz);
    }

    /**
     * class 2 String
     *
     * @param clazz
     * @return
     */
    public static String parse2String(Class clazz) {
        return GsonHolder.gson.toJson(clazz);
    }

    /**
     * map 2 String
     *
     * @param map
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> String map2String(Map<String, T> map, Class<T> tClass) {
        Type type = new MyMapParameterizedType(tClass);
        return GsonHolder.gson.toJson(map, type);
    }

    /**
     * list 2 String
     *
     * @param list
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> String list2String(List<T> list, Class<T> tClass) {
        Type type = new MyListParameterizedType(tClass);
        return GsonHolder.gson.toJson(list, type);
    }


    /**
     * 解析List
     */
    private static class MyListParameterizedType implements ParameterizedType {
        Class aClass;

        public MyListParameterizedType(Class clazz) {
            aClass = clazz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{aClass};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 解析Map
     */
    private static class MyMapParameterizedType implements ParameterizedType {
        Class aClass;

        public MyMapParameterizedType(Class clazz) {
            aClass = clazz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class, aClass};
        }

        @Override
        public Type getRawType() {
            return HashMap.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
