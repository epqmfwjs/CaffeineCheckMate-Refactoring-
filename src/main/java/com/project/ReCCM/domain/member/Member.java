package com.project.ReCCM.domain.member;

import com.project.ReCCM.domain.BaseTimeEntity;
import com.project.ReCCM.domain.custom.LikeCount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")  // 과거 날짜만 허용
    @Column(nullable = false)
    private LocalDate memberAge;

    @NotNull(message = "회원의 체중은 null이 될 수 없습니다.")
    private Double memberWeight; // 멤버몸무게(카페인계산때 필요)

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
