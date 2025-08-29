package cn.xj.kokoro.mobile

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.view.allViews
import kotlin.reflect.KClass

class EventFrameLayout: FrameLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    // 需要排除拦截的子View类型列表
    private val excludedViewTypes = mutableSetOf<KClass<out View>>()

    // 需要排除拦截的特定子View列表
    private val excludedSpecificViews = mutableSetOf<View>()

    // 添加需要排除拦截的View类型
    fun addExcludedViewType(viewClass: KClass<out View>) {
        excludedViewTypes.add(viewClass)
    }

    // 添加需要排除拦截的特定View
    fun addExcludedView(view: View) {
        excludedSpecificViews.add(view)
    }

    // 移除排除拦截的View
    fun removeExcludedView(view: View) {
        excludedSpecificViews.remove(view)
        excludedViewTypes.remove(view::class)
    }

    fun removeAllExcludeViews() {
        excludedSpecificViews.clear()
        excludedViewTypes.clear()
    }
    var interceptEnabled = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!interceptEnabled) {
            return super.onInterceptTouchEvent(ev)
        }

        ev ?: return super.onInterceptTouchEvent(ev)

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 在DOWN事件时判断是否需要拦截
                Log.e("TAG", "onInterceptTouchEvent: "+shouldInterceptEvent(ev) )
                return shouldInterceptEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                // 可以根据需要处理MOVE事件
                return isIntercepting
            }
            else -> return super.onInterceptTouchEvent(ev)
        }
    }

    // 判断是否需要拦截事件
    private fun shouldInterceptEvent(event: MotionEvent): Boolean {
        // 查找触摸位置下的子View
        val targetView = findViewAtPosition(event.rawX, event.rawY)

        // 如果没有找到目标View，默认拦截
        if (targetView == null) {
            return true
        }
        Log.e("TAG", "onInterceptTouchEvent: "+targetView )

        Log.e("TAG", "onInterceptTouchEvent: "+excludedSpecificViews )
        Log.e("TAG", "onInterceptTouchEvent: "+excludedViewTypes )

        // 检查是否在排除的特定View列表中
        if (excludedSpecificViews.contains(targetView)) {
            return false // 不拦截
        }

        // 检查是否在排除的View类型列表中
        for (excludedType in excludedViewTypes) {
            if (excludedType.isInstance(targetView)) {
                return false // 不拦截
            }
        }

        // 默认拦截
        return true
    }

    // 查找指定位置下的子View
    private fun findViewAtPosition(x: Float, y: Float): View? {
        // 逆序遍历子View（从最上层开始）
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            if (isPointInView(child, x, y) && child.visibility == VISIBLE) {
                return child
            }
        }
        return null
    }

    // 判断点是否在View内
    private fun isPointInView(view: View, x: Float, y: Float): Boolean {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val viewX = location[0]
        val viewY = location[1]

        view.getLocationOnScreen(location)
        val viewX_ = location[0]
        val viewY_ = location[1]


        Log.e("TAG", "isPointInView: "+view )
        Log.e("TAG", "isPointInView: VIEW_WINDOW-  "+viewX )
        Log.e("TAG", "isPointInView: VIEW_WINDOW-  "+viewY )
        Log.e("TAG", "isPointInView: VIEW_SCREEN-  "+viewX_ )
        Log.e("TAG", "isPointInView: VIEW_SCREEN-  "+viewY_ )
        Log.e("TAG", "isPointInView: TOUCH-  "+x )
        Log.e("TAG", "isPointInView: TOUCH-  "+y )

        return (x >= viewX && x < viewX + view.width &&
                y >= viewY && y < viewY + view.height)
    }

    // 保存拦截状态
    private var isIntercepting = false

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        if (!interceptEnabled) {
//            return super.onTouchEvent(event)
//        }
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                isIntercepting = shouldInterceptEvent(event)
//                return isIntercepting
//            }
//
//            MotionEvent.ACTION_UP -> {
//                if (isIntercepting) {
//                    performClick()
//                }
//                isIntercepting = false
//                return true
//            }
//
//            else -> return isIntercepting
//        }
//    }

    override fun performClick(): Boolean {
        super.performClick()
        // 处理点击事件
        return true
    }
}