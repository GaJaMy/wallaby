package com.wallaby.moamoa.domain.comment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wallaby.moamoa.domain.expense.entity.Expense;
import com.wallaby.moamoa.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_uuid")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @Column(nullable = false)
    private String contents;

    private Integer likeCount;

    private Integer hateCount;

    private Integer heartCount;

    public void setMember(Member member) {
        if (this.member != member) {
            this.member = member;
            if (member != null && !member.getComments().contains(this)) {
                member.addComment(this);
            }
        }
    }
}
