package com.web.kokoro.backend.config

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import javax.crypto.SecretKey

@Configuration
class JwtConfig {

    @Value("\${jwt.secret}")
    private lateinit var secretString: String

    @Bean
    fun secretKey(): SecretKey {
        // 解码Base64密钥
        val decodedKey = Base64.getDecoder().decode(secretString)
        return Keys.hmacShaKeyFor(decodedKey)
    }
}