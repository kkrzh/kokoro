package cn.xj.kokoro.mobile.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by xianjie on 2023年1月4日10:27:15
 *
 * Description:
 */
class FlagView: View {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    private val paint: Paint by lazy{
        Paint().apply {
            isAntiAlias = true
            strokeWidth = 10f
        }
    }
    private val path: Path by lazy {
        Path()
    }

    private var labelWidth = 0f

    private var labelColor = Color.parseColor("#FFF44336")

    private var centerX = 0f

    private var centerY = 0f

    private var text: String = ""

    private var textColor = Color.WHITE

    private var textSize = 38F


    fun setFlag(text:String,color:Int,textSize:Float){
        this.text = text
        labelColor = color
        this.textSize = textSize
        invalidate()
    }


    fun setFlag(type:String){
        when(type){
            "debug" ->{
                text = "调试版"
                labelColor = Color.parseColor("#FFF44336")
            }
            "beta"->{
                text = "内测版"
                labelColor = Color.parseColor("#FFE91E63")
            }
        }
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        labelWidth = width / 2F
        setBackgroundColor(Color.TRANSPARENT)
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()

        //画标签背景区域
        path.reset()
        paint.color = labelColor
        paint.style = Paint.Style.FILL
        path.moveTo(width - labelWidth , 0F)
        path.lineTo(width.toFloat(), 0F)
        path.lineTo(0F, height.toFloat())
        path.lineTo(0F, height - labelWidth)
        canvas.drawPath(path, paint)

        //画文字 逆时针选择45度
        canvas.rotate(-45F, centerX, centerY)

        //文字中心点横坐标
        val textX = width / 2

        //文字中心点纵坐标
        val textY = (height - labelWidth / 2f) / 2f
        paint.color = textColor
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        paint.isFakeBoldText = true
        //设置文字居中绘制
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, textX.toFloat(), textY, paint)
    }
}