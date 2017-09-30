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
import com.kxjsj.doctorassistant.Rx.RxLifeUtils;
import com.kxjsj.doctorassistant.Utils.MyToast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ConversationUtils.openChartList(this);
        Observable.just(1)
              .observeOn(Schedulers.io())
              .map(integer -> {
                    String url="https://github.com/";
                    String s = HttpClientUtils.get(url, null);
                    if (Constance.DEBUGTAG)
                        Log.i(Constance.DEBUG, "mainzzzzz: "+s);
                    return s;
              })
                .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new MyObserver<String>(this) {
                  @Override
                  public void onNext(String s) {
                      super.onNext(s);
                      MyToast.Companion.showToasteLong(s,1);
                  }

                  @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e("mainzzzzz: ", "onError: ",e );
            }
        });

        startActivity(new Intent(this, RadioActivity.class));
        overridePendingTransition(0,0);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxLifeUtils.getInstance().remove(this);
    }
}
