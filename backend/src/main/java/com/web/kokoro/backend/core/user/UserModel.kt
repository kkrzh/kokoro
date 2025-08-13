package com.web.kokoro.backend.core.user

data class UserEntity(val id: Int? = null,
                      val username: String,
                      val password: String,
                      val name: String)
data class RegisterRequest(val username:String, val password:String)
