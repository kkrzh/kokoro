package cn.xj.kokoro.mobile.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

import android.animation.ValueAnimator
import android.graphics.*
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import cn.xj.kokoro.mobile.utils.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class DashboardView : View {

    // 自定义属性
    private var minValue = 0f
    private var maxValue = 100f
    private var currentValue = 0f
        set(value) {
            if (value.toInt() > 55){
                unit = "贪婪"
            }else{
                unit = "中性"
            }
            valueChangeCallback?.invoke(value)
            field = value
        }
    internal var valueChangeCallback:((Float)-> Unit)? = null
    private var scaleColor = Color.parseColor("#666666")
    private var pointerColor = Color.parseColor("#FF4081")
    private var textColor = Color.parseColor("#333333")
    private var processTextColor = Color.parseColor("#CCAB45")
    private var scaleCount = 5
    private var startAngle = -210F
    private var sweepAngle = 240f
    private var unit = ""

    // 绘图工具
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val coloursPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {

        }
    }

    private val path = Path()
    private val rect = RectF()
    private val textBounds = Rect()


    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }
    private var currentWidth = 0F
    private var currentHeight = 0F
    private var shader: Shader? = null
    private var alphaShader: Shader? = null
    private var alphaHeight = 30F.dp


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val size = when {
            widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY ->
                min(widthSize, heightSize)
            widthMode == MeasureSpec.EXACTLY -> widthSize
            heightMode == MeasureSpec.EXACTLY -> heightSize
            else -> min(widthSize, heightSize)
        }

        setMeasuredDimension(size, size)
    }
    // 间距
    val distance = 20F.dp
    //主Arc
    var rectFArc: RectF? = null
    //进度Arc
    var rectFProcessArc: RectF? = null

    // 动画相关
    private var animator : ValueAnimator? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.removeAllUpdateListeners()
        animator = null
    }

    fun startAnimation(process: Float) {
        if (animator != null){
            animator = null
        }
            animator = ValueAnimator.ofFloat(currentValue, process).apply {
                duration = 600
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    currentValue = (it.animatedValue as Float)
                    invalidate()
                }
            }
        animator?.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
            rectFArc = RectF(distance,distance, width-distance,height-distance)
            rectFProcessArc = RectF(distance*1.2f,distance*1.2f, width-distance,height-distance)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        currentWidth = width.toFloat()
        currentHeight = height.toFloat()


        //背景
        if (shader == null){
            shader = LinearGradient(
                0f, 0f,  // 起点坐标
                currentWidth, currentHeight,  // 终点坐标
                material500List.toIntArray(),  // 颜色数组
                null,  // 颜色分布（null 表示均匀分布）
                Shader.TileMode.CLAMP // 渐变模式
            )
        }
        //选中时的背景
        if (alphaShader == null){
            alphaShader = LinearGradient(
                0f, 0f,  // 起点坐标
                currentWidth, currentHeight,  // 终点坐标
                halfAlphaMaterial500List.toIntArray(),  // 颜色数组
                null,  // 颜色分布（null 表示均匀分布）
                Shader.TileMode.CLAMP // 渐变模式
            )
        }
        val centerX = width / 2f
        val centerY = height / 2f
        //半径
        val radius = min(width-distance, height-distance) * 0.4f


        // 绘制背景圆盘
        drawDialPlate(canvas, centerX, centerY, radius)

        // 绘制刻度
        drawScales(canvas, centerX, centerY, radius)

        // 绘制指针
        rectFProcessArc!! .apply {
            drawPointer(canvas, centerX(), centerY(), radius)
        }

        // 绘制中心点
        drawCenterPoint(canvas, centerX, centerY)

        // 绘制数值
        drawValueText(canvas, centerX, centerY)
    }

    private fun drawDialPlate(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        //绘制底部背景圆
        paint.color = Color.parseColor("#ffffff")
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)

        //绘制半弧
