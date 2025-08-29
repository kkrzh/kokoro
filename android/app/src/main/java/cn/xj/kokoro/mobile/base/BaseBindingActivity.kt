package cn.xj.kokoro.mobile.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

/**
 * Created by xianjie on 2023年1月6日16:00:08
 *
 * Description:
 */
abstract class BaseBindingActivity<V : ViewBinding>: BaseOriginActivity(true) {
    lateinit var binding: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observerUI()
        init(savedInstanceState)
    }
    fun binding(block: V.() -> Unit) {
        block.invoke(binding)
    }
    override var setContentView: () -> Unit = {
        binding = DataBindingUtil.setContentView(this, this.getViewId())
    }
    abstract fun getViewId(): Int

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)
}