package com.kxjsj.doctorassistant.Rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vange on 2017/9/13.
 */

public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> compose() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
            //TODO
        }).observeOn(AndroidSchedulers.mainThread());
    }
}
