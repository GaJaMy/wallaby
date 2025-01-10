package com.wallaby.moamoa.domain.expense.entity;

import com.wallaby.moamoa.domain.category.entity.Category;
import com.wallaby.moamoa.domain.category.entity.CategoryType;
import com.wallaby.moamoa.domain.comment.entity.Comment;
import com.wallaby.moamoa.domain.group.entity.Group;
import com.wallaby.moamoa.domain.settlement.entity.Settlement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Settlement settlement;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer totalAmount;

    @Enumerated(value = EnumType.STRING)
    private ExpenseType expenseType;

    @Column(nullable = false)
    private LocalDateTime time;

    public void setGroup(Group group) {
        if (this.group != group) {
            this.group = group;
            if (group != null && !group.getExpenses().contains(this)) {
                group.addExpense(this);
            }
        }
    }

    public void setSettlement(Settlement settlement) {
        if (this.settlement != settlement) {
            this.settlement = settlement;
            if (settlement != null && settlement.getExpense() != this) {
                settlement.setExpense(this);
            }
        }
    }

    public void setCategory(Category category) {
        if (this.category != category) {
            this.category = category;
            if(category != null && category.getExpense() != this) {
                category.setExpense(this);
            }
        }
    }
}
