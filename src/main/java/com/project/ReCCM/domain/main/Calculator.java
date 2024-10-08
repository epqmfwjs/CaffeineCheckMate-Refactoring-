package com.project.ReCCM.domain.main;

import com.project.ReCCM.domain.BaseTimeEntity;
import com.project.ReCCM.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Calculator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // pk값

    private int caffeine;

    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 댓글이 속한 게시글과의 관계



    public Calculator(int caffeine, Member member) {
        this.caffeine = caffeine;
        this.member = member;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now(); // 엔티티가 저장되기 전에 현재 날짜로 초기화
    }

}
