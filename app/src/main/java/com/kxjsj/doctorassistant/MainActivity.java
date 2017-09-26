package com.kxjsj.doctorassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kxjsj.doctorassistant.Appxx.RadioActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Net.HttpClientUtils;
import com.kxjsj.doctorassistant.Rx.MyObserver;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ConversationUtils.openChartList(this);
        Observable.just(1)
              .observeOn(Schedulers.io())
              .map(integer -> {
                    String url="https://www.baidu.com/baidu?tn=monline_3_dg&ie=utf-8&wd=%E8%8B%B9%E6%9E%9C";
                    String s = HttpClientUtils.get(url, null);
                    if (Constance.DEBUGTAG)
                        Log.i(Constance.DEBUG, "mainzzzzz: "+s);
                    return integer.toString();
              })
              .subscribe(new MyObserver<String>("aa") {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e("mainzzzzz: ", "onError: ",e );
            }
        });

        startActivity(new Intent(this, RadioActivity.class));
        finish();
    }
}
