package com.wallaby.moamoa.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wallaby.moamoa.common.type.SocialsType;
import com.wallaby.moamoa.domain.comment.entity.Comment;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroup;
import com.wallaby.moamoa.domain.settlement.entity.Settlement;
import com.wallaby.moamoa.domain.settlementDetail.entity.SettlementDetail;
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
    @Builder.Default
    private List<MemberGroup> memberGroup = new ArrayList<>();

    @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Settlement> settlements = new ArrayList<>();

    @OneToMany(mappedBy = "payee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SettlementDetail> settlementDetails = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime create_at;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime update_at;


    public void addMemberGroup(MemberGroup memberGroup) {
        if (!this.memberGroup.contains(memberGroup)) {
            this.memberGroup.add(memberGroup);
            if (memberGroup != null && memberGroup.getMember() != this) {
                memberGroup.setMember(this);
            }
        }
    }

    public void addSettlementDetail(SettlementDetail settlementDetail) {
        if (!this.settlementDetails.contains(settlementDetail)) {
            this.settlementDetails.add(settlementDetail);
            if (settlementDetail != null && settlementDetail.getPayee() != this) {
                settlementDetail.setPayee(this);
            }
        }
    }

    public void addSettlement(Settlement settlement) {
        if (!this.settlements.contains(settlement)) {
            this.settlements.add(settlement);
            if (settlement != null && settlement.getPayer() != this) {
                settlement.setPayer(this);
            }
        }
    }

    public void addComment(Comment comment) {
        if (this.comments.contains(comment)) {
            this.comments.add(comment);
            if (comment != null && comment.getMember() != this) {
                comment.setMember(this);
            }
        }
    }

    public void modifyFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void modifyNickName(String nickName) {
        this.nickName = nickName;
    }
}
