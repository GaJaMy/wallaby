package com.wallaby.moamoa.domain.settlement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wallaby.moamoa.domain.category.entity.CategoryType;
import com.wallaby.moamoa.domain.expense.entity.ExpenseType;
import com.wallaby.moamoa.domain.expense.param.BuildExpenseParam;
import com.wallaby.moamoa.domain.settlement.entity.SettlementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSettlementRequestDto {
    private long groupId; // 모임 아이디

    private String payerUuid; // 돈을 낸사람 uuid

    private String expenseName; // 지출 이름

    private String location; // 지출 장소

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time; // 지출 시간

    private int totalAmount; // 총 지출 돈

    private ExpenseType expenseType; // 지출 종류 : 고정 지출, 일반 지출

    private SettlementType settlementType; // 정산 종류 : 더치 페이, 가격별, 비율, 더내기

    private ArrayList<RegisterSettlementDto> registerSettlementDtos; // 지출 내역

    private Long categoryId; // 카테고리

    public BuildExpenseParam toExpenseParam() {
        return BuildExpenseParam.builder()
                .name(getExpenseName())
                .location(getLocation())
                .time(getTime())
                .totalAmount(getTotalAmount())
                .expenseType(getExpenseType())
                .build();
    }
}
