package com.project.ReCCM.Repository.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaffeineRequest {
    private Long coffeeId; // 커피 PK 값
    private Long memberId; // 로그인멤버 PK 값
    private int caffeineAmount; // 카페인 양
    private int calorie;
    private int sugar;




}
