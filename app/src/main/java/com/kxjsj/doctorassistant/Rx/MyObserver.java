package com.kxjsj.doctorassistant.Rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class MyObserver<T> implements Observer<T> {
    private static final String TAG = "MyObserver";
    private String tag = null;
    Disposable d;

    protected MyObserver(String tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        RxLifeUtils.getInstance().add(tag, d);
    }


    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed())
            d.isDisposed();
    }

    public void onProgress(long current, long total) {
    }

}

