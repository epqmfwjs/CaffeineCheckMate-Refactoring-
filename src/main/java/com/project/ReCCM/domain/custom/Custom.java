package com.project.ReCCM.domain.custom;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ReCCM.Repository.custom.*;
import com.project.ReCCM.domain.BaseTimeEntity;
import com.project.ReCCM.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Data
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

    @Column
    private String brand;

    @Column
    private String syrup;

    @Column
    private String whipped;

    @Column
    private String shot;

    @Column
    private String milk;

    @Column
    private String coffeeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 멤버와 게시글에 대한 n : n 관계를 like엔티티를 통해 매핑
    @JsonIgnore
    @OneToMany(mappedBy = "custom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeCount> likes;

    public Custom(CustomPostRequestDto requestDto) {
        this.customTitle = requestDto.getCustomTitle();
        this.customContent = requestDto.getCustomContent();
        this.brand = String.valueOf(requestDto.getBrand());
        this.syrup = String.valueOf(requestDto.getSyrup());
        this.whipped = String.valueOf(requestDto.getWhipped());
        this.shot = String.valueOf(requestDto.getShot());
        this.milk = String.valueOf(requestDto.getMilk());
        this.coffeeType = String.valueOf(requestDto.getCoffeeType());
    }

    public int getLikesCount() {
        return likes.size(); // 좋아요 개수 반환
    }
}
