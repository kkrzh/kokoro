package cn.xj.kokoro.mobile.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.xj.kokoro.mobile.NativeLib
import cn.xj.kokoro.mobile.R
import cn.xj.kokoro.mobile.base.BaseFragment

class Page2Fragment: BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_page2

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.textView).text = NativeLib.helloWorld()
    }
}