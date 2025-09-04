package com.web.kokoro.backend.core.deepseek

import com.alibaba.fastjson.JSON
import com.mobile.hotel.model.DeepseekRequestModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration

class DeepseekConfig {
    @Value("\${deepseek.api-key}")
    lateinit var apiKey: String

    fun getRequestBody(content: String,isReasoner: Boolean):RequestBody{
        val requestParams = DeepseekRequestModel(
            model = if (isReasoner) "deepseek-reasoner" else "deepseek-chat",
            messages = arrayListOf(DeepseekRequestModel.Message("标准md格式","system"),DeepseekRequestModel.Message(content,"user"))
        )
        return JSON.toJSONString(requestParams).toRequestBody("application/json;charset=utf-8".toMediaType())
    }
}