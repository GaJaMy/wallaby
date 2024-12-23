package com.wallaby.moamoa.domain.memberGroup.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class MemberGroupId implements Serializable {
    private String platformUuid;
    private Long groupId;
}
