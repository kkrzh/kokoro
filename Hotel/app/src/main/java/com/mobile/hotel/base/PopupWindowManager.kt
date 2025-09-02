package com.mobile.hotel.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.PopupWindow
import com.mobile.hotel.base.InputMethodManager.hideSoftInputFromWindow
import java.lang.ref.WeakReference

/**
 *Created by Zebra-RD张先杰 on 2022年3月11日17:17:38
 *Description:PopupWindow管理/工具类
 * @param context
 * @param xmlId 布局id
 * @param xDimenId 宽
 * @param yDimenId 高
 * @param outsideTouchable 点击外部消失
 * @param focusable 可以点击
 * @param background 窗口后背景
 * @param animal
 * @param finishListener
 * @param clickListener
 */
class PopupWindowManager(
    context: Activity,
    xmlId: Int,
    xDimenId: Int,
    yDimenId: Int,
    outsideTouchable: Boolean = true,
    focusable: Boolean = true,
    background: Boolean = true,
    animal: Int = 0,
    finishListener: (View.() -> Unit)? = null,
    clickListener: (View.(PopupWindow, PopupWindowManager) -> Unit),
) {
   companion object{
       @JvmField
       val MATCH_PARENT = -1
       @JvmField
       val WRAP_CONTENT = -2
   }
    private var mBackground = false
    private var mContextWeakRef: WeakReference<Activity>? = null

    // 使用时安全获取
    private fun getContext(): Activity? {
        return mContextWeakRef?.get()?.takeIf { context ->
            !context.isDestroyed && !context.isFinishing
        }
    }

     var selectNumSpecPop: PopupWindow? = null

    var mPopUpView:View? = null

    init {
        mContextWeakRef = WeakReference(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView = inflater.inflate(xmlId, null, false)
        selectNumSpecPop = PopupWindow(
            popUpView,
           if(xDimenId>0) context.resources.getDimension(xDimenId).toInt() else xDimenId,
            if(yDimenId>0) context.resources.getDimension(yDimenId).toInt() else yDimenId
        ).apply {
            if (animal != 0)
                animationStyle = animal
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //点击外部小时
            isOutsideTouchable = outsideTouchable
            //设置可以点击
            isFocusable = focusable
            //背景色
            mBackground = background
            setOnDismissListener {
                    darkenBackground(1F)
                    finishListener?.invoke(popUpView)
            }
        }

        selectNumSpecPop?.enterTransition =TransitionSet()
        mPopUpView = popUpView
        if (popUpView != null && selectNumSpecPop != null)
            clickListener.invoke(popUpView, selectNumSpecPop!!,this)
    }


    //判断Window状态，如果弹出就收回
    private fun windowIsShow(show: () -> Unit) {
        if (selectNumSpecPop != null && selectNumSpecPop!!.isShowing)
            selectNumSpecPop!!.dismiss()
        else show.invoke()
    }

    /**
     * 改变背景颜色
     */
    fun darkenBackground(bgcolor: Float) {
        val lp: WindowManager.LayoutParams =
            getContext()!!.window.attributes
        lp.alpha = bgcolor
        if (bgcolor == 1F){
            getContext()!!.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }else{
            getContext()!!.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        getContext()!!.window.attributes = lp
    }

    fun show(x: Int, y: Int, gravity: Int,hindView: EditText? = null) {
        if (hindView!=null) {
            getContext()!!.hideSoftInputFromWindow( hindView.windowToken)
        }

        windowIsShow {
            selectNumSpecPop?.let {it->
                getContext()?.let {ac->
                    if ( ac.window.decorView.windowToken!=null && !ac.isDestroyed && !ac.isFinishing){
                        it.showAtLocation(getContext()!!.window.decorView, gravity, x, y)
                        if (mBackground) darkenBackground(0.5F)
                        else darkenBackground(1F)
                   }
                }
            }
        }
    }
    fun show(toolbarBack: View, x: Int = 0, y: Int, gravity: Int,hindView: EditText? = null) {
        if (hindView!=null) {
            getContext()!!.hideSoftInputFromWindow(hindView.windowToken)
        }

        windowIsShow {
            selectNumSpecPop?.let {
                it.showAsDropDown(toolbarBack, x, y, gravity)
                if (mBackground) darkenBackground(0.5F)
                else darkenBackground(1F)
            }
        }
    }

    fun show(view: View,hindView: EditText? = null){
        if (hindView!=null) {
            getContext()!!.hideSoftInputFromWindow(hindView.windowToken)
        }

        windowIsShow {
            selectNumSpecPop?.let {
                it.showAsDropDown(view,Gravity.BOTTOM,0,0)
                if (mBackground) darkenBackground(0.5F)
                else darkenBackground(1F)
            }
        }
    }
    fun dismiss() {
        selectNumSpecPop?.let { popup ->
            popup.dismiss()
        }
    }

    fun destroy(){
        selectNumSpecPop?.let { popup ->
            popup.dismiss()
            // 清除所有回调（防止内存泄漏）
            popup.setOnDismissListener(null)
            popup.contentView?.viewTreeObserver?.removeOnGlobalLayoutListener(null)
        }
        selectNumSpecPop = null
        mPopUpView = null
        mContextWeakRef?.clear()
        mContextWeakRef = null
    }
}