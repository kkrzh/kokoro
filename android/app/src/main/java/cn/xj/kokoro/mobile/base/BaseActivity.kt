package cn.xj.kokoro.mobile.base

import android.os.Bundle


abstract class BaseActivity(contentLayoutId:Int) : BaseOriginActivity(false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    override var setContentView: () -> Unit = {
        if (contentLayoutId!=0) setContentView(contentLayoutId)
    }

    abstract fun init(savedInstanceState: Bundle?)
}