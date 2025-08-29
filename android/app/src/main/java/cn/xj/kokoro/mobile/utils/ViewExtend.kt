package cn.xj.kokoro.mobile.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import cn.xj.kokoro.mobile.App


/**
Created by Zebra-RD张先杰 on 2022年7月6日14:04:42

Description:该类存放了View相关的扩展函数
 */

/**
 * @param interval 至少要隔interval毫秒才可以再次点击
 */
fun View.setOnIntervalClickListener(
    interval: Long = 1000,
    toast: String = "",
    isBusy: (() -> Unit)? = null,
    click: (View) -> Unit
) {
    var lastClickTime: Long = 0
    if ((System.currentTimeMillis() - lastClickTime) >= interval) setOnClickListener {
        if ((System.currentTimeMillis() - lastClickTime) >= interval) {
            lastClickTime = System.currentTimeMillis()
            click.invoke(it)
        } else {
            if (toast.isNotBlank()) Toast.makeText(App.Companion.application, toast, Toast.LENGTH_SHORT).show()
            isBusy?.invoke()
        }
    }
}


fun Context.getSafeCompatColor(@ColorRes res: Int, defaultColor: Int = Color.TRANSPARENT): Int {
    return ResourcesCompat.getColor(resources, res, null) ?: defaultColor
}

fun Context.getSafeCompatDrawable(@DrawableRes res: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, res, null)
}

fun View.getSafeCompatDrawable(@DrawableRes res: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, res, null)
}

/**
 * 当使用apply初始化对象时方便使用
 * 安全获取View的颜色资源，如果View未attached则返回默认值
 * 警惕空指针异常(虽然进行了处理，但这使它出错时让你更难排查)，你应该确保View被添加到window时才使用它，优先使用[Context.getCompatColor]]
 * @param res 颜色资源ID
 * @param defaultColor 当resources不可用时返回的默认颜色
 */
@Throws(NullPointerException::class, Resources.NotFoundException::class)
fun View.getSafeCompatColor(@ColorRes res: Int, defaultColor: Int = Color.TRANSPARENT): Int {
    return context?.let {
        runCatching {
            ResourcesCompat.getColor(it.resources, res, null)
        }.getOrDefault(defaultColor)
    } ?: defaultColor
}

/**
 * TextView最小化创建
 * @param root 父布局
 * @param contentText 内容文字
 * @param textColorRes 文字颜色
 * @param textSize 文字大小
 * @param maxLines 最大行数（默认不限制）
 * @param attachToRoot 是否添加到父布局中
 * @param layoutParams 布局参数（默认 WRAP_CONTENT）
 * @param config  当方法字段不满足时实现
 * @return 创建的TextView
 */
fun createSimpleTextView(
    root: ViewGroup,
    contentText: String,
    @ColorRes textColorRes: Int,
    textSize: Float,
    maxLines: Int = Int.MAX_VALUE,
    attachToRoot: Boolean = false,
    layoutParams: LayoutParams? = null,
    config: ((TextView) -> Unit)? = null
):TextView {
    return createSimpleTextView(root.context,contentText,textColorRes, textSize, maxLines, layoutParams){
        config?.invoke(it)
        if (attachToRoot) {
            root.addView(it)
        }
    }
}

/**
 * TextView最小化创建(重载方法)
 * 配置同上，区别在于不会强关联父布局
 */
fun createSimpleTextView(
    context: Context,
    contentText: String,
    @ColorRes textColorRes: Int,
    textSize: Float,
    maxLines: Int = Int.MAX_VALUE,
    layoutParams: LayoutParams? = null,
    config: ((TextView) -> Unit)? = null
):TextView{
    return TextView(context).apply {
        text = contentText
        this.textSize = textSize

        this.maxLines = maxLines
        ellipsize = if (maxLines == 1) TextUtils.TruncateAt.END else null

        this.layoutParams = layoutParams ?: LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        setTextColor(getSafeCompatColor(textColorRes))
        config?.invoke(this)
    }
}

val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Int.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

val Float.dpToInt
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()

val Int.dpToInt
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()