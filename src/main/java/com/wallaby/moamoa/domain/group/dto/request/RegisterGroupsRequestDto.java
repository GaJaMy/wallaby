package com.wallaby.moamoa.domain.group.dto.request;

import com.wallaby.moamoa.domain.group.entity.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterGroupsRequestDto {
    private String groupName;
    private CurrencyType currencyType;
}
