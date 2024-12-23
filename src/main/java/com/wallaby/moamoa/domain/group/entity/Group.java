package com.wallaby.moamoa.domain.group.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupId;

    @Column(nullable = false)
    private String groupName;

    private String notification;

    private int budget;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CurrencyType currency;

    private String backgroundImage;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<MemberGroup> memberGroup = new ArrayList<>();

    public void addMemberGroup(MemberGroup memberGroup) {
        if (!this.memberGroup.contains(memberGroup)) {
            this.memberGroup.add(memberGroup);
            if (memberGroup != null && memberGroup.getGroup() != this) {
                memberGroup.setGroup(this);
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
}
