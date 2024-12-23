package com.wallaby.moamoa.security.exception;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

// 인가 과정 중, 권한이 일치하지 않을 시 여기로 반환
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.debug("UnAuthorize error: {}", ErrorCode.ACCESS_DENIED.getLogMsg());
        ResponseMaker.makeErrorResponse(response, HttpServletResponse.SC_OK, ErrorCode.ACCESS_DENIED);
    }
}
