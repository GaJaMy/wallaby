package com.wallaby.moamoa.domain.member.service;

import com.wallaby.moamoa.common.dto.response.EmptyData;
import com.wallaby.moamoa.common.dto.response.ResponseDto;
import com.wallaby.moamoa.common.exception.CustomException;
import com.wallaby.moamoa.common.exception.ErrorCode;
import com.wallaby.moamoa.common.util.response.ResponseMaker;
import com.wallaby.moamoa.domain.auth.service.AuthService;
import com.wallaby.moamoa.domain.member.dto.request.EditMemberNickNameRequestDto;
import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> modifyMemberNickName(String platformUuid, EditMemberNickNameRequestDto editMemberNickNameRequestDto) {
        Member member = memberRepository.findByPlatformUuid(platformUuid)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_MATCHED_PASSWORD));

        member.modifyNickName(editMemberNickNameRequestDto.getNewNickName());

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, EmptyData.builder().build());
    }

    public Member getMember(String uuid) {
        return memberRepository.findByPlatformUuid(uuid)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_MATCHED_PASSWORD));
    }
}
