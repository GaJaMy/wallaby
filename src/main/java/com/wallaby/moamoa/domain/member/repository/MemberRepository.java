package com.wallaby.moamoa.domain.member.repository;

import com.wallaby.moamoa.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByPlatformUuid(String PlatformUuid);
}
