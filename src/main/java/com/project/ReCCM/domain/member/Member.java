package com.project.ReCCM.domain.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String memberName;

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
