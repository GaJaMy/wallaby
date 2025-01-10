package com.wallaby.moamoa.domain.settlementDetail.entity;

import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.settlement.entity.Settlement;
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
@Table(name = "settlement_detail")
public class SettlementDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_uuid")
    private Member payee;

    public void setSettlement(Settlement settlement) {
        if (this.settlement != settlement) {
            this.settlement = settlement;
            if (settlement != null && !settlement.getSettlementDetails().contains(this)) {
                settlement.addSettlementDetail(this);
            }
        }
    }

    public void setPayee(Member payee) {
        if (this.payee != payee) {
            this.payee = payee;
            if (payee != null && !payee.getSettlementDetails().contains(this)) {
                payee.addSettlementDetail(this);
            }
        }
    }
}