//        paint.color = Color.parseColor("#E0E0E0")
        coloursPaint.style = Paint.Style.STROKE
        coloursPaint.strokeWidth = 10F.dp
        coloursPaint.shader = shader
        coloursPaint.strokeJoin = Paint.Join.ROUND
        coloursPaint.strokeCap = Paint.Cap.ROUND

        canvas.drawArc(rectFArc!!,startAngle,240F, false, coloursPaint)

        //绘制进度
        val pointerAngle = (currentValue - minValue) / (maxValue - minValue) * sweepAngle
        Log.e("TAG", "角度: "+pointerAngle )
        coloursPaint.strokeWidth = alphaHeight
        coloursPaint.shader = alphaShader
        coloursPaint.strokeJoin = Paint.Join.BEVEL
        coloursPaint.strokeCap = Paint.Cap.BUTT
        Log.e("TAG", "drawDialPlate: 左"+distance*1.2 )
        Log.e("TAG", "drawDialPlate: 上"+distance*1.2 )
        Log.e("TAG", "drawDialPlate: 右"+(width-distance) )
        Log.e("TAG", "drawDialPlate: 下"+(height-distance) )
        canvas.drawArc(rectFProcessArc!!,startAngle,pointerAngle, false, coloursPaint)

    }

    private fun drawScales(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val scaleLength = radius * 0.15f
//        val textRadius = min(width, height) * 0.4f

        val textRadius = radius * 1.25f

        paint.color = scaleColor
        paint.style = Paint.Style.STROKE
        paint.textSize = radius * 0.1f
        paint.textAlign = Paint.Align.CENTER

        // 绘制刻度线
        for (i in 0..scaleCount) {
            paint.strokeWidth = 4f
            val angle = startAngle + i * (sweepAngle / scaleCount)
            val rad = Math.toRadians(angle.toDouble())

            val startX = centerX + (radius - scaleLength) * cos(rad).toFloat()
            val startY = centerY + (radius - scaleLength) * sin(rad).toFloat()

            val endX = centerX + radius * cos(rad).toFloat()
            val endY = centerY + radius * sin(rad).toFloat()

            canvas.drawLine(startX, startY, endX, endY, paint)

            paint.strokeWidth = 1f
            // 绘制刻度值
            val value = minValue + i * (maxValue - minValue) / scaleCount
            val valueText = if (value % 1 == 0f) value.toInt().toString() else "%.1f".format(value)

            val textX = centerX + textRadius * cos(rad).toFloat()
            val textY = centerY + textRadius * sin(rad).toFloat() + paint.textSize / 3

            canvas.drawText(valueText, textX, textY, paint)
        }
    }

    private fun drawPointer(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val pointerAngle = startAngle + ((currentValue - minValue) / (maxValue - minValue) * sweepAngle)
        Log.e("TAG", "角度坐标: "+pointerAngle )
        Log.e("TAG", "drawDialPlate: 圆心"+centerX )
        Log.e("TAG", "drawDialPlate: 圆心"+centerY )

        val rad = Math.toRadians(pointerAngle.toDouble())

        //基于弧形的rect算出半径
        val pointerLength = (height-distance*1.2f-distance*1.2f )/2 - alphaHeight/2.toFloat()+10

        // 绘制指针
        paint.color = pointerColor
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 8f
        paint.strokeCap = Paint.Cap.ROUND
        val cosRad = cos(rad)
        val sinRad = sin(rad)

        //中心点
        val endX = centerX + pointerLength * cosRad
        val endY = centerY + pointerLength * sinRad
        //左定点
        val offsetStartAngle = -2.00
        val offsetStartRad = Math.toRadians(pointerAngle + offsetStartAngle)
        val cosStartOffset = cos(offsetStartRad).toFloat()
        val sinStartOffset = sin(offsetStartRad).toFloat()
        val endXS = centerX + pointerLength * cosStartOffset
        val endYS = centerY + pointerLength * sinStartOffset

        //三角形角度
        val offsetAngle = 2.00
        val offsetRad = Math.toRadians(pointerAngle + offsetAngle)
        val cosOffset = cos(offsetRad).toFloat()
        val sinOffset = sin(offsetRad).toFloat()

        val endXEnd = centerX + (pointerLength + alphaHeight) * cosRad
        val endYEnd = centerY + (pointerLength + alphaHeight) * sinRad
        val endXL = centerX + pointerLength * cosOffset
        val endYL = centerY + pointerLength * sinOffset

        // 5. 绘制三角形
        path.reset()
        path.moveTo(endXS.toFloat(), endYS.toFloat())
        path.lineTo(endXEnd.toFloat(), endYEnd.toFloat())
        path.lineTo(endXL, endYL)
        path.close()


        coloursPaint.style = Paint.Style.FILL_AND_STROKE
        coloursPaint.strokeWidth = 1F.dp
        coloursPaint.shader = shader
        canvas.drawPath(path,coloursPaint)
//        canvas.drawLine(centerX, centerY, endX.toFloat(), endY.toFloat(), paint)
//
//        // 绘制指针头
//        paint.style = Paint.Style.FILL
//        canvas.drawCircle(endX.toFloat(), endY.toFloat(), 10f, paint)
    }

    private fun drawCenterPoint(canvas: Canvas, centerX: Float, centerY: Float) {
//        paint.color = Color.parseColor("#BDBDBD")
//        canvas.drawCircle(centerX, centerY, 15f, paint)

        paint.color = Color.WHITE
        canvas.drawCircle(centerX, centerY, 10f, paint)
    }

    private fun drawValueText(canvas: Canvas, centerX: Float, centerY: Float) {
        paint.color = processTextColor
        paint.style = Paint.Style.FILL
        paint.textSize = height * 0.1f
        paint.textAlign = Paint.Align.CENTER
        paint.setTypeface(Typeface.DEFAULT)

        // 绘制当前值
        val valueText = "%.0f".format(currentValue)
        paint.getTextBounds(valueText, 0, valueText.length, textBounds)
        canvas.drawText(valueText, centerX, centerY - textBounds.height(), paint)

        // 绘制单位
        paint.color = textColor
        paint.textSize = height * 0.08f
        paint.setTypeface(Typeface.DEFAULT_BOLD)
        paint.getTextBounds(unit, 0, unit.length, textBounds)
        canvas.drawText(unit, centerX, centerY + textBounds.height(), paint)
    }


    fun setValue(value: Float, animate: Boolean = true) {
        val clampedValue = value.coerceIn(minValue, maxValue)
//        if (value.toInt() > 55){
//            unit = "贪婪"
//        }else{
//            unit = "中性"
//        }

        if (animate) {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(currentValue, clampedValue).apply {
                duration = 800
                interpolator = null
                addUpdateListener { animation ->
                    currentValue = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            currentValue = clampedValue
            invalidate()
        }
    }
}