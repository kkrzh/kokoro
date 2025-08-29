package cn.xj.kokoro.mobile.view

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import cn.xj.kokoro.mobile.R
import kotlin.concurrent.thread

/**
 * Created by xianjie on 2022年12月29日16:06:00
 *
 * Description:优化了之前的设计
 */
class LoadingWindow(
    activity: Activity,
) : PopupWindow(activity) {
    private var minShowTime = 500L
    private var showTime = 0L
    private var context: Activity? = activity
    init {
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height =  ViewGroup.LayoutParams.MATCH_PARENT
    }

    fun changeText(string:String){
        try {
            val textView =  contentView.findViewById<TextView>(R.id.textView)
            if (string.isNotBlank()){
                textView.visibility = View.VISIBLE
                textView.text = string
            }
        }catch (e:NullPointerException){
            throw NullPointerException("你必须在窗口显示后才可以更改文本！")
        }
    }

    fun showWindow(string:String? = null){
        contentView =  LayoutInflater.from(context).inflate(R.layout.popup_loading, null, false)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val textView =  contentView.findViewById<TextView>(R.id.textView)
        if (string!=null && string.isNotBlank()){
            textView.visibility = View.VISIBLE
            textView.text = string
        }else{
            textView.text = ""
            textView.visibility = View.GONE
        }

        showTime = System.currentTimeMillis()
        if (context!= null && context!!.window.decorView.windowToken !=null)showAtLocation(context!!.window.decorView, Gravity.CENTER, 0, 0)
    }


    fun hindWindow(finish:(()->Unit)){
        val nowTime = System.currentTimeMillis()
        if  (nowTime - showTime >= minShowTime) {
                finish.invoke()
                dismiss()
            } else {
            thread {
                Thread.sleep(minShowTime - (nowTime - showTime))
                context!!.runOnUiThread {
                    hindWindow(finish)
                }
            }
            }
    }

    fun clearWindow(){
        context = null
    }
}