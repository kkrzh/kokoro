package cn.xj.kokoro.mobile

import android.graphics.Bitmap

class FeatureMatcher {

    companion object {
        // 加载本地库
        init {
            System.loadLibrary("feature-matcher")
        }

        // 声明本地方法
        private external fun nativeMatchFeatures(bitmap1: Bitmap, bitmap2: Bitmap): Long
        private external fun nativeDrawMatches(
            bitmap1: Bitmap,
            bitmap2: Bitmap,
            resultPtr: Long,
            resultBitmap: Bitmap
        )
    }

    // 执行特征匹配
    fun matchFeatures(bitmap1: Bitmap, bitmap2: Bitmap): Long {
        // 创建可修改的 Bitmap 副本
        val mutableBitmap1 = bitmap1.copy(Bitmap.Config.ARGB_8888, true)
        val mutableBitmap2 = bitmap2.copy(Bitmap.Config.ARGB_8888, true)

        return nativeMatchFeatures(mutableBitmap1, mutableBitmap2)
    }

    // 绘制匹配结果
    fun drawMatches(
        bitmap1: Bitmap,
        bitmap2: Bitmap,
        resultPtr: Long,
        outputBitmap: Bitmap
    ) {
        // 创建可修改的 Bitmap 副本
        val mutableBitmap1 = bitmap1.copy(Bitmap.Config.ARGB_8888, true)
        val mutableBitmap2 = bitmap2.copy(Bitmap.Config.ARGB_8888, true)
        val mutableOutput = outputBitmap.copy(Bitmap.Config.ARGB_8888, true)

        nativeDrawMatches(mutableBitmap1, mutableBitmap2, resultPtr, mutableOutput)
    }
}