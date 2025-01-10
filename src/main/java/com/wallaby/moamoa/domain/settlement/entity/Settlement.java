package com.wallaby.moamoa.domain.settlement.entity;

import com.wallaby.moamoa.domain.expense.entity.Expense;
import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.settlementDetail.entity.SettlementDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settlement")
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @Enumerated(value = EnumType.STRING)
    private SettlementType settlementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_uuid")
    private Member payer;

    @OneToMany(mappedBy = "settlement", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @Builder.Default
    private List<SettlementDetail> settlementDetails = new ArrayList<>();

    public void setExpense(Expense expense) {
        if (this.expense != expense) {
            this.expense = expense;
            if (expense != null && expense.getSettlement() != this) {
                expense.setSettlement(this);
            }
        }
    }

    public void setPayer(Member payer) {
        if (this.payer != payer) {
            this.payer = payer;
            if (payer != null && !payer.getSettlements().contains(this)) {
                payer.addSettlement(this);
            }
        }
    }

    public void addSettlementDetail(SettlementDetail settlementDetail) {
        if (!this.settlementDetails.contains(settlementDetail)) {
            this.settlementDetails.add(settlementDetail);
            if (settlementDetail != null && settlementDetail.getSettlement() != this) {
                settlementDetail.setSettlement(this);
            }
        }
    }
}
