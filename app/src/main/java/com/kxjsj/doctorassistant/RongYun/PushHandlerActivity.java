package com.kxjsj.doctorassistant.RongYun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;

/**
 * Created by vange on 2017/9/22.
 */

public class PushHandlerActivity extends BaseTitleActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if(intent!=null){
            String targetId = intent.getStringExtra("targetId");
            String pushData = intent.getStringExtra("pushData");
            String pushId = intent.getStringExtra("pushId");
            String extra = intent.getStringExtra("extra");
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG, "initView: "
                +"targetId:"+targetId
                +"pushData:"+pushData
                +"pushId:"+pushId
                +"extra:"+extra);
        }
    }
}
