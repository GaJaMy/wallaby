package com.wallaby.moamoa.common.util.authorize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 인증 정보 객체 리스트를 가지는 클래스
// SecurityConfig와 JwtTokenFilter에서 Authorization을 할 API 인지 판단하는데 쓰임
@Getter
@AllArgsConstructor
@Component
public class AuthorizationInfoRegistry {
    private final List<AuthorizationInfo> objectList;

    public AuthorizationInfoRegistry() {
        this.objectList = makeObjectList();
    }

    private List<AuthorizationInfo> makeObjectList() {
        return new ArrayList<>();
    }
}
