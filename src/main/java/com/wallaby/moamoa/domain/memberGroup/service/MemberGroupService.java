package com.wallaby.moamoa.domain.memberGroup.service;

import com.wallaby.moamoa.common.exception.CustomException;
import com.wallaby.moamoa.common.exception.ErrorCode;
import com.wallaby.moamoa.domain.group.entity.Group;
import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroup;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroupId;
import com.wallaby.moamoa.domain.memberGroup.repository.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberGroupService {
    private final MemberGroupRepository memberGroupRepository;

    public void buildMemberGroup(Member member, Group group) {
        MemberGroupId memberGroupId = MemberGroupId.builder()
                .groupId(group.getGroupId())
                .platformUuid(member.getPlatformUuid())
                .build();

        MemberGroup newMemberGroup = MemberGroup.builder()
                .memberGroupId(memberGroupId)
                .build();

        newMemberGroup.setMember(member);
        newMemberGroup.setGroup(group);

        memberGroupRepository.save(newMemberGroup);
    }

    public void editNickName(String uuid, Long groupId, String groupNickName) {
        MemberGroupId memberGroupId = MemberGroupId.builder()
                .platformUuid(uuid)
                .groupId(groupId)
                .build();

        MemberGroup memberGroup = memberGroupRepository.findById(memberGroupId)
                .orElseThrow(() -> new CustomException(ErrorCode.ALREADY_SIGN_OUTED_CUSTOMER));

        memberGroup.modifyGroupNickName(groupNickName);
    }
}
