package com.kxjsj.doctorassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kxjsj.doctorassistant.Appxx.RadioActivity;
import com.kxjsj.doctorassistant.RongYun.ConversationUtils;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ConversationUtils.openChartList(this);
        startActivity(new Intent(this, RadioActivity.class));
        finish();
    }
}
