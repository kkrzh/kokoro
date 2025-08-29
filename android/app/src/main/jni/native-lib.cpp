#include <jni.h>
#include <string>
extern "C" JNIEXPORT jstring JNICALL

Java_cn_xj_kokoro_mobile_NativeLib_helloWorld(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}