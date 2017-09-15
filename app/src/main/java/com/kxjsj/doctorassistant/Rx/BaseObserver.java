package com.kxjsj.doctorassistant.Rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private static final String TAG = "BaseObserver";
    private String tag = null;
    Disposable d;

    protected BaseObserver(String tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        RxLifeUtils.getInstance().add(tag, d);
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
        if (d != null && !d.isDisposed())
            d.isDisposed();
    }

    protected abstract void onHandleSuccess(T t);

    public void onProgress(String tag, long current, long total) {
    }

    protected void onHandleError(String msg) {
    }
}

