//
// Created by xianj on 2025/9/2.
//
#include "bitmap_utils.h"

using namespace cv;

Mat BitmapUtils::bitmapToMat(JNIEnv *env, jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels = nullptr;

    // 获取 Bitmap 信息
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) {
        return Mat();
    }

    // 锁定 Bitmap 像素
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) {
        return Mat();
    }

    // 根据格式创建 OpenCV Mat
    Mat mat;
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        mat = Mat(info.height, info.width, CV_8UC4, pixels);
        cvtColor(mat, mat, COLOR_RGBA2BGRA); // Android 是 RGBA, OpenCV 默认是 BGRA
    } else if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        mat = Mat(info.height, info.width, CV_8UC2, pixels);
        cvtColor(mat, mat, COLOR_BGR5652BGR);
    } else {
        // 处理其他格式
        ConvertArgbToMat(env, bitmap, info, pixels, mat);
    }

    // 解锁 Bitmap
    AndroidBitmap_unlockPixels(env, bitmap);

    return mat.clone(); // 返回副本，避免原始像素数据变更导致的意外行为
}

void BitmapUtils::matToBitmap(JNIEnv *env, const Mat& mat, jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels = nullptr;

    // 获取 Bitmap 信息
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) {
        return;
    }

    // 锁定 Bitmap 像素
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) {
        return;
    }

    // 根据源 Mat 格式和目标 Bitmap 格式进行转换
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat tmp(info.height, info.width, CV_8UC4, pixels);
        if (mat.type() == CV_8UC4) {
            cvtColor(mat, tmp, COLOR_BGRA2RGBA);
        } else if (mat.type() == CV_8UC3) {
            cvtColor(mat, tmp, COLOR_BGR2RGBA);
        } else if (mat.type() == CV_8UC1) {
            cvtColor(mat, tmp, COLOR_GRAY2RGBA);
        } else {
            // 处理不支持的格式
            tmp = Scalar(0, 0, 0, 255);
        }
    } else if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        Mat tmp(info.height, info.width, CV_8UC2, pixels);
        if (mat.type() == CV_8UC3 || mat.type() == CV_8UC4) {
            cvtColor(mat, tmp, COLOR_BGR2BGR565);
        } else if (mat.type() == CV_8UC1) {
            cvtColor(mat, tmp, COLOR_GRAY2BGR565);
        } else {
            // 处理不支持的格式
            tmp = Scalar(0, 0, 0);
        }
    } else {
        // 处理其他格式
        ConvertMatToArgb(env, bitmap, info, pixels, mat);
    }

    // 解锁 Bitmap
    AndroidBitmap_unlockPixels(env, bitmap);
}

// 转换 ARGB_8888 格式到 Mat
void BitmapUtils::ConvertArgbToMat(JNIEnv *env, jobject bitmap, AndroidBitmapInfo& info, void* pixels, Mat& dst) {
    // ARGB_8888 格式处理

    Mat srcRGBA(info.height, info.width, CV_8UC4, pixels);

    // 根据源格式创建目标 Mat
    if (dst.empty()) {
        dst = Mat(info.height, info.width, CV_8UC4);
    }

    // 转换格式
    cvtColor(srcRGBA, dst, COLOR_RGBA2BGRA);
}

// 转换 Mat 到 ARGB_8888 格式
void BitmapUtils::ConvertMatToArgb(JNIEnv *env, jobject bitmap, AndroidBitmapInfo& info, void* pixels, const Mat& src) {
    Mat srcMat;

    // 确保源 Mat 是 8UC4 格式
    if (src.type() != CV_8UC4) {
        if (src.type() == CV_8UC3) {
            cvtColor(src, srcMat, COLOR_BGR2BGRA);
        } else if (src.type() == CV_8UC1) {
            cvtColor(src, srcMat, COLOR_GRAY2BGRA);
        } else {
            srcMat = src;
        }
    } else {
        srcMat = src;
    }

    // 锁定 Bitmap 像素
    pixels= nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) {
        return;
    }

    // 创建目标 Mat
    Mat dstRGBA(info.height, info.width, CV_8UC4, pixels);

    // 转换格式
    cvtColor(srcMat, dstRGBA, COLOR_BGRA2RGBA);

    // 解锁 Bitmap
    AndroidBitmap_unlockPixels(env, bitmap);
}