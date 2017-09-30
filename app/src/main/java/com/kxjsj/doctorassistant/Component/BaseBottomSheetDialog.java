package com.kxjsj.doctorassistant.Component;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kxjsj.doctorassistant.R;


/**
 * Created by vange on 2017/9/30.
 */

public abstract class BaseBottomSheetDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view,savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    protected  abstract int getLayoutId();

    protected abstract void initView(View view,Bundle savedInstanceState);

    /**
     * show
     * @param manager
     */
    public BaseBottomSheetDialog show(FragmentManager manager){
        show(manager,getClass().getSimpleName());
        return this;
    }

}
