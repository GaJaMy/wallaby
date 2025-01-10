package com.wallaby.moamoa.domain.settlementDetail.service;

import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.settlementDetail.entity.SettlementDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettlementDetailService {
    public SettlementDetail buildSettlementDetail(Member payee, Integer amount) {
        return SettlementDetail.builder()
                .payee(payee)
                .amount(amount)
                .build();
    }
}
