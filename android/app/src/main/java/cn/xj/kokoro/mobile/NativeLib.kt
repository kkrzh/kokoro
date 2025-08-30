package cn.xj.kokoro.mobile;

/**
 * Created by you on 2018-03-12.
 * 图像数据nv21,i420, nv12, yv12的一些转换裁剪旋转的相关操作
 */
public final class NativeLib {

    private NativeLib() {}

    static {
        System.loadLibrary("lib-kokoro");
    }

    public static native String helloWorld();

}
