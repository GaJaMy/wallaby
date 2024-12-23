package com.wallaby.moamoa.domain.auth.dto.request;

import com.wallaby.moamoa.common.type.SocialsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninMemberRequestDto {
    private String uuid;
    private SocialsType socialsType;
    private String fcmToken;
}
