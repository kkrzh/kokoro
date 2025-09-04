package com.web.kokoro.backend.base

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {
    // 处理 HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException, request: WebRequest
    ): ResponseEntity<Result> {
        // 分析异常原因

        var errorMessage: ResultCode? = null

            println("root 不为空")
            if (ex.message!!.contains("Required request body is missing")) {
                errorMessage = ResultCode.BAD_REQUEST
            } else if (ex.message!!.contains("JSON parse error")) {
                errorMessage = ResultCode.NOT_ACCEPTABLE
            } else if (ex.message!!.contains("Required request part")) {
                errorMessage = ResultCode.CUSTEM(400, "Required request part")
            }else{
                errorMessage = ResultCode.CUSTEM(400, ex.message?:"message be blank")
            }



        // 创建错误响应
        val errorResponse: Result = Result.error (errorMessage)
        println("拦截到异常")

        return ResponseEntity<Result>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    // 处理其他异常
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception?, request: WebRequest): ResponseEntity<Result> {
        return ResponseEntity<Result>(Result.error(ResultCode.ERROR), HttpStatus.BAD_REQUEST)
    }
}