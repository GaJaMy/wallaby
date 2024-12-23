package com.wallaby.moamoa.common.util.authorize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

// API의 권한 정보를 담고 있는 클래스
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationInfo {
    private String url;             // 경로
    private HttpMethod method;      // 메서드
    private String[] roles;         // 권한
}
