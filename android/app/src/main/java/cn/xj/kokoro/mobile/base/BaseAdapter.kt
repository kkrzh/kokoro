package cn.xj.kokoro.mobile.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter(
    private val context_: Context? = null
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {
    lateinit var context: Context
    init {
        if (context_!=null) context= context_
    }
    class BaseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val viewList = HashMap<Int, View>()
        fun <T : View> getViewById(id: Int): T {
            return if (viewList[id] == null) {
                viewList[id] = view.findViewById<View>(id)
                view.findViewById<T>(id)
            } else viewList[id] as T
        }
    }

    abstract fun setItemLayout(viewType: Int): Int
    abstract fun setItemCount(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        //得到LayoutInflater用于把XML初始化为View
        val inflater =
            LayoutInflater.from(parent.context).inflate(setItemLayout(viewType), parent, false)
        //通过子类必须实现的方法，得到一个ViewHolder
        return BaseViewHolder(inflater)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        isAnimating = {
            recyclerView.isAnimating
        }
    }

    override fun getItemCount(): Int = setItemCount()
    protected var isAnimating: (() -> Boolean)? = null

    /**
     * 一个防止改变时点击的事件 ，必须实现isAnimating 默认已提供实现
     * @param interval 不为空时不能连续点击
     * @param toastToast 不为空时连续点击将会提示
     * @param isBusy 两虚点击时的回调
     * @param click 正常点击的回调
     */
    protected fun View.setOnClickForAdapterListener(
        interval: Int? = null,
        toastToast: String = "",
        isBusy: (() -> Unit)? = null,
        click: (View) -> Unit
    ) {
        if (isAnimating!=null){
                var lastClickTime: Long = 0
                if (interval != null) {
                        setOnClickListener {
                            if ((System.currentTimeMillis() - lastClickTime) >= interval && !isAnimating!!.invoke()) {
                                click.invoke(it)
                                lastClickTime = System.currentTimeMillis()

                            } else {
                                if (toastToast.isNotBlank()) Toast.makeText(context, toastToast, Toast.LENGTH_SHORT).show()
                                isBusy?.invoke()
                            }
                        }
                } else {
                    setOnClickListener {
                        if (!isAnimating!!.invoke())click.invoke(it)
                    }
                }
        }else throw RuntimeException("要使用setOnClickForAdapterListener必须实现isAnimating")
    }
    /**
     * 一个防止改变时点击的事件 ，必须实现isAnimating 默认已提供实现
     * @param interval 不为空时不能连续点击
     * @param toastToast 不为空时连续点击将会提示
     * @param isBusy 两虚点击时的回调
     * @param click 正常点击的回调
     */
    protected fun View.setOnInterceptClickForAdapterListener(
        interval: Int? = null,
        toastToast: String = "",
        isBusy: (() -> Unit)? = null,
        click: (View) -> Unit
    ) {
        if (isSuccess!=null){
            var lastClickTime: Long = 0
            if (interval != null) {
                setOnClickListener {
                    if ((System.currentTimeMillis() - lastClickTime) >= interval && isSuccess!!.invoke()) {
                        click.invoke(it)
                        lastClickTime = System.currentTimeMillis()

                    } else {
                        if (toastToast.isNotBlank())  Toast.makeText(context, toastToast, Toast.LENGTH_SHORT).show()
                        isBusy?.invoke()
                    }
                }
            } else {
                setOnClickListener {
                    if (isSuccess!!.invoke())click.invoke(it)
                }
            }
        }else throw RuntimeException("要使用setOnClickForAdapterListener必须实现isSuccess")
    }

    var isSuccess:(()->Boolean)? = {
        true
    }
}
