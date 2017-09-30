package com.kxjsj.doctorassistant.Appxx

import android.os.Bundle

import com.kxjsj.doctorassistant.Component.BaseFragment
import com.kxjsj.doctorassistant.R

/**
 * Created by vange on 2017/9/19.
 */

class MineF : BaseFragment() {
    override fun initView(savedInstanceState: Bundle?) {
        retainInstance = true
    }

    override fun getLayoutId(): Int {
        return R.layout.mine_layout
    }

    override fun loadLazy() {

    }
}
