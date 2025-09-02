//
// Created by xianj on 2025/9/2.
//
#ifndef BITMAP_UTILS_H
#define BITMAP_UTILS_H

#include <jni.h>
#include <android/bitmap.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>

class BitmapUtils {
public:
    static cv::Mat bitmapToMat(JNIEnv *env, jobject bitmap);
    static void matToBitmap(JNIEnv *env, const cv::Mat& mat, jobject bitmap);

private:
    static void ConvertArgbToMat(JNIEnv *env, jobject bitmap, AndroidBitmapInfo& info, void* pixels, cv::Mat& dst);
    static void ConvertMatToArgb(JNIEnv *env, jobject bitmap, AndroidBitmapInfo& info, void* pixels, const cv::Mat& src);
};

#endif // BITMAP_UTILS_H