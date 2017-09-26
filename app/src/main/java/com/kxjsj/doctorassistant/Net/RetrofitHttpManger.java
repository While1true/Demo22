package com.kxjsj.doctorassistant.Net;


import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ck on 2017/7/31.
 */

public class RetrofitHttpManger {
    private static final int DEFAULT_CONNECT_TIMEOUT = 5;
    private static final int DEFAULT_READ_TIMEOUT = 10;
    private static final String BASEURL = "";
    private Retrofit mRetrofit;


    private RetrofitHttpManger() {
        OkHttpClient httpclient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor(message -> {
                    if (Constance.DEBUGTAG)
                        Log.i(Constance.DEBUG, "log: " + message);
                }).setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
        // 添加公共参数拦截器
        // 添加通用的Header
//         .addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request.Builder builder = chain.request().newBuilder();
//                builder.addHeader("token", "123");
//                return chain.proceed(builder.build());
//            }
//        })

        mRetrofit = new Retrofit.Builder()
                .client(httpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();

    }

    private static class SingleHolder {
        private static RetrofitHttpManger manger = new RetrofitHttpManger();
    }

    public static <T> T create(Class<T> service) {
        return SingleHolder.manger.mRetrofit.create(service);
    }
}

