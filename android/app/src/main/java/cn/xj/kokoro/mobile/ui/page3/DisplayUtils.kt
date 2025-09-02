package cn.xj.kokoro.mobile.ui.page3

import android.content.Context
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.view.Display
import cn.xj.kokoro.mobile.App

object  DisplayUtils {

    /**
     * @param info 屏幕宽度 屏幕高度 dpi
     */
    fun getDisplayMetricsInfo(info:((screenWidth:Int,screenHeight:Int,densityDpi:Int)->Unit)? = null): ArrayList<Int> {
        App.application.apply {
            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = displayManager.getDisplay(Display.DEFAULT_DISPLAY)  // 获取默认显示器
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels
            val densityDpi = displayMetrics.densityDpi
            info?.invoke(screenWidth, screenHeight, densityDpi)
            return arrayListOf(screenWidth, screenHeight, densityDpi)
        }
    }
}