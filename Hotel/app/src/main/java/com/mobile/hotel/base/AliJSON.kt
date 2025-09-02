package com.mobile.hotel.base

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject

/**
Created by Zebra-RD张先杰 on 2022年8月3日13:53:02

Description:JSON解析类，增强方法安全性健壮性
 */

fun String.initializeToObject(): JSONObject =
    JSON.parseObject(this)

fun String.initializeToArray(): JSONArray =
    JSON.parseArray(this)

/**
 * 字符串 直接取值
 */
fun String.getJSONFields(str: String, defaultSting: String = ""): String =
    initializeToObject().getJSONFields(str, defaultSting)

/**
 * JSONObject 获取 String
 */
fun JSONObject.getJSONFields(str: String, defaultString: String = ""): String =
    getString(str) ?: defaultString

/**
 * JSONArray 直接获取 子JSONObject 中的 String
 */
fun JSONArray.getJSONArrayItemFields(
    position: Int,
    str: String,
    defaultSting: String = "",
): String {
    return if (this.size > position) getJSONObject(position).getJSONFields(str, defaultSting)
    else defaultSting
}

/**
 * 字符串直接 获取 不为空的JSONObject
 */
fun String.getSecureJSONObject(str: String): JSONObject =
    initializeToObject().getSecureJSONObject(str)

/**
 * 字符串直接 获取 不为空的JSONObject
 */
fun JSONObject.getSecureJSONObject(str: String): JSONObject =
    getJSONObject(str)?:JSONObject()

/**
 * String 获取 安全的JSONArray
 */
fun String.getSecureJSONArray(str: String): JSONArray =
    initializeToObject().getSecureJSONArray(str)


/**
 * JSONObject 获取 安全的JSONArray
 */
fun JSONObject.getSecureJSONArray(str: String): JSONArray {
    val result = runCatching {
        getJSONArray(str)
    }
    return result.getOrNull() ?: JSONArray()
}



