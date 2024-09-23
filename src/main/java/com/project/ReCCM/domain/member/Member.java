package com.project.ReCCM.domain.member;

import com.project.ReCCM.domain.custom.LikeCount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
