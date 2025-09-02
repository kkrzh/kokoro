package com.mobile.hotel

import android.util.Log
import com.google.gson.Gson
import com.mobile.hotel.model.BaseModel
import java.lang.reflect.Type


/**
 * Created by xianjie on 2025年2月25日11:24:33
 *
 * Description:
 */

/**
 * 对脱壳或未脱壳的数据json解析
 * 注意不要嵌套传入泛型，泛型擦除会导致无法获取嵌套中的具体类型
 * 对于数组使用下方的重写方法处理吧
 */
fun <T> safeParse(
    result: String?, clazz: Class<T>, errorCallback: ((String) -> Unit),
    successCallback: ((String, T) -> Unit)
) {
    parseResponse(result, errorCallback) { msg, data ->
        val dataDetail = runCatching {
            Gson().fromJson<T>(data, clazz);
        }.getOrNull()
        if (dataDetail != null) {
            successCallback.invoke(msg, dataDetail)
        } else {
            errorCallback.invoke("解析数据为空")
        }
    }
}

/**
 * 解析List
 */
fun <T> safeParseList(
    result: String?, typeOfT: Type, errorCallback: ((String) -> Unit),
    successCallback: ((String, T) -> Unit)
) {
    parseResponse(result, errorCallback) { msg, data ->
        val dataDetail = runCatching {
            Gson().fromJson<T>(data, typeOfT)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        if (dataDetail != null) {
            successCallback.invoke(msg, dataDetail)
        } else {
            errorCallback.invoke("解析数据为空")
        }
    }
}

/**
 * 数据脱壳
 */
fun parseResponse(
    result: String?,
    errorCallback: ((String) -> Unit),
    successCallback: ((String, String) -> Unit)
) {
    if (result != null) {
        runCatching {
            val model = Gson().fromJson(result, BaseModel::class.java)
            val data = model.yunos_ebanma_right_platform_call_response
            if (data != null){
                if ((data.status ?: "-1") == "0"){
                    successCallback.invoke(data.message?:"",data.value?:"")
                }else{
                    errorCallback.invoke(data.message?:"data.status != 0")
                }
            }else{
                errorCallback.invoke("yunos_ebanma_right_platform_call_response field must not be blank")
            }
        }.onFailure {
            Log.e("NET", "NetUtils.parseResponse: ${it}")
            errorCallback.invoke(it.message ?: "解析异常")
        }
    } else {
        errorCallback.invoke("net api response body is null")
    }
}

