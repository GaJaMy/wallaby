package com.wallaby.moamoa.domain.memberGroup.repository;

import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroup;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, MemberGroupId> {

}
