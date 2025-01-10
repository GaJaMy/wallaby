package com.wallaby.moamoa.domain.settlement.repository;

import com.wallaby.moamoa.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement ,Long> {

}
