package com.project.ReCCM.domain.custom;

import com.project.ReCCM.domain.BaseTimeEntity;
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
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 댓글이 속한 게시글과의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_id", nullable = false)
    private Custom custom; // 댓글이 속한 게시글과의 관계

}
