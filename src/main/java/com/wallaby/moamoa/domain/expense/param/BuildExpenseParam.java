package com.wallaby.moamoa.domain.expense.param;

import com.wallaby.moamoa.domain.expense.entity.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildExpenseParam {
    private String name;
    private String location;
    private Integer totalAmount;
    private ExpenseType expenseType;
    private LocalDateTime time;
}
