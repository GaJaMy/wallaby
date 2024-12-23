package com.wallaby.moamoa.domain.auth.dto.response;

import com.wallaby.moamoa.domain.auth.dto.request.SigninMemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninMemberResponseDto {
    private String accessToken;

    public static SigninMemberResponseDto buildSigninMemberResponseDto(String accessToken) {
        return SigninMemberResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
