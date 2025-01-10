package com.wallaby.moamoa.domain.settlement.dto.response;

import com.wallaby.moamoa.domain.settlement.dto.SettlementDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSettlementListResponseDto {
    @Builder.Default
    private ArrayList<SettlementDto> settlementList = new ArrayList<>();
}
