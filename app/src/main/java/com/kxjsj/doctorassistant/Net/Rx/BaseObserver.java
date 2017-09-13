package com.kxjsj.doctorassistant.Net.Rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private static final String TAG = "BaseObserver";

    protected BaseObserver(String tag) {
    }

    @Override
    public void onSubscribe(Disposable d) {
    }


    @Override
    public void onNext(BaseBean<T> value) {
        if (value.isSuccess()) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }

    protected abstract void onHandleSuccess(T t);

    public void onProgress(String tag,long current,long total){}

    protected void onHandleError(String msg) {
    }
}

