package cn.xj.kokoro.mobile.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.sign

/**
 */
class MagicCurtain : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    private val childViewPager: ViewPager2?
        get() {
            var v: ViewPager2? = null
            children.forEach {
                if (it is ViewPager2){
                    v =  it as? ViewPager2
                }
            }
            return v
        }

    private val parentRootView: View?
        get() {
            var v: View? = parent as? View
            while (v?.parent != null) {
                Log.e("TAG", ": "+v)
                v = v.parent as? View
            }
            return v as? View
        }


    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> childViewPager?.canScrollHorizontally(direction) ?: false
            1 -> childViewPager?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
//        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(event: MotionEvent) {
        parentRootView
        val orientation = childViewPager?.orientation ?: return
        // Early return if child can't scroll in same direction as parent
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentTranslationX = 0F
                changeTranslationX = 0F
                startX = event.rawX
                lastMoveX = event.rawX
                startTime = System.currentTimeMillis()
            }

            MotionEvent.ACTION_MOVE -> {
                // 计算本次移动的增量（当前坐标 - 上次坐标）
//                    val deltaX = event.rawX - lastMoveX
                lastMoveX = event.rawX // 更新上次坐标

                // 计算总滑动距离（当前坐标 - 初始坐标）
                val totalDeltaX = event.rawX - startX

                changeTranslationX = totalDeltaX

                changeDevView()
            }

            MotionEvent.ACTION_UP -> {
                //按下抬起间隔小于300毫秒才算点击
                if (System.currentTimeMillis() - startTime < 300) {
//                    v.performClick()
                }
//                adjustDevView()
            }

            else -> {
//                adjustDevView()
            }
        }


        //不可以向左滑动时
        if (!canChildScroll(orientation, -1f)) {

//            changeDevView()
            Log.e("TAG", "handleInterceptTouchEvent: 此时不可向左滑了" )

        }
        //不可以向右滑动时
        if (!canChildScroll(orientation, 1f)){
            changeDevView()
            Log.e("TAG", "handleInterceptTouchEvent: 此时不可向右滑了" )

        }
//        Log.e("TAG", "handleInterceptTouchEvent: 正常滚动" )

//        if (e.action == MotionEvent.ACTION_DOWN) {
//            initialX = e.x
//            initialY = e.y
//            parent.requestDisallowInterceptTouchEvent(true)
//        } else if (e.action == MotionEvent.ACTION_MOVE) {
//            val dx = e.x - initialX
//            val dy = e.y - initialY
//            val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL
//
//            // assuming ViewPager2 touch-slop is 2x touch-slop of child
//            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
//            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f
//
//            if (scaledDx > touchSlop || scaledDy > touchSlop) {
//                if (isVpHorizontal == (scaledDy > scaledDx)) {
//                    // Gesture is perpendicular, allow all parents to intercept
//                    parent.requestDisallowInterceptTouchEvent(false)
//                } else {
//                    // Gesture is parallel, query child if movement in that direction is possible
//                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
//                        // Child can scroll, disallow all parents to intercept
//                        parent.requestDisallowInterceptTouchEvent(true)
//                    } else {
//                        // Child cannot scroll, allow all parents to intercept
//                        parent.requestDisallowInterceptTouchEvent(false)
//                    }
//                }
//            }
//        }
    }

    // 记录按下时的时间
    private var startTime: Long = 0L

    // 记录按下时的初始X坐标
    private var startX = 0F

    // 记录上一次移动的X坐标
    private var lastMoveX = 0F

    // 滑动时改变的x坐标
    private var changeTranslationX = 0F

    // 点击时的那一刻 当前x坐标
    private var currentTranslationX = 0F

    private fun changeDevView() {
        //从显示状态开始时的滑动处理
        if (currentTranslationX == 0F) {
//            adjustChangedX(minTranslationX)
//
            childViewPager?.translationX =  changeTranslationX
        }
        //从隐藏状态开始时的滑动处理
        if (currentTranslationX == 500F) {
//            adjustChangedX(maxTranslationX)
//            loadLayout.translationX = maxTranslationX + changeTranslationX
            childViewPager?.translationX = 0F + changeTranslationX
        }
    }

}