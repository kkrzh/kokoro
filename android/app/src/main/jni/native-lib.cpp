#include <jni.h>
#include <string>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/myfontface.h>
#include <android/log.h>

extern "C" JNIEXPORT jstring JNICALL

Java_cn_xj_kokoro_mobile_NativeLib_helloWorld(JNIEnv *env, jobject) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_cn_xj_kokoro_mobile_NativeLib_showText(JNIEnv *env, jobject,jstring inputPath,jstring outputPath){
    const char* inputPathCStr = env->GetStringUTFChars(inputPath, nullptr);
    const char* outputPathCStr = env->GetStringUTFChars(outputPath, nullptr);

    // 检查转换是否成功
    if (!inputPathCStr || !outputPathCStr) {
        // 错误处理：内存不足
        return;
    }
    __android_log_print(ANDROID_LOG_ERROR, "TAG", "%s input", inputPathCStr);
    __android_log_print(ANDROID_LOG_ERROR, "TAG", "%s output", outputPathCStr);


    cv::Mat bgr = cv::imread(inputPathCStr, 1);

    // use this font
    MyFontFace myfont;

    // draw full-width text with myfont
    const char* zhtext = "称呼机器人为破铜烂铁，\n违反了禁止歧视机器人法！";
    cv::putText(bgr, zhtext, cv::Point(30, 250), cv::Scalar(127, 0, 127), myfont, 20);

    // get bounding rect
    cv::Rect rr = cv::getTextSize(bgr.size(), zhtext, cv::Point(30, 250), myfont, 20);

    cv::imwrite(outputPathCStr, bgr);

}