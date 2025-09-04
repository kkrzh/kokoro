package com.mobile.hotel.model

import org.apache.ibatis.jdbc.Null

/**
{
  "messages": [
    {
      "content": "You are a helpful assistant",
      "role": "system"
    },
    {
      "content": "Hi",
      "role": "user"
    }
  ],
  "model": "deepseek-chat",
  "frequency_penalty": 0,
  "max_tokens": 4096,
  "presence_penalty": 0,
  "response_format": {
    "type": "text"
  },
  "stop": null,
  "stream": false,
  "stream_options": null,
  "temperature": 1,
  "top_p": 1,
  "tools": null,
  "tool_choice": "none",
  "logprobs": false,
  "top_logprobs": null
}
*/
data class DeepseekRequestModel(
    //介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，降低模型重复相同内容的可能性。
    val frequency_penalty: Int = 0,
    //是否返回所输出 token 的对数概率。如果为 true，则在 message 的 content 中返回每个输出 token 的对数概率。
    val logprobs: Boolean = false,
    //长度
    val max_tokens: Int = 4096,
    //内容
    val messages: List<Message> = listOf(),
    //是否深度思考
    val model: String = "deepseek-chat",
    //介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其是否已在已有文本中出现受到相应的惩罚，从而增加模型谈论新主题的可能性。
    val presence_penalty: Int = 0,
    // [text, json_object]
    val response_format: ResponseFormat = ResponseFormat("text"),
    //敏感词
    val stop: List<String>? = null,
    //sse
    val stream: Boolean = true,
    val stream_options: StreamOptions = StreamOptions(false),
    //采样温度，介于 0 和 2 之间。更高的值，如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定。 我们通常建议可以更改这个值或者更改 top_p，但不建议同时对两者进行修改。
    val temperature: Int = 1,
    val tool_choice: String = "none",
    val tools: Any? = null,
    //一个介于 0 到 20 之间的整数 N，指定每个输出位置返回输出概率 top N 的 token，且返回这些 token 的对数概率。指定此参数时，logprobs 必须为 true。
    val top_logprobs: Int? = null,
    //作为调节采样温度的替代方案，模型会考虑前 top_p 概率的 token 的结果。所以 0.1 就意味着只有包括在最高 10% 概率中的 token 会被考虑。 我们通常建议修改这个值或者更改 temperature，但不建议同时对两者进行修改。
    val top_p: Float = 1F
) {
    data class Message(
        val content: String = "",
        val role: String = ""
    )

    data class ResponseFormat(
        val type: String = ""
    )
    data class StreamOptions(val include_usage: Boolean = false)
}
data class CompletionsRequest(val content:String, val isReasoner: Boolean)
data class DeepseekResponseModel(
    val choices: List<Choice?>? = listOf(),
    val created: Int? = 0,
    val id: String? = "",
    val model: String? = "",
    val `object`: String? = "",
    val system_fingerprint: String? = "",
    val usage: Usage? = Usage()
) {
    data class Choice(
        val delta: Delta? = Delta(),
        val finish_reason: String? = "",
        val index: Int? = 0,
        val logprobs: Any? = Any()
    ) {
        data class Delta(
            val content: String? = ""
        )
    }

    data class Usage(
        val completion_tokens: Int? = 0,
        val prompt_cache_hit_tokens: Int? = 0,
        val prompt_cache_miss_tokens: Int? = 0,
        val prompt_tokens: Int? = 0,
        val prompt_tokens_details: PromptTokensDetails? = PromptTokensDetails(),
        val total_tokens: Int? = 0
    ) {
        data class PromptTokensDetails(
            val cached_tokens: Int? = 0
        )
    }
}