package cn.xj.kokoro.mobile.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected val TAG by lazy {
        if (this::class.java.canonicalName!=null){
            val className: List<String> = this::class.java.canonicalName!!.split(".")
            className[className.size-1]
        }else "TAG"
    }
    private var rootView: View? = null
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        if (arguments != null) onBundle(requireArguments())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return setContentView(inflater, container, getViewId())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view,savedInstanceState)
    }


    /**
     * fragment可见的时候操作，取代onResume，且在可见状态切换到可见的时候调用
     */
    open fun onVisible() {
        observerUI()
    }

    /**
     * fragment不可见的时候操作,onPause的时候,以及不可见的时候调用
     */
    open fun onHidden() {

    }


    override fun onResume() {//和activity的onResume绑定，Fragment初始化的时候必调用，但切换fragment的hide和visible的时候可能不会调用！
        super.onResume()
        if (isAdded && !isHidden) {//用isVisible此时为false，因为mView.getWindowToken为null
            onVisible()
        }
    }

    override fun onPause() {
        if (isVisible)
            onHidden()
        super.onPause()
    }


    //默认fragment创建的时候是可见的，但是不会调用该方法！切换可见状态的时候会调用，但是调用onResume，onPause的时候却不会调用
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
        } else {
            onHidden()
        }
    }


    private fun setContentView(inflater: LayoutInflater, container: ViewGroup?, resId: Int): View? {
        if (rootView == null) {
            rootView = inflater.inflate(resId, container, false)
        } else {
            container?.removeView(rootView)
        }
        return rootView
    }


    abstract fun getViewId(): Int

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(view: View, savedInstanceState: Bundle?)

}