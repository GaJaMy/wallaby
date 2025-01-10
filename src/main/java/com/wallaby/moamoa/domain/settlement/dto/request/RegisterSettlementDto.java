package com.wallaby.moamoa.domain.settlement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSettlementDto {
    private String payeeUuid; // 돈을 줄 사람 uuid
    private Integer amount; // 지출 할 돈
}
