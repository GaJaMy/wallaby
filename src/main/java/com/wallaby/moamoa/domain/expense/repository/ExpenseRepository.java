package com.wallaby.moamoa.domain.expense.repository;

import com.wallaby.moamoa.domain.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
