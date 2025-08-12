package com.web.kokoro.backend.base

object ResultCode {
    // ================ 成功状态码 (2xx) ================
    const val SUCCESS = 200 // 请求成功
    const val CREATED = 201 // 资源创建成功
    const val ACCEPTED = 202 // 请求已接受但未处理完成
    const val NO_CONTENT = 204 // 请求成功但无返回内容

    // ================ 重定向状态码 (3xx) ================
    const val MOVED_PERMANENTLY = 301 // 资源永久重定向
    const val FOUND = 302 // 资源临时重定向
    const val SEE_OTHER = 303 // 重定向到其他资源
    const val NOT_MODIFIED = 304 // 资源未修改（缓存）

    // ================ 客户端错误 (4xx) ================
    const val BAD_REQUEST = 400 // 请求参数错误
    const val UNAUTHORIZED = 401 // 未授权/身份验证失败
    const val FORBIDDEN = 403 // 禁止访问（权限不足）
    const val NOT_FOUND = 404 // 资源不存在
    const val METHOD_NOT_ALLOWED = 405 // 请求方法不允许
    const val NOT_ACCEPTABLE = 406 // 请求格式不可接受
    const val CONFLICT = 409 // 资源冲突
    const val GONE = 410 // 资源已永久删除
    const val UNSUPPORTED_MEDIA_TYPE = 415 // 不支持的媒体类型
    const val UNPROCESSABLE_ENTITY = 422 // 请求格式正确但语义错误
    const val TOO_MANY_REQUESTS = 429 // 请求过于频繁

    // ================ 服务端错误 (5xx) ================
    const val ERROR = 500 // 服务器内部错误
    const val NOT_IMPLEMENTED = 501 // 功能未实现
    const val BAD_GATEWAY = 502 // 网关错误
    const val SERVICE_UNAVAILABLE = 503 // 服务不可用
    const val GATEWAY_TIMEOUT = 504 // 网关超时
    const val HTTP_VERSION_NOT_SUPPORTED = 505 // HTTP版本不支持
}

data class Result(
    val code: Int,
    val msg: String,
    val data: Any?
) {
    companion object{
        @JvmStatic
        fun success(data: Any? = null): Result =
            Result(ResultCode.SUCCESS, "成功", data)

        @JvmStatic
        fun error(code: Int = ResultCode.ERROR, msg: String): Result =
            Result(code, msg, null)
    }
}