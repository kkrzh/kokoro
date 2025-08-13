package com.web.kokoro.backend.base

sealed class  ResultCode(val code: Int, val msg: String) {
    // ================ 成功状态码 (2xx) ================
    object SUCCESS : ResultCode(200, "成功")
    object CREATED : ResultCode(201, "资源创建成功")
    object ACCEPTED : ResultCode(202, "请求已接受但未处理完成")
    object NO_CONTENT : ResultCode(204, "请求成功但无返回内容")

    // ================ 重定向状态码 (3xx) ================
    object MOVED_PERMANENTLY : ResultCode(301, "资源永久重定向")
    object FOUND : ResultCode(302, "资源临时重定向")
    object SEE_OTHER : ResultCode(303, "重定向到其他资源")
    object NOT_MODIFIED : ResultCode(304, "资源未修改（缓存）")

    // ================ 客户端错误 (4xx) ================
    object BAD_REQUEST : ResultCode(400, "请求参数错误")
    object UNAUTHORIZED : ResultCode(401, "未授权/身份验证失败")
    object FORBIDDEN : ResultCode(403, "禁止访问（权限不足）")
    object NOT_FOUND : ResultCode(404, "资源不存在")
    object METHOD_NOT_ALLOWED : ResultCode(405, "请求方法不允许")
    object NOT_ACCEPTABLE : ResultCode(406, "请求格式不可接受")
    object CONFLICT : ResultCode(409, "资源冲突")
    object GONE : ResultCode(410, "资源已永久删除")
    object UNSUPPORTED_MEDIA_TYPE : ResultCode(415, "不支持的媒体类型")
    object UNPROCESSABLE_ENTITY : ResultCode(422, "请求格式正确但语义错误")
    object TOO_MANY_REQUESTS : ResultCode(429, "请求过于频繁")

    // ================ 服务端错误 (5xx) ================
    object ERROR : ResultCode(500, "服务器内部错误")
    object NOT_IMPLEMENTED : ResultCode(501, "功能未实现")
    object BAD_GATEWAY : ResultCode(502, "网关错误")
    object SERVICE_UNAVAILABLE : ResultCode(503, "服务不可用")
    object GATEWAY_TIMEOUT : ResultCode(504, "网关超时")
    object HTTP_VERSION_NOT_SUPPORTED : ResultCode(505, "HTTP版本不支持")
}

data class Result(
    val code: Int,
    val msg: String,
    val data: Any?
) {
    companion object{
        @JvmStatic
        fun success(data: Any? = null): Result =
            Result(ResultCode.SUCCESS.code, "成功", data)

        @JvmStatic
        fun success(): Result =
            Result(ResultCode.SUCCESS.code, "成功", null)

        @JvmStatic
        fun error(code: Int, msg: String): Result =
            Result(code, msg, null)

        @JvmStatic
        fun error(msg: String): Result =
            Result(500, msg, null)

        @JvmStatic
        fun error(result: ResultCode): Result =
            Result(result.code, result.msg, null)
    }
}