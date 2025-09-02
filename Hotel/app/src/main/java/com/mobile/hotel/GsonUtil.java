package com.mobile.hotel;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * GSON解析工具类.
 */
public class GsonUtil {

    public static final String TAG = GsonUtil.class.getSimpleName();


    // 创建一个不进行HtmlEscaping的Gson对象
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();


    private GsonUtil() {

    }

    /**
     * Gets the single instance of GsonUtil.
     *
     * @return single instance of GsonUtil
     */
    private static synchronized Gson getInstance() {
        return GSON;
    }

    /**
     * From object to json string.
     *
     * @param object the object
     * @return the string
     */
    public static String fromObjectToJsonString(Object object) {
        String result = null;
        if (object != null) {
            result = getInstance().toJson(object);
        }
        return result;

    }

    /**
     * From json string to obejct.
     *
     * @param <T>        the generic type
     * @param jsonString the json string
     * @param classOfT   the class of t
     * @return the t
     */
    public static <T> T fromJsonStringToObject(String jsonString,
                                               Class<T> classOfT) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return getInstance().fromJson(jsonString, classOfT);
            } catch (Exception e) {
                Log.e(TAG, "fromJsonStringToObject: "+e );
                e.printStackTrace();
            }

        }
        return null;

    }

    public static <T> T fromJsonStringToObject_NOTRYCATCH(String jsonString,
                                               Class<T> classOfT) {
        if (!TextUtils.isEmpty(jsonString)) {
                return getInstance().fromJson(jsonString, classOfT);
        }
        return null;

    }

    /**
     * From json string to collection.
     *
     * @param <T>        the generic type
     * @param jsonString the json string
     * @param typeOfT    the type of t
     * @return the t
     */
    public static <T> T fromJsonStringToCollection(String jsonString, Type typeOfT) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return getInstance().fromJson(jsonString, typeOfT);

            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
        return null;

    }

}
