package com.project.ReCCM.domain.custom;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Custom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK값

    @Column
    private String customTitle; //제목

    @Column
    private String customContent; //내용

    @Column
    private LocalDateTime customRegdate; //작성날짜

    @Column
    private String imgReal; // 이미지 오리지널네임

    @Column
    private String imgCopy; // 이미지 카피네임
    // 다른 필드들

}
