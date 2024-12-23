package com.wallaby.moamoa.domain.auth.service;

import com.wallaby.moamoa.common.dto.response.ResponseDto;
import com.wallaby.moamoa.common.exception.CustomException;
import com.wallaby.moamoa.common.exception.ErrorCode;
import com.wallaby.moamoa.common.type.SocialsType;
import com.wallaby.moamoa.common.util.response.ResponseMaker;
import com.wallaby.moamoa.domain.auth.dto.request.SigninMemberRequestDto;
import com.wallaby.moamoa.domain.auth.dto.request.SignupMemberRequestDto;
import com.wallaby.moamoa.domain.auth.dto.response.SigninMemberResponseDto;
import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.member.repository.MemberRepository;
import com.wallaby.moamoa.security.JwtTokenManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public ResponseDto<?> signup(SignupMemberRequestDto signupMemberRequestDto) {
        memberRepository.findByPlatformUuid(signupMemberRequestDto.getSocialsUuid())
                .ifPresent(member -> {
                    throw new CustomException(ErrorCode.ALREADY_SIGN_OUTED_CUSTOMER);
                });

        Member member = buildMember(signupMemberRequestDto);

        memberRepository.save(member);

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS,null);
    }
    
    public ResponseDto<?> signin(SigninMemberRequestDto signinMemberRequestDto) {
        Member member = memberRepository.findByPlatformUuid(signinMemberRequestDto.getUuid())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_DENIED));

        authenticate(member.getPlatformUuid());

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String accessToken = jwtTokenManager.genUniqueAccessToken(
                member.getPlatformUuid(), generateAuthorities(), valueOperations
        );

        String refreshToken = jwtTokenManager.genRefreshToken(
                member.getPlatformUuid()
        );

        valueOperations.set(member.getPlatformUuid(), accessToken);
        valueOperations.set(accessToken, refreshToken);

        member.modifyFcmToken(signinMemberRequestDto.getFcmToken());

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, SigninMemberResponseDto.buildSigninMemberResponseDto(accessToken));
    }

    private List<GrantedAuthority> generateAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        return authorities;
    }

    private void authenticate(String id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(id, "");

        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }


    private Member buildMember(SignupMemberRequestDto signupMemberRequestDto) {
        return Member.builder()
                .platformType(signupMemberRequestDto.getSocialsType())
                .platformUuid(signupMemberRequestDto.getSocialsUuid())
                .build();
    }

    private Member buildMember(String platformUuid, SocialsType socialsType) {
        return Member.builder()
                .platformType(socialsType)
                .platformUuid(platformUuid)
                .build();
    }
}
