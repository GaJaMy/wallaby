package com.wallaby.moamoa.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wallaby.moamoa.common.type.SocialsType;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id
    private String platformUuid;

    private String nickName;

    @Enumerated(value = EnumType.STRING)
    private SocialsType platformType;

    private String fcmToken;

    @Column(nullable = false)
    private String profileImageUrl;

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<MemberGroup> memberGroup = new ArrayList<>();

    public void addMemberGroup(MemberGroup memberGroup) {
        if (!this.memberGroup.contains(memberGroup)) {
            this.memberGroup.add(memberGroup);
            if (memberGroup != null && memberGroup.getMember() != this) {
                memberGroup.setMember(this);
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

    public void modifyFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void modifyNickName(String nickName) {
        this.nickName = nickName;
    }
}
