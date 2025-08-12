package com.web.kokoro.backend.core.user.service

import com.web.kokoro.backend.base.Result
import com.web.kokoro.backend.base.jwt.JwtUtils
import com.web.kokoro.backend.core.user.RegisterRequest
import com.web.kokoro.backend.mapper.UserMapper
import com.web.kokoro.backend.pojo.UserEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userMapper: UserMapper, private val jwtConfig: JwtUtils) {

    //密码加密解密
    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    /**
     * 注册
     */
    fun register(requestBody: RegisterRequest): Result {
        if (requestBody.password.length < 8) {
            return Result.error(500, "密码至少8位")
        }

        if (userMapper.getByUsernameAndPassword(requestBody) != null) {
            return Result.error("用户名已存在")
        }

        val encryptedPassword = passwordEncoder.encode(requestBody.password)
        userMapper.insert(UserEntity(username = requestBody.username, password = encryptedPassword, name = generateRandomUsername()))
        return Result.success()
    }

    /**
     * 生成随机用户名
     */
    private fun generateRandomUsername(): String {
        // 使用UUID + 随机数生成唯一用户名
        val uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8)
        val randomNum = Random().nextInt(1000)
        return "user_$uuid$randomNum"
    }

    /**
     * 登录
     */
    fun login(requestBody: RegisterRequest): Result {
        // 查找用户

        val userEmp = userMapper.getByUsernameAndPassword(requestBody)?:return Result.error(500,"用户不存在")

        // 验证密码
        if (!passwordEncoder.matches(requestBody.password, userEmp.password)) {
            return Result.error(500, "用户名或密码错误")
        }

        val jwt = jwtConfig.genJwt(mapOf<String, Any?>("id" to userEmp.id, "username" to userEmp.username))
        //返回查询结果给Controller
        return Result.success(jwt)
    }

}