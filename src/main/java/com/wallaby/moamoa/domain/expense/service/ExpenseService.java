package com.wallaby.moamoa.domain.expense.service;

import com.wallaby.moamoa.domain.expense.entity.Expense;
import com.wallaby.moamoa.domain.expense.param.BuildExpenseParam;
import com.wallaby.moamoa.domain.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public Expense buildExpense(BuildExpenseParam buildExpenseParam) {
        return Expense.builder()
                .name(buildExpenseParam.getName())
                .location(buildExpenseParam.getLocation())
                .totalAmount(buildExpenseParam.getTotalAmount())
                .expenseType(buildExpenseParam.getExpenseType())
                .time(buildExpenseParam.getTime())
                .build();
    }
}
