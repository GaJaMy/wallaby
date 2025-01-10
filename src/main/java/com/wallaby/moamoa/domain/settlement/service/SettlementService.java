package com.wallaby.moamoa.domain.settlement.service;

import com.wallaby.moamoa.common.dto.response.EmptyData;
import com.wallaby.moamoa.common.dto.response.ResponseDto;
import com.wallaby.moamoa.common.exception.ErrorCode;
import com.wallaby.moamoa.common.util.response.ResponseMaker;
import com.wallaby.moamoa.domain.category.entity.Category;
import com.wallaby.moamoa.domain.category.service.CategoryService;
import com.wallaby.moamoa.domain.expense.entity.Expense;
import com.wallaby.moamoa.domain.expense.service.ExpenseService;
import com.wallaby.moamoa.domain.group.entity.Group;
import com.wallaby.moamoa.domain.group.service.GroupsService;
import com.wallaby.moamoa.domain.member.entity.Member;
import com.wallaby.moamoa.domain.member.service.MemberService;
import com.wallaby.moamoa.domain.settlement.dto.request.RegisterSettlementDto;
import com.wallaby.moamoa.domain.settlement.dto.request.RegisterSettlementRequestDto;
import com.wallaby.moamoa.domain.settlement.entity.Settlement;
import com.wallaby.moamoa.domain.settlement.entity.SettlementType;
import com.wallaby.moamoa.domain.settlement.repository.SettlementRepository;
import com.wallaby.moamoa.domain.settlementDetail.entity.SettlementDetail;
import com.wallaby.moamoa.domain.settlementDetail.service.SettlementDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final GroupsService groupsService;
    private final ExpenseService expenseService;
    private final MemberService memberService;
    private final SettlementDetailService settlementDetailService;
    private final CategoryService categoryService;


    @Transactional
    public ResponseDto<?> registerSettlement(RegisterSettlementRequestDto registerSettlementRequestDto) {
        // 돈을 낼사람을 가져옴
        Member payer = memberService.getMember(registerSettlementRequestDto.getPayerUuid());

        // 그룹을 가져옴
        Group group = groupsService.getGroup(registerSettlementRequestDto.getGroupId());

        // 카테고리 가져오기
        Category category = categoryService.getCategory(registerSettlementRequestDto.getCategoryId());

        // 지출 생성
        Expense expense = expenseService.buildExpense(registerSettlementRequestDto.toExpenseParam());

        // 지출에 대한 정산 정보 생성
        Settlement settlement = buildSettlement(payer, registerSettlementRequestDto.getSettlementType());

        ArrayList<RegisterSettlementDto> settlementDtos = registerSettlementRequestDto.getRegisterSettlementDtos();

        for (RegisterSettlementDto dto : settlementDtos) {
            Member payee = memberService.getMember(dto.getPayeeUuid());
            SettlementDetail settlementDetail = settlementDetailService.buildSettlementDetail(payee, dto.getAmount());
            settlement.addSettlementDetail(settlementDetail);
        }

        // 지출과 정산을 연관 시켜줌
        expense.setSettlement(settlement);
        expense.setCategory(category);

        group.addExpense(expense);

        return ResponseMaker.buildResponse(ErrorCode.SUCCESS, EmptyData.builder().build());
    }


    private boolean validateNickName(String nickName, Group group) {
        return groupsService.validateGroupsNickName(nickName,group);
    }

    private Settlement buildSettlement(Member payer, SettlementType settlementType) {
        return Settlement.builder()
                .settlementType(settlementType)
                .payer(payer)
                .build();
    }
}
