package com.wallaby.moamoa.domain.group.repository;

import com.wallaby.moamoa.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupsRepository extends JpaRepository<Group, Long> {
}
