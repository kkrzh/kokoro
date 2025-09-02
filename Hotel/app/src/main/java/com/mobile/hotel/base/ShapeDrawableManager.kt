package com.mobile.hotel.base

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build

/**
Created by Zebra-RD张先杰 on 2022年10月25日09:56:32

Description:以kotlin代码的形式动态构建Shape，而非通过xml
 */
/**
 * 基础的Shape图形
 * @param radius 圆角
 * @param backGroundColor 背景颜色
 * @param
 */
fun createBasicShape(
    radius: Float = 0F,
    backGroundColor: Int? = null,
    strokeWidth: Int = 0,
    strokeColor: Int = Color.BLACK,
    height: Int? = null, width: Int? = null
): Drawable = GradientDrawable().apply {
    if (height != null && width != null) setSize(height, width)
    cornerRadius = radius
    if(backGroundColor != null)setColor(backGroundColor)
    setStroke(strokeWidth, strokeColor)
}

/**
 * 渐变的Shape
 * android 10以上可以通过[setColors]api控制比例，10以下需要通过backGroundColor，重复相同颜色控制比例
 */
fun createGradientShape(
    radius: Float = 0F,
    backGroundColor: IntArray = intArrayOf(),
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT,
    strokeWidth: Int = 0,
    strokeColor: Int = Color.BLACK,
    positions: FloatArray? = null,
): Drawable = GradientDrawable(orientation, backGroundColor).apply {
    cornerRadius = radius
    setStroke(strokeWidth, strokeColor)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && positions != null) {
        setColors(colors, positions)
    }
}