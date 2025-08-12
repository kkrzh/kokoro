package com.web.kokoro.backend.filter

import com.web.kokoro.backend.base.Result
import com.web.kokoro.backend.base.ResultCode.UNAUTHORIZED
import com.web.kokoro.backend.base.jwt.JwtUtils
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        System.out.println("lanjie")

        //1.获取请求url
        val url: String = request.requestURL.toString()
        System.out.println("请求路径：{}"+url) //请求路径：http://localhost:8080/login
        if(url.contains("/login") || url.contains("/register")){
            filterChain.doFilter(request, response);//放行请求
            return;//结束当前方法的执行
        }

        val token = extractToken(request)

        if (token != null && jwtUtils.validateToken(token)) {
            val claims = jwtUtils.parseJwt(token)
            val auth = createAuthentication(claims)
            SecurityContextHolder.getContext().authentication = auth
        }else{

            //把Result对象转换为JSON格式字符串 (fastjson是阿里巴巴提供的用于实现对象和json的转换工具类)
            val json: String? = com.alibaba.fastjson.JSONObject.toJSONString(Result.error(UNAUTHORIZED))
            response.contentType = "application/json;charset=utf-8"

            //响应
            response.writer.write(json)

            return
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        return if (header != null && header.startsWith("Bearer ")) {
            header.substring(7)
        } else {
            null
        }
    }

    private fun createAuthentication(claims: Claims): Authentication {
        val username = claims.subject
        val roles = claims["roles"] as? List<String> ?: emptyList()
        val authorities = roles.map { SimpleGrantedAuthority(it) }
        return UsernamePasswordAuthenticationToken(username, null, authorities)
    }
}