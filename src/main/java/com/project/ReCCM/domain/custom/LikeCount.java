package com.project.ReCCM.domain.custom;

import com.project.ReCCM.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LikeCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_id", nullable = false)  // 게시글 ID
    private Custom custom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = " member_id", nullable = false)  // 사용자 ID
    private Member member;



}
