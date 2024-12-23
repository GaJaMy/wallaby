package com.wallaby.moamoa.domain.group.service;

import com.wallaby.moamoa.common.dto.response.EmptyData;
import com.wallaby.moamoa.common.dto.response.ResponseDto;
import com.wallaby.moamoa.common.exception.CustomException;
import com.wallaby.moamoa.common.exception.ErrorCode;
import com.wallaby.moamoa.common.util.response.ResponseMaker;
import com.wallaby.moamoa.domain.group.dto.request.RegisterGroupsRequestDto;
import com.wallaby.moamoa.domain.group.entity.Group;
import com.wallaby.moamoa.domain.group.repository.GroupsRepository;
import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.member.service.MemberService;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroup;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroupId;
import com.wallaby.moamoa.domain.memberGroup.service.MemberGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupsService {
    private final MemberGroupService memberGroupService;
    private final MemberService memberService;

    private final GroupsRepository groupsRepository;

    @Transactional
    public ResponseDto<?> registerGroups(String uuid, RegisterGroupsRequestDto registerGroupsRequestDto) {
        Member member = memberService.getMember(uuid);

        Group newGroup = buildGroup(registerGroupsRequestDto);

        Group saved = groupsRepository.save(newGroup);

        memberGroupService.buildMemberGroup(member, saved);

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, EmptyData.builder().build());
    }

    @Transactional
    public ResponseDto<?> editGroupNickName(String uuid, Long groupId, String nickName) {
        Group group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_DENIED));

        memberGroupService.editNickName(uuid, groupId, nickName);

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, EmptyData.builder().build());
    }

    @Transactional
    public ResponseDto<?> addMemberByQr() {

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, EmptyData.builder().build());
    }

    @Transactional
    public ResponseDto<?> addMemberByUrl() {

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, EmptyData.builder().build());
    }

    public Group buildGroup(RegisterGroupsRequestDto registerGroupsRequestDto) {
        return Group.builder()
                .groupName(registerGroupsRequestDto.getGroupName())
                .currency(registerGroupsRequestDto.getCurrencyType())
                .build();
    }
}
