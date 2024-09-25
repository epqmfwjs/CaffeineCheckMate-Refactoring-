package com.project.ReCCM.domain.custom;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ReCCM.domain.BaseTimeEntity;
import com.project.ReCCM.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Custom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK값

    @Column
    private String customTitle; //제목

    @Column
    private String customContent; //내용

    @Column
    private String imgReal; // 이미지 오리지널네임

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 멤버와 게시글에 대한 n : n 관계를 like엔티티를 통해 매핑
    @JsonIgnore
    @OneToMany(mappedBy = "custom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeCount> likes;

    public int getLikesCount() {
        return likes.size(); // 좋아요 개수 반환
    }

    public Custom(String customTitle, String customContent) {
        this.customTitle = customTitle;
        this.customContent = customContent;
    }

}
