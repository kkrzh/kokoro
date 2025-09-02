package com.mobile.hotel

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.mobile.hotel.base.dpToInt
import com.mobile.hotel.base.getSafeCompatColor

/**
 * Description: type
 */
class StarProgressView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        if (attributes != null) {
            context.withStyledAttributes(attributes, R.styleable.StarProgressView) {
                currentCount = getInteger(R.styleable.StarProgressView_count, 0)
            }
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var maxCount = 5

    private var currentCount = 0

    private val starVector by lazy {
        VectorDrawableCompat.create(
            resources,
            R.drawable.ic_star,
            context.theme
        )
    }

    private val starVectorVar by lazy {
        VectorDrawableCompat.create(
            resources,
            R.drawable.ic_star,
            context.theme
        ).apply {
            this?.tintVectorDrawable(R.color.red)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //离散模式 有几个星测量几个星 连续模式 固定测量最大数
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> 16f.dpToInt
            else -> MeasureSpec.getSize(heightMeasureSpec)
        }

        val desiredWidth = height * maxCount
        val finalWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val finalHeight = resolveSize(height, heightMeasureSpec)

        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val starSize = height.toFloat()

        repeat(maxCount) { index ->
            val starSizeScaled = starSize * 0.8f
            val offset = (starSize - starSizeScaled) / 2f

            val left = index * starSize + offset
            val right = left + starSizeScaled

            if (index + 1 <= currentCount) {
                starVectorVar?.setBounds(
                    left.toInt(),
                    offset.toInt(),
                    right.toInt(),
                    (offset + starSizeScaled).toInt()
                )
                starVectorVar?.draw(canvas)
            } else {
                starVector?.setBounds(
                    left.toInt(),
                    offset.toInt(),
                    right.toInt(),
                    (offset + starSizeScaled).toInt()
                )
                starVector?.draw(canvas)
            }
        }

    }


    fun setStarCount(count: Int) {
        if (count > maxCount) {
            throw IllegalArgumentException("Star count ($count) exceeds maximum allowed ($maxCount),\n Please adjust value within range [0, $maxCount].")
        }
        currentCount = count
        requestLayout()
        invalidate()
    }

    fun getStarCount(): Int = currentCount

    //设置颜色
    private fun Drawable.tintVectorDrawable(@ColorRes colorRes: Int): Drawable {
        val color = getSafeCompatColor(colorRes)
        return mutate().apply {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    setTint(color)
                }

                else -> {
                    colorFilter = PorterDuffColorFilter(
                        color,
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
    }
}