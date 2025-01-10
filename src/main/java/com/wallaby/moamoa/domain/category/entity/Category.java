package com.wallaby.moamoa.domain.category.entity;

import com.wallaby.moamoa.domain.expense.entity.Expense;
import com.wallaby.moamoa.domain.group.entity.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Expense expense;

    private String categoryName;

    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;

    private String categoryImage;

    public void setGroup(Group group) {
        if (this.group != group) {
            this.group = group;
            if (group != null && !group.getCategories().contains(this)) {
                group.addCategory(this);
            }
        }
    }

    public void setExpense(Expense expense) {
        if (this.expense != expense) {
            this.expense = expense;
            if (expense != null && expense.getCategory() != this) {
                expense.setCategory(this);
            }
        }
    }
}
