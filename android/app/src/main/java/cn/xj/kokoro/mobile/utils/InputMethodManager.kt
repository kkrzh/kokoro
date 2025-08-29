package cn.xj.kokoro.mobile.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.IBinder
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


/**
 * Created by xianjie on 2022年11月16日11:11:28
 *
 * Description:
 */
object InputMethodManager {
    /**
     * 隐藏输入法
     */
    fun Context.hideSoftInputFromWindow(windowToken: IBinder):Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
         return imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
    /**
     * 显示输入法
     */
    fun Context.showSoftInputFromWindow(
        editView: View,
        flags: Int = InputMethodManager.SHOW_IMPLICIT
    ) {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editView, flags)
    }

    fun Fragment.setOnGlobalLayoutListener(changeListener:((Boolean)->Unit)){
        requireContext().setOnGlobalLayoutListener(changeListener)
    }
    /**
     * 软键盘展开或收起的监听
     */

    fun Context.setOnGlobalLayoutListener(changeListener:((Boolean)->Unit)){
        if(this is Activity){
            val rootView = window.decorView
            var rootViewVisibleHeight = 0

            rootView.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener { //获取当前根视图在屏幕上显示的大小
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)
                val visibleHeight = r.height()
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    changeListener.invoke(true)
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    changeListener.invoke(false)
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }
            })
        }
    }
}