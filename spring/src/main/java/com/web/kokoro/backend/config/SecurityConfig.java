package com.web.kokoro.backend.config;
import com.web.kokoro.backend.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // 使用JWT，不需要csfr
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register","/login").permitAll() // 允许注册接口公开访问
                        .anyRequest().authenticated() // 其他接口需要认证
                )
                .httpBasic().disable() // 禁用HTTP Basic认证
                .formLogin().disable() // 禁用表单登录
                .csrf().disable(); // 禁用CSRF保护（API通常不需要）
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        // 创建一个空的UserDetailsService，不包含任何用户
//        return new InMemoryUserDetailsManager(); // 无参构造会创建一个空的用户存储
//    }
}