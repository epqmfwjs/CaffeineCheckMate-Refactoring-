package com.project.ReCCM.domain.member;

import com.project.ReCCM.domain.BaseTimeEntity;
import com.project.ReCCM.domain.custom.LikeCount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // pk값

    @Column(nullable = false, unique = true)
    private String memberId;  // 멤버아이디

    @Column(nullable = false)
    private String password;  // 멤버비번

    @Column(nullable = false)
    private String memberName;  // 멤버이름

    @Column(nullable = false)
    private String memberEmail;  //멤버메일

    @Column(nullable = false)
    private String memberAge;  //멤버생년월일

    @Column(nullable = false)
    private double memberWieght;  // 멤버몸무게(카페인계산때 필요)

    @Column(nullable = false)
    private String memberPhone;  // 멤버 폰번호

    @Column(nullable = false)
    private String imgReal;  // 멤버 프로필 이미지 주소
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberGender memberGender;  // 멤버 성별

    // 멤버와 게시글에 대한 n : n 관계를 like엔티티를 통해 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeCount> likes;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // PrePersist 메서드로 기본값 설정
    @PrePersist
    public void setDefaultRole() {
        if (this.role == null) {
            this.role = MemberRole.USER;  // 기본값 USER 설정
        }
    }

}
