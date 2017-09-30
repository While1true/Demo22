package com.kxjsj.doctorassistant.Utils;


import com.kxjsj.doctorassistant.App;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by vange on 2017/9/30.
 * Mob短信验证服务
 * 先在App中初始化
 * 在验证界面验证
 *
 */

public class MessageUtils {
    private static long last=0;
    private static final String APP_KEY = "2160ceda81af1", APP_SERECT = "8d659f28ea238bf011a7a49fdc5c578c";

    public static void init(App app) {
        MobSDK.init(app, APP_KEY, APP_SERECT);
    }

    /**
     * 注册监听
     * @param handler
     */
    public static void registListener(MyMssHandler handler) {
        SMSSDK.registerEventHandler(handler);
    }

    /**
     * 注销监听
     * @param handler
     */
    public static void unregistListener(MyMssHandler handler) {
        SMSSDK.unregisterEventHandler(handler);
    }

    /**
     * 请求getVerificationCode的时间间隔不应该小于60秒
     * 发送验证码
     * @param phoneNumber
     */
    public static void sendMessage(String phoneNumber) {
        if(System.currentTimeMillis()-last>60*1000) {
            cn.smssdk.SMSSDK.getVerificationCode("86", phoneNumber);
            last = System.currentTimeMillis();
        }else{
            K2JUtils.toast("60s后才能再次发送验证码!",1);
        }
    }

    /**
     * 认证验证码
     * @param
     * @param phone
     * @param code
     */
    public static void authCode(String phone, String code) {
        cn.smssdk.SMSSDK.submitVerificationCode("86", phone, code);
    }

    public static void getall(){
        cn.smssdk.SMSSDK.getSupportedCountries();
    }

    public interface MssListener {
        void onSendSmsSuccess();

        /**
         * String phone country
         * Object 电话号码 国家编码
         * @param datas
         */
        void onAuthSuccess(HashMap<String, Object> datas);

        /**
         * String 国家code
         * Object 正则表达式
         * @param countries
         */
        void onReceiverSupportCountries(ArrayList<HashMap<String, Object>> countries);
    }

    public static class MyMssHandler extends EventHandler {
        MssListener listener;

        public MyMssHandler(MssListener listener) {
            this.listener = listener;
        }

        @Override
        public void afterEvent(int event, int result, Object data) {
            if (data instanceof Throwable) {
                Throwable throwable = (Throwable) data;
                String msg = throwable.getMessage();
                K2JUtils.toast(msg, 1);
            } else {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    listener.onAuthSuccess((HashMap<String, Object>) data);
                    //提交验证码成功
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    listener.onSendSmsSuccess();
                    //获取验证码成功
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    listener.onReceiverSupportCountries((ArrayList<HashMap<String, Object>>) data);
                }
            }
        }
    }
}
