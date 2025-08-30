package cn.xj.kokoro.mobile

class NativeLib {
    // 声明本地方法
    external fun helloWorld(): String
    external fun showText(input: String,out: String)

    companion object {
        val INSTANT = NativeLib()
        // 加载本地库
        init {
            System.loadLibrary("kokoro_opencv")
        }
    }
}
