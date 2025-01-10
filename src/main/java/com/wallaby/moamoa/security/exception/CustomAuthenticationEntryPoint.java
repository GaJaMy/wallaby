package com.wallaby.moamoa.security.exception;

import com.wallaby.moamoa.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증 도중에 발생한 에러 처리기
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //        log.debug("Unauthorized error: {}", ErrorCode.INCORRECT_AUTH_INFO.getCode());
//        ResponseMaker.makeErrorResponse(response, HttpServletResponse.SC_OK, ErrorCode.INCORRECT_AUTH_INFO);
        if (authException instanceof BadCredentialsException) {
            // 비밀번호 불일치
            log.debug("Unauthorized error: {}", ErrorCode.INCORRECT_AUTH_INFO.getCode());
//            ResponseMaker.makeErrorResponse(response, HttpServletResponse.SC_OK, ErrorCode.INCORRECT_AUTH_INFO);
        } else {
            // 기타 인증 실패 (등록되지 않은 URL, 헤더 누락 등)
            log.debug("Unauthorized error: {}", ErrorCode.INCORRECT_AUTH_INFO.getCode());
//            ResponseMaker.makeErrorResponse(response, HttpServletResponse.SC_OK, ErrorCode.INCORRECT_REQUEST_URL);
        }
    }
}
