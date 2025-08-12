package com.web.kokoro.backend.base.jwt
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils {

    @Value("\${jwt.secret}")
    private lateinit var secretString: String

    @Value("\${jwt.expiration}")
    private var expirationMs: Long = 86400000 // 默认24小时

    private lateinit var secretKey: SecretKey

    @PostConstruct
    fun init() {
        val decodedKey = Base64.getDecoder().decode(secretString)
        secretKey = Keys.hmacShaKeyFor(decodedKey)
    }

    fun genJwt(claims: Map<String, Any?>): String {
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + expirationMs))
            .setIssuer("kokoro-app")
            .setAudience("web-client")
            .setId(UUID.randomUUID().toString())
            .signWith(secretKey)
            .compact()
    }

    fun parseJwt(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String): Boolean {
        return try {
            parseJwt(token) // 解析成功即有效
            true
        } catch (e: Exception) {
            false
        }
    }
}