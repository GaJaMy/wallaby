package com.wallaby.moamoa.domain.group.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wallaby.moamoa.domain.category.entity.Category;
import com.wallaby.moamoa.domain.expense.entity.Expense;
import com.wallaby.moamoa.domain.memberGroup.entity.MemberGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String groupName;

    private String notification;

    private int budget;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CurrencyType currency;

    private String backgroundImage;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberGroup> memberGroups = new ArrayList<>();

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    public void addMemberGroup(MemberGroup memberGroup) {
        if (!this.memberGroups.contains(memberGroup)) {
            this.memberGroups.add(memberGroup);
            if (memberGroup != null && memberGroup.getGroup() != this) {
                memberGroup.setGroup(this);
            }
        }
    }

    public void addExpense(Expense expense) {
        if (!this.expenses.contains(expense)) {
            this.expenses.add(expense);
            if (expense != null && expense.getGroup() != this) {
                expense.setGroup(this);
            }
        }
    }

    public void addCategory(Category category) {
        if (!this.categories.contains(category)) {
            this.categories.add(category);
            if (category != null && category.getGroup() != this) {
                category.setGroup(this);
            }
        }
    }
}
