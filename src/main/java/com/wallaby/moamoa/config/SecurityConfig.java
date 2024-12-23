package com.wallaby.moamoa.config;

import com.wallaby.moamoa.common.util.authorize.AuthorizationInfo;
import com.wallaby.moamoa.common.util.authorize.AuthorizationInfoRegistry;
import com.wallaby.moamoa.security.JwtTokenManager;
import com.wallaby.moamoa.security.exception.CustomAccessDeniedHandler;
import com.wallaby.moamoa.security.exception.CustomAuthenticationEntryPoint;
import com.wallaby.moamoa.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthorizationInfoRegistry authorizationInfoRegistry;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTemplate<String,String> redisTemplate;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final AesManager aesManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configuration PasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuration security filter chain set up start");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(new JwtTokenFilter(authorizationInfoRegistry, jwtTokenManager, redisTemplate)
                        , UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    for (AuthorizationInfo info : authorizationInfoRegistry.getObjectList()) {
                        if (info.getRoles() != null) {
                            auth.requestMatchers(info.getMethod(), info.getUrl()).hasAnyRole(info.getRoles());
                            log.info("Add authorization method={},url={},roles={}",info.getMethod(), info.getUrl(), info.getRoles());
                        } else {
                            auth.requestMatchers(info.getMethod(), info.getUrl()).permitAll();
                            log.info("Permit All method={},url={},roles={}",info.getMethod(), info.getUrl(), info.getRoles());
                        }
                    }

                    auth.anyRequest().denyAll();
                });

        SecurityFilterChain filterChain = http.build();
        log.info("Complete security filter chain setup");

        return filterChain;
    }
}
