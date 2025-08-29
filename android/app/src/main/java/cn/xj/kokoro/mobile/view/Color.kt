package cn.xj.kokoro.mobile.view

import android.graphics.Color
import kotlin.random.Random

/**
 * Created by xianjie on 2025年2月24日20:02:13
 *
 * Description:
 */
val RED_500                   =                      Color.parseColor("#FFE91E")
val PINK_500                  =                      Color.parseColor("#FFE91E63")
val PURPLE_500                =                      Color.parseColor("#FF9C27B0")
val DEEP_PURPLE_500           =                      Color.parseColor("#FF673AB7")
val INDIGO_500                =                      Color.parseColor("#FF3F51B5")
val BLUE_500                  =                      Color.parseColor("#FF2196F3")
val LIGHT_BLUE_500            =                      Color.parseColor("#FF03A9F4")
val CYAN_500                  =                      Color.parseColor("#FF00BCD4")
val TEAL_500                  =                      Color.parseColor("#FF009688")
val GREEN_500                 =                      Color.parseColor("#FF4CAF50")
val LIGHT_GREEN_500           =                      Color.parseColor("#FF8BC34A")
val LIME_500                  =                      Color.parseColor("#FFCDDC39")
val YELLOW_500                =                      Color.parseColor("#FFFFEB3B")
val AMBER_500                 =                      Color.parseColor("#FFFFC107")
val ORANGE_500                =                      Color.parseColor("#FFFF9800")
val DEEP_ORANGE_500           =                      Color.parseColor("#FFFF5722")
val BROWN_500                 =                      Color.parseColor("#FF795548")
val GREY_500                  =                      Color.parseColor("#FF9E9E9E")
val BLUE_GREY_500             =                      Color.parseColor("#FF607D8B")

val material500List = arrayListOf(
    RED_500, PINK_500, PURPLE_500, DEEP_PURPLE_500, INDIGO_500, BLUE_500,
    LIGHT_BLUE_500, CYAN_500, TEAL_500, GREEN_500, LIGHT_GREEN_500, LIME_500, YELLOW_500, AMBER_500,
    ORANGE_500, DEEP_ORANGE_500, BROWN_500, GREY_500, BLUE_GREY_500
)

// 生成透明度减半的色组
val halfAlphaMaterial500List = material500List.map { color ->
    // 获取当前颜色的ARGB分量
    val alpha = Color.alpha(color) / 2  // 透明度减半
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)

    // 创建新颜色（透明度减半）
    Color.argb(alpha, red, green, blue)
}

var lastColor = 0
fun random500Color(): Int {
    var numberInRange = Random.nextInt(0, material500List.size-1)
    while (numberInRange == lastColor){
        numberInRange = Random.nextInt(0, material500List.size-1)
    }
    lastColor = numberInRange
    return material500List[numberInRange]
}