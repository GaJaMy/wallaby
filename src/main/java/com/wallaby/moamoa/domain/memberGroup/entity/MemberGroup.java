package com.wallaby.moamoa.domain.memberGroup.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wallaby.moamoa.domain.group.entity.Group;
import com.wallaby.moamoa.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_groups")
public class MemberGroup {
    @EmbeddedId
    private MemberGroupId memberGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("platformUuid")
    @JoinColumn(name = "platform_uuid")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    private String groupNickName;

    public void setMember(Member member) {
        if (this.member != member) {
            this.member = member;
            if (member != null && !member.getMemberGroup().contains(this)) {
                member.addMemberGroup(this);
            }
        }
    }

    public void setGroup(Group group) {
        if (this.group != group) {
            this.group = group;
            if (group != null && !group.getMemberGroup().contains(this)) {
                group.addMemberGroup(this);
            }
        }
    }

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime create_at;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime update_at;

    public void modifyGroupNickName(String groupNickName) {
        this.groupNickName = groupNickName;
    }
}
